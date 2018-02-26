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
import com.songzi.channel.service.manager.IPManager;
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
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service Implementation for managing JhiOrder.
 */
@Service
@Transactional
public class JhiOrderService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final JhiOrderRepository jhiOrderRepository;

    private final ChannelRepository channelRepository;

    private final ProductRepository productRepository;

    private final StatisticsRepository statisticsRepository;

    private final ProductStatisticsRepository productStatisticsRepository;

    private final PersonaRepository personaRepository;

    private final IPManager ipManager;

    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */
    private final static String apiKey = "sk_test_KCmz14nrfvbLzLmLWTPSOKGS";

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
                           StatisticsRepository statisticsRepository,
                           ProductStatisticsRepository productStatisticsRepository,
                           PersonaRepository personaRepository,
                           IPManager ipManager) {
        this.jhiOrderRepository = jhiOrderRepository;
        this.channelRepository = channelRepository;
        this.productRepository = productRepository;
        this.statisticsRepository = statisticsRepository;
        this.productStatisticsRepository = productStatisticsRepository;
        this.personaRepository = personaRepository;
        this.ipManager = ipManager;
        init();
    }

    private void init() {
        Pingpp.apiKey = apiKey;
        Pingpp.privateKeyPath = "rsa_private_key_pkcs8.pem";

        LocalDate today = LocalDate.now();

        Statistics salesDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.SALES_DAILY, today);
        if (salesDailyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_SALES_DAILY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.SALES_DAILY);
            statistics.setDate(today);
            statisticsRepository.save(statistics);
        }
    }

    /**
     * Save a jhi_order.
     *
     * @param orderVM
     * @param ip
     * @return the persisted entity
     */
    public JhiOrder save(OrderVM orderVM, String ip) {
        log.debug("Request to save JhiOrder : {}", orderVM);

        Channel channel = channelRepository.findOne(orderVM.getChannelId());
        Product product = productRepository.findOne(orderVM.getProductId());
        JhiOrder order = new JhiOrder();
        order.setBirthInfo(orderVM.getBirthInfo());
        order.setChannelId(orderVM.getProductId());
        order.setChannelName(channel.getName());
        order.setProductId(orderVM.getProductId());
        order.setProductName(product.getName());
        order.setPrice(orderVM.getPrice());
        order.setSexInfo(SexType.valueOf(orderVM.getSexInfo()));
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

    public Page<JhiOrder> findAllByOrderDateBetween(Example order, Pageable pageable) {

        return jhiOrderRepository.findAll(order, pageable);
    }

    public List<JhiOrder> findAllByOrderDateBetween(Example order) {
        return jhiOrderRepository.findAll(order);
    }


    /**
     * 创建 Customs
     * <p>
     * 创建 Customs 用户需要组装一个 map 对象作为参数传递给 Customs.create();
     * map 里面参数的具体说明请参考：https://www.pingxx.com/api
     *
     * @return Charge
     */
    public Customs tryToPay(Long orderId, String payType, String ip) {

        JhiOrder order = jhiOrderRepository.findOne(orderId);

        Customs obj = null;
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("order_no", "123456789");
        chargeParams.put("amount", order.getPrice() * 100);//订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        Map<String, String> app = new HashMap<>();
        app.put("id", appId);
        chargeParams.put("app", app);
        chargeParams.put("channel", payType);
        chargeParams.put("currency", "cny");
        chargeParams.put("client_ip", ip);
        chargeParams.put("subject", order.getChannelId() + "-" + order.getProductId());
        chargeParams.put("body", "test");
        try {
            //发起交易请求
            obj = Customs.create(chargeParams);
            log.info(obj.toString());
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
        return obj;
    }

    @Async
    public void updateOrderByHook(Event event) {
        Charge charge = (Charge) event.getData().getObject();
        JhiOrder order = jhiOrderRepository.findOneByCode(charge.getOrderNo());
        String payTypeStr = charge.getChannel();
        PayType payType = null;
        if (payTypeStr.equals("wx")) {
            payType = PayType.微信;
        } else if (payType.equals("alipay")) {
            payType = PayType.支付宝;
        }
        order.setPayType(payType);
        order.setStatus(OrderStatus.未支付);
        jhiOrderRepository.saveAndFlush(order);
        updateStatistic(order);
    }

    @Async
    void updateStatistic(JhiOrder order){

        //update statistic
        //SALES_TOTAL total_sales
        LocalDate today = LocalDate.now();
        statisticsRepository.updateCountByType(order.getPrice(), StatisticsType.SALES_TOTAL);

        //SALES_DAILY
        statisticsRepository.plusOneByStatisticsTypeAndDate(StatisticsType.SALES_DAILY, today);

        //SALES_MONTHLY
        statisticsRepository.plusOneByStatisticsTypeAndDate(StatisticsType.SALES_MONTHLY, today.withDayOfMonth(today.lengthOfMonth()));

        //SALES_TOTAL_D2D
        int salesTotalToday = statisticsRepository.getCountByTypeAndDate(StatisticsType.SALES_DAILY, today);
        int salesTotalYesterday = statisticsRepository.getCountByTypeAndDate(StatisticsType.SALES_DAILY, today.minusDays(-1));

        int salesTotalD2d = 0;

        if (salesTotalYesterday == 0){
            salesTotalD2d = salesTotalToday;
        }else {
            salesTotalD2d = salesTotalToday - salesTotalYesterday;
        }

        statisticsRepository.updateCountByType(salesTotalD2d, StatisticsType.SALES_TOTAL_D2D);

        //PAY_TOTAL
        statisticsRepository.plusOneByStatisticsType(StatisticsType.PAY_TOTAL);
        int payTotalCount = statisticsRepository.getCountByType(StatisticsType.PAY_TOTAL);

        //PAY_TOTAL_CONVERSION
        int payTotalConversion = 0;
        if (VisitService.getPvTotal() != 0){
            payTotalConversion = payTotalCount * 100 / (VisitService.getPvTotal() * 100) ;
        }
        statisticsRepository.updateCountByType(payTotalConversion, StatisticsType.PAY_TOTAL_CONVERSION);

        //CHANNEL_SALES
        statisticsRepository.updateCountByTypeAndName(1, StatisticsType.CHANNEL_SALES, order.getChannelName());

        //PRODUCT_SALES
        statisticsRepository.updateCountByTypeAndName(1, StatisticsType.PRODUCT_SALES, order.getProductName());

        //PRODUCT_SALES_MONTHLY
        statisticsRepository.updateCountByTypeAndName(1, StatisticsType.PRODUCT_SALES_MONTHLY, order.getProductName());

        //PRODUCT_CONVERSION
        int count = statisticsRepository.getCountByTypeAndName(StatisticsType.PRODUCT_SALES, order.getProductName());
        int productConversion = count * 100 / (VisitService.getProductUv().get(order.getProductId()) * 100);
        statisticsRepository.updateCountByTypeAndName(productConversion, StatisticsType.PRODUCT_CONVERSION, order.getProductName() + "");


        //ProductStatistics
        LocalDate lastMonthDay = LocalDate.now().minusMonths(-1);

        ProductStatistics ps = productStatisticsRepository.findOneByName(order.getProductName());
        ps.setCount(ps.getCount() + 1);
        int lastMonthCount = statisticsRepository.getCountByTypeAndNameAndDate(StatisticsType.PRODUCT_SALES_MONTHLY, order.getProductName(), lastMonthDay.withDayOfMonth(lastMonthDay.lengthOfMonth()));
        int thisMonthCount = statisticsRepository.getCountByTypeAndNameAndDate(StatisticsType.PRODUCT_SALES_MONTHLY, order.getProductName(), today.withDayOfMonth(today.lengthOfMonth()));

        int m2m = (thisMonthCount - lastMonthCount) *100 / (lastMonthCount * 100);
        ps.setM2m(m2m);

        //Persona
        String city = ipManager.getCity(order.getIp());
        //city_sales_number
        Persona citySalesNumber = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, order.getChannelId(), order.getProductId());
        if (citySalesNumber == null){
            citySalesNumber = new Persona();
            citySalesNumber.setName(city);
            citySalesNumber.setChannelId(order.getChannelId());
            citySalesNumber.setProductId(order.getProductId());
            citySalesNumber.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesNumber.setCount(1);
        }else{
            citySalesNumber.setCount(citySalesNumber.getCount() + 1);
        }
        personaRepository.save(citySalesNumber);

        Persona citySalesNumberAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, 0L, 0L);
        if (citySalesNumberAll == null){
            citySalesNumberAll = new Persona();
            citySalesNumberAll.setName(city);
            citySalesNumberAll.setChannelId(0L);
            citySalesNumberAll.setProductId(0L);
            citySalesNumberAll.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesNumberAll.setCount(1);
        }else{
            citySalesNumberAll.setCount(citySalesNumberAll.getCount() + 1);
        }
        personaRepository.save(citySalesNumberAll);

        Persona citySalesNumberProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, 0L, order.getProductId());
        if (citySalesNumberProduct == null){
            citySalesNumberProduct = new Persona();
            citySalesNumberProduct.setName(city);
            citySalesNumberProduct.setChannelId(0L);
            citySalesNumberProduct.setProductId(order.getProductId());
            citySalesNumberProduct.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesNumberProduct.setCount(1);
        }else{
            citySalesNumberProduct.setCount(citySalesNumberProduct.getCount() + 1);
        }
        personaRepository.save(citySalesNumberProduct);

        Persona citySalesNumberChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, 0L, order.getProductId());
        if (citySalesNumberChannel == null){
            citySalesNumberChannel = new Persona();
            citySalesNumberChannel.setName(city);
            citySalesNumberChannel.setChannelId(order.getChannelId());
            citySalesNumberChannel.setProductId(0L);
            citySalesNumberChannel.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesNumberChannel.setCount(1);
        }else{
            citySalesNumberChannel.setCount(citySalesNumberChannel.getCount() + 1);
        }
        personaRepository.save(citySalesNumberChannel);


        //city_sales_price
        Persona citySalesPrice = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, order.getChannelId(), order.getProductId());
        if (citySalesPrice == null){
            citySalesPrice = new Persona();
            citySalesPrice.setName(city);
            citySalesPrice.setChannelId(order.getChannelId());
            citySalesPrice.setProductId(order.getProductId());
            citySalesPrice.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesPrice.setCount(order.getPrice());
        }else{
            citySalesPrice.setCount(citySalesPrice.getCount() + order.getPrice());
        }
        personaRepository.save(citySalesPrice);

        Persona citySalesPriceAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, 0L, 0L);
        if (citySalesPriceAll == null){
            citySalesPriceAll = new Persona();
            citySalesPriceAll.setName(city);
            citySalesPriceAll.setChannelId(0L);
            citySalesPriceAll.setProductId(0L);
            citySalesPriceAll.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesPriceAll.setCount(order.getPrice());
        }else{
            citySalesPriceAll.setCount(citySalesPriceAll.getCount() + order.getPrice());
        }
        personaRepository.save(citySalesPriceAll);

        Persona citySalesPriceProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, 0L, order.getProductId());
        if (citySalesPriceProduct == null){
            citySalesPriceProduct = new Persona();
            citySalesPriceProduct.setName(city);
            citySalesPriceProduct.setChannelId(0L);
            citySalesPriceProduct.setProductId(order.getProductId());
            citySalesPriceProduct.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesPriceProduct.setCount(order.getPrice());
        }else{
            citySalesPriceProduct.setCount(citySalesPriceProduct.getCount() + order.getPrice());
        }
        personaRepository.save(citySalesPriceProduct);

        Persona citySalesPriceChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, order.getChannelId(), 0L);
        if (citySalesPriceChannel == null){
            citySalesPriceChannel = new Persona();
            citySalesPriceChannel.setName(city);
            citySalesPriceChannel.setChannelId(order.getChannelId());
            citySalesPriceChannel.setProductId(0L);
            citySalesPriceChannel.setPersonaType(PersonaType.CITY_SALES_NUMBER);
            citySalesPriceChannel.setCount(order.getPrice());
        }else{
            citySalesPriceChannel.setCount(citySalesPriceChannel.getCount() + order.getPrice());
        }
        personaRepository.save(citySalesPriceChannel);

        //sex
        String sexStr = order.getSexInfo().toString();
        Persona sex = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, order.getChannelId(), order.getProductId());
        if (sex == null){
            sex = new Persona();
            sex.setName(sexStr);
            sex.setChannelId(order.getChannelId());
            sex.setProductId(order.getProductId());
            sex.setPersonaType(PersonaType.SEX);
            sex.setCount(1);
        }else{
            sex.setCount(sex.getCount() + 1);
        }
        personaRepository.save(sex);

        Persona sexAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, 0L, 0L);
        if (sexAll == null){
            sexAll = new Persona();
            sexAll.setName(sexStr);
            sexAll.setChannelId(0L);
            sexAll.setProductId(0L);
            sexAll.setPersonaType(PersonaType.SEX);
            sexAll.setCount(1);
        }else{
            sexAll.setCount(sexAll.getCount() + 1);
        }
        personaRepository.save(sexAll);

        Persona sexProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, 0L, order.getProductId());
        if (sexProduct == null){
            sexProduct = new Persona();
            sexProduct.setName(sexStr);
            sexProduct.setChannelId(0L);
            sexProduct.setProductId(order.getProductId());
            sexProduct.setPersonaType(PersonaType.SEX);
            sexProduct.setCount(1);
        }else{
            sexProduct.setCount(sexProduct.getCount() + 1);
        }
        personaRepository.save(sexProduct);


        Persona sexChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, order.getChannelId(), 0L);
        if (sexChannel == null){
            sexChannel = new Persona();
            sexChannel.setName(sexStr);
            sexChannel.setChannelId(order.getChannelId());
            sexChannel.setProductId(0L);
            sexChannel.setPersonaType(PersonaType.SEX);
            sexChannel.setCount(1);
        }else{
            sexChannel.setCount(sexChannel.getCount() + 1);
        }
        personaRepository.save(sexChannel);
        //age

        LocalDate birth = LocalDate.parse(order.getBirthInfo(), formatter);
        int ageStr = Period.between(today, birth).getYears();
        Persona age = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, order.getChannelId(), order.getProductId());
        if (age == null){
            age = new Persona();
            age.setName(getAgeRange(ageStr));
            age.setChannelId(order.getChannelId());
            age.setProductId(order.getProductId());
            age.setPersonaType(PersonaType.AGE);
            age.setCount(1);
        }else{
            age.setCount(age.getCount() + 1);
        }
        personaRepository.save(age);

        Persona ageAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, 0L, 0L);
        if (ageAll == null){
            ageAll = new Persona();
            ageAll.setName(getAgeRange(ageStr));
            ageAll.setChannelId(0L);
            ageAll.setProductId(0L);
            ageAll.setPersonaType(PersonaType.AGE);
            ageAll.setCount(1);
        }else{
            ageAll.setCount(ageAll.getCount() + 1);
        }
        personaRepository.save(ageAll);

        Persona ageProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, 0L, order.getProductId());
        if (ageProduct == null){
            ageProduct = new Persona();
            ageProduct.setName(getAgeRange(ageStr));
            ageProduct.setChannelId(0L);
            ageProduct.setProductId(order.getProductId());
            ageProduct.setPersonaType(PersonaType.AGE);
            ageProduct.setCount(1);
        }else{
            ageProduct.setCount(ageProduct.getCount() + 1);
        }
        personaRepository.save(ageProduct);

        Persona ageChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, order.getChannelId(), 0L);
        if (ageChannel == null){
            ageChannel = new Persona();
            ageChannel.setName(getAgeRange(ageStr));
            ageChannel.setChannelId(order.getChannelId());
            ageChannel.setProductId(0L);
            ageChannel.setPersonaType(PersonaType.AGE);
            ageChannel.setCount(1);
        }else{
            ageChannel.setCount(ageChannel.getCount() + 1);
        }
        personaRepository.save(ageChannel);

    }

    @Scheduled(cron = "1 0 0 1/1 * *")
    public void resetDaily() {
        log.info("resetDaily");
        Statistics statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName("当天销售额");
        statistics.setType(StatisticsType.SALES_DAILY);
        statistics.setDate(LocalDate.now());
        statisticsRepository.save(statistics);
    }


    @Scheduled(cron = "1 0 0 * 1/1 *")
    public void resetMonthly() {
        log.info("resetMonthly");
        LocalDate today = LocalDate.now();
        Statistics statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName("当月销售额");
        statistics.setType(StatisticsType.SALES_MONTHLY);
        statistics.setDate(today.withDayOfMonth(today.lengthOfMonth()));
        statisticsRepository.save(statistics);

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            statistics = new Statistics();
            statistics.setCount(0);
            statistics.setName(product.getName());
            statistics.setType(StatisticsType.PRODUCT_SALES_MONTHLY);
            statistics.setDate(today.withDayOfMonth(today.lengthOfMonth()));
            statisticsRepository.save(statistics);

            ProductStatistics ps = new ProductStatistics();
            ps.setCount(0);
            ps.setName(product.getName());
            ps.setM2m(0);
            productStatisticsRepository.save(ps);
        }
    }

    private String getAgeRange(int age){
        if (age < 17 && age > 0){
            return Constants.AGE_SEVENTEEN;
        }else if (age <= 24){
            return Constants.AGE_TWENTY_FOUR;
        }else if (age <= 29) {
            return Constants.AGE_TWENTY_NINE;
        }else if (age <= 34) {
            return Constants.AGE_THIRTY_FOUR;
        }else if (age <= 39){
            return Constants.AGE_THIRTY_NINE;
        }else if (age <= 50){
            return Constants.AGE_FIFTY;
        }else {
            return Constants.UNKNOWN;
        }
    }


}
