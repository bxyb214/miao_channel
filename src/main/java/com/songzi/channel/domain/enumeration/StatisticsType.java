package com.songzi.channel.domain.enumeration;

/**
 * The StatisticsType enumeration.
 */

public enum StatisticsType {

// 查询
    PAY_DAILY,
    SALES_TOTAL_D2D,
// 实时
    PV_TOTAL, PV_DAILY, UV_TOTAL, UV_DAILY,

// 每天
    PAY_TOTAL,
    SALES_TOTAL,
    SALES_DAILY,
    SALES_PRODUCT_CHANNEL_DAILY,
    SALES_PRODUCT_CHANNEL_TOTAL,
    UV_PRODUCT_CHANNEL_DAILY,
    UV_PRODUCT_CHANNEL_TOTAL,
    PV_PRODUCT_CHANNEL_DAILY,
    PV_PRODUCT_CHANNEL_TOTAL,
    PRODUCT_CONVERSION,

// 每月
    SALES_MONTHLY,
    PV_MONTHLY,
    SALES_PRODUCT_CHANNEL_MONTHLY,

// 每年
    SALES_YEARLY,
    PV_YEARLY
}
