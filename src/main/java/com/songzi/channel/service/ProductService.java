package com.songzi.channel.service;

import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import com.songzi.channel.domain.enumeration.OrderStatus;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.JhiOrderRepository;
import com.songzi.channel.repository.ProductRepository;

import com.songzi.channel.security.AuthoritiesConstants;
import com.songzi.channel.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Service for Visit.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final ProductRepository productRepository;

    private final ChannelRepository channelRepository;

    private final UserService userService;

    private final JhiOrderRepository orderRepository;

    private Set<String> codeSet;



    public ProductService(ProductRepository productRepository,
                          ChannelRepository channelRepository,
                          JhiOrderRepository orderRepository,
                          UserService userService) {
        this.channelRepository = channelRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        initCodeSet();
    }

    private void initCodeSet(){
        codeSet =  new HashSet<>();
        List<Product> products = productRepository.findAllProductChannelCode();
        for (Product p : products) {
            for (Channel c : p.getChannels()){
                codeSet.add(p.getCode() + "-" + c.getCode());
                log.debug(p.getCode() + "-" + c.getCode());
            }
        }
    }


    public Product save(Product product) {

        if (product.getChannels() != null) {
            Set<Channel> channels = product.getChannels().stream()
                .map(channel -> channelRepository.findOne(channel.getId()))
                .collect(Collectors.toSet());
            product.setChannels(channels);
        }

        product.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
        Product p = productRepository.save(product);

        return p;
    }

    public Product update(Product product){
        Product oldProduct = productRepository.findOne(product.getId());
        product.setCode(oldProduct.getCode());
        return productRepository.save(product);
    }

    public Page<Product> getAllProducts(Product product, Pageable pageable) {

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            Channel channel = userService.getCurrentUserChannel();
            Page<Product> page =  productRepository.findAllByChannels_Id(channel.getId(), pageable);

            return page;
        }
        return productRepository.findAll(Example.of(product), pageable);
    }

    public List<Product> getAllProductsList(Page<Product> page) {
        List<Product> list = page.getContent();

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            Channel channel = userService.getCurrentUserChannel();
            for (Product p : list){
                p.setLink(p.getLink() + "?p=" + p.getCode() + "&c" + "=" + channel.getCode());
                Integer sold = orderRepository.countByChannelIdAndProductIdAndStatus(channel.getId(), p.getId(), OrderStatus.已支付);
                p.setSold(sold);

            }
        }else {

            for (Product product: list){
                Integer sold = orderRepository.countByProductIdAndStatus(product.getId(), OrderStatus.已支付);
                product.setSold(sold);
            }
        }


        return list;
    }

    public Set<String> getCodeSet() {
        return codeSet;
    }

    /**
     * 每分钟刷新productCode-channelCode
     */
    @Scheduled(cron = "0 0/1 * * * *")
    void resetCodeSet() {
        initCodeSet();
    }
}
