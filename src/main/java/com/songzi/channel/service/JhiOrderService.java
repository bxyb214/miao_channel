package com.songzi.channel.service;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Customs;
import com.pingplusplus.model.Event;
import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.*;
import com.songzi.channel.domain.enumeration.*;
import com.songzi.channel.repository.*;
import com.songzi.channel.repository.support.Range;
import com.songzi.channel.security.AuthoritiesConstants;
import com.songzi.channel.security.SecurityUtils;
import com.songzi.channel.service.manager.IPManager;
import com.songzi.channel.web.rest.errors.OrderNotFoundException;
import com.songzi.channel.web.rest.vm.OrderVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Service Implementation for managing JhiOrder.
 */
@Service
@Transactional
public class JhiOrderService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final JhiOrderRepository jhiOrderRepository;

    private final ChannelRepository channelRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final UserService userService;


    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */
//    private final static String apiKey = "sk_test_KCmz14nrfvbLzLmLWTPSOKGS";
    private final static String apiKey = "sk_live_aDGCK4vTmvL4qHGm5CPiX5uT";

    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    private final static String appId = "app_9mHabPXPW9yTTCWH";

    /**
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
     * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
     * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
     */


    public JhiOrderService(JhiOrderRepository jhiOrderRepository,
                           ChannelRepository channelRepository,
                           ProductRepository productRepository,
                           ProductService productService,
                           UserService userService) {
        this.jhiOrderRepository = jhiOrderRepository;
        this.channelRepository = channelRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.userService = userService;
        init();
    }

    private void init() {
        Pingpp.apiKey = apiKey;
        Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXAIBAAKBgQCdQc6atxOQUXJJWHpLs6ku/6FfkQ4YlkNzXxgWl6ywinlLDdul\n" +
            "EKMqxyNPJ6nb6cl8KlYXoLn8ZCvuvIFLx1fGxWMt/KlWkeZ+L2A86wZLQd2T8R+1\n" +
            "H9Cz04hrKhPlky57+JSqrKWSW55EIi3XrrafXkb0eLtdHOIXlt0hrMtnWwIDAQAB\n" +
            "AoGAA+in1wan8NVlbtdJKuqLUQS/oR186TWK9aouchlRDI5Ul33/sKmqNJ0Ajq4g\n" +
            "tbuo63It86ZmGRTwVp3O5PBOiTNSDgCgqixh/l/4ZtVHaaFZXSKLgjD7evRW/2dp\n" +
            "TPinm0Qk15GDQ5u3c3Mpb6/g3b21ayAf1EvV18hyeQQZ63ECQQDPFY4WQZTcOUXb\n" +
            "XuH0EE4IvbyJSZye6bTlfhyCM2Zo1w/DqqdbR8Gu4oqCUjqAgNMV1fm8NUktZwpH\n" +
            "/o34iDPTAkEAwmcwh4m7ksNCZDoeMnuVEa4lDYe37254b7KoKvzhjKCq8wzyIe7Y\n" +
            "2YMf1XbnTn8XJ2oXQLIDpPgMKZWV+KIxWQJAM/Qac6b8a8vKpODHG25DxZmkhWT8\n" +
            "cDn98l2TVW8D0GW0RMi5uALmqrOXQF7U4oxvaNkIEJnlxAhH4gwRHjs9lQJAKtYk\n" +
            "GmmeHGvMMFuiFcungFrYLVAPlsHH5gz/A2HvrVYl9SKQJCwGOn+rPxVRSLm2d2fo\n" +
            "MchIB0zlGTCt+Dh9GQJBAJNMEdle9J9Am7PzOyQSJ62LZRlsqimJG/n5hOqCxB7T\n" +
            "VOoow0Y/jQ05k8L81ypK7AeEiBuLJSLS2vY5Nk0iX+Y=\n" +
            "-----END RSA PRIVATE KEY-----\n";

    }

    /**
     * Save a jhi_order.
     *
     * @param orderVM
     * @param ip
     * @return the persisted entity
     */
    public JhiOrder save(OrderVM orderVM, String ip) {

        if (productService.getCodeSet().contains(orderVM.getProductId() + "-" + orderVM.getChannelId()))
        log.debug("Request to save JhiOrder : {}", orderVM);

        Channel channel = channelRepository.findOneByCode(orderVM.getChannelId());
        Product product = productRepository.findOneByCode(orderVM.getProductId());
        JhiOrder order = new JhiOrder();
        order.setBirthInfo(orderVM.getBirthInfo());
        order.setChannelId(channel.getId());
        order.setChannelName(channel.getName());
        order.setProductId(product.getId());
        order.setProductName(product.getName());

        order.setPrice(product.getPrice());
        order.setProportionPrice(channel.getProportion());
        order.setProportionPrice(product.getPrice() * channel.getProportion());
        order.setSexInfo(orderVM.getSexInfo());
        order.setStatus(OrderStatus.未支付);
        order.setCode(new Date().getTime() + (int) (Math.random() * 9 + 1) * 1000000 + "");
        order.setOrderDate(Instant.now());
        order.setIp(ip);
        return jhiOrderRepository.save(order);
    }

    /**
     * Get all the jhi_orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JhiOrder> findAll(Pageable pageable) {
        log.debug("Request to get all Jhi_orders");
        return jhiOrderRepository.findAll(pageable);
    }

    /**
     * Get one jhi_order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public JhiOrder findOne(Long id) {
        log.debug("Request to get JhiOrder : {}", id);
        return jhiOrderRepository.findOne(id);
    }

    /**
     * Delete the jhi_order by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JhiOrder : {}", id);
        jhiOrderRepository.delete(id);
    }

    public Page<JhiOrder> findAllWithRange(JhiOrder order, List<Range<JhiOrder>> ranges, Pageable pageable) {

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            Long channelId = userService.getCurrentUserChannel().getId();
            order.setChannelId(channelId);
        }
        return jhiOrderRepository.queryByExampleWithRange(Example.of(order), ranges, pageable);
    }

    public List<JhiOrder> findAllByOrderDateBetween(JhiOrder order, List<Range<JhiOrder>> ranges) {
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            Long channelId = userService.getCurrentUserChannel().getId();
            order.setChannelId(channelId);
        }
        return jhiOrderRepository.queryByExampleWithRange(Example.of(order), ranges);
    }


    /**
     * 创建 Customs
     * <p>
     * 创建 Customs 用户需要组装一个 map 对象作为参数传递给 Customs.create();
     * map 里面参数的具体说明请参考：https://www.pingxx.com/api
     *
     * @return Charge
     */
    public Charge tryToPay(String orderNo, String payType, String ip) {

        JhiOrder order = jhiOrderRepository.findOneByCode(orderNo);

        Charge charge = null;


        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", order.getPrice() * 100);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", order.getChannelName() + "-" + order.getProductName());
        chargeMap.put("body", order.getChannelName() + "-" + order.getProductName());
        chargeMap.put("order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", payType);// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", ip); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如: 127.0.0.1
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);
        try {
            //发起交易请求
            charge = Charge.create(chargeMap);
            String chargeString = charge.toString();
            System.out.println(chargeString);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
        return charge;
    }

    @Async
    public void updateOrderByHook(Event event) {
        Charge charge = (Charge) event.getData().getObject();
        JhiOrder order = jhiOrderRepository.findOneByCode(charge.getOrderNo());
        if (order == null){
            log.info("订单号: [" + charge.getOrderNo() + "]不存在");
            throw new OrderNotFoundException();
        }
        String payTypeStr = charge.getChannel();
        try {
            order.setPayType(PayType.valueOf(payTypeStr));
        }catch (Exception e){
            log.info("不支持的支付方式:" + payTypeStr, e);
        }
        order.setStatus(OrderStatus.已支付);
        jhiOrderRepository.saveAndFlush(order);
    }

    public JhiOrder findOneByCode(String orderNo) {
        return jhiOrderRepository.findOneByCode(orderNo);
    }
}
