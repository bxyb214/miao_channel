package com.songzi.channel.service;

import com.songzi.channel.domain.Authority;
import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import com.songzi.channel.domain.User;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.ProductRepository;
import com.songzi.channel.repository.UserRepository;
import com.songzi.channel.repository.VisitRepository;
import com.songzi.channel.security.AuthoritiesConstants;
import com.songzi.channel.security.SecurityUtils;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
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

    private final StatisticsService statisticsService;

    private final UserService userService;

    public ProductService(ProductRepository productRepository,
                          ChannelRepository channelRepository,
                          StatisticsService statisticsService,
                          UserService userService) {
        this.channelRepository = channelRepository;
        this.productRepository = productRepository;
        this.statisticsService = statisticsService;
        this.userService = userService;
    }

    public Product save(Product product) {

        if (product.getChannels() != null) {
            Set<Channel> channels = product.getChannels().stream()
                .map(channel -> channelRepository.findOne(channel.getId()))
                .collect(Collectors.toSet());
            product.setChannels(channels);
        }
        Product p = productRepository.save(product);
        statisticsService.createProductStatistics(p);

        return p;
    }

    public Page<Product> getAllProducts(Product product, Pageable pageable) {

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            Channel channel = userService.getCurrentUserChannel();
            return productRepository.findAllByChannels_Id(channel.getId(), pageable);
        }
        return productRepository.findAll(Example.of(product), pageable);
    }
}
