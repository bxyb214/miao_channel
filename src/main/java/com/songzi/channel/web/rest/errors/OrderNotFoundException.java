package com.songzi.channel.web.rest.errors;

public class OrderNotFoundException extends BadRequestAlertException {

    public OrderNotFoundException() {
        super(ErrorConstants.ORDER_NOT_FOUND_TYPE, "订单号不存在", "orderService", "ordernotexists");
    }
}
