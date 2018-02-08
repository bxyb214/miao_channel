package com.songzi.channel.service;

import com.songzi.channel.domain.Authority;
import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.ProductRepository;
import com.songzi.channel.repository.VisitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductService(ProductRepository productRepository, ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
        this.productRepository = productRepository;
    }

    public Product save(Product product) {

        if (product.getChannels() != null) {
            Set<Channel> channels = product.getChannels().stream()
                .map(channel -> channelRepository.findOne(channel.getId()))
                .collect(Collectors.toSet());
            product.setChannels(channels);
        }
        return productRepository.save(product);
    }
}
