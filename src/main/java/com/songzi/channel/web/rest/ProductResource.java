package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;

import com.songzi.channel.domain.enumeration.ProductType;
import com.songzi.channel.domain.enumeration.Status;
import com.songzi.channel.repository.ProductRepository;
import com.songzi.channel.security.AuthoritiesConstants;
import com.songzi.channel.security.SecurityUtils;
import com.songzi.channel.service.ProductService;
import com.songzi.channel.web.rest.errors.BadRequestAlertException;
import com.songzi.channel.web.rest.util.HeaderUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
@Api(value = "测试管理", description = "已测：测试管理")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    private final ProductRepository productRepository;

    private final ProductService productService;

    public ProductResource(ProductRepository productRepository, ProductService productService) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param product the product to create
     * @return the ResponseEntity with status 201 (Created) and with body the new product, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    @Timed
    @ApiOperation(value = "已测；创建测试，不能有id")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Product result = productService.save(product);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param product the product to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated product,
     * or with status 400 (Bad Request) if the product is not valid,
     * or with status 500 (Internal Server Error) if the product couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    @Timed
    @ApiOperation(value = "已测；更新测试")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to update Product : {}", product);
        if (product.getId() == null) {
            return createProduct(product);
        }
        Product result = productService.update(product);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, product.getId().toString()))
            .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    @Timed
    @ApiOperation(value = "已测；测试列表")
    public ResponseEntity<List<Product>> getAllProducts(Pageable pageable,
                                                        @ApiParam(value = "渠道名称") @RequestParam(required = false) String name,
                                                        @ApiParam(value = "价格") @RequestParam(required = false) Double price,
                                                        @ApiParam(value = "状态, 取值范围 delete, normal") @RequestParam(required = false) String status,
                                                        @ApiParam(value = "商品分类 取值范围 性格, 事业, 爱情, 财富, 运势, 风水, 占卜, 起名, 择吉, 命理") @RequestParam(required = false) String type) {

        log.debug("REST request to get a page of Products");
        Product product = new Product();
        if(name != null)
            product.setName(name);
        if(price != null)
            product.setPrice(price);
        if(status != null)
            product.setStatus(Status.valueOf(status.toUpperCase()));
        if(type != null)
            product.setProductType(ProductType.valueOf(type.toUpperCase()));

        Page<Product> page = productService.getAllProducts(product, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        List<Product> content = productService.getAllProductsList(page);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the product, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    @Timed
    @ApiOperation(value = "已测；测试详情")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Product product = productRepository.findOneWithChannelsById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(product));
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the product to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    @Timed
    @ApiOperation(value = "已测；删除测试")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
