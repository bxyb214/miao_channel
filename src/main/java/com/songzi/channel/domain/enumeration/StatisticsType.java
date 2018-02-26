package com.songzi.channel.domain.enumeration;

/**
 * The StatisticsType enumeration.
 * SALES_TOTAL 总销售额
 * SALES_DAILY 天销售额
 * SALES_MONTHLY 月销售额
 * SALES_YEARLY 年销售额
 * SALES_TOTAL_D2D 销售日增长额
 * PV_TOTAL 访问总量
 * PV_DAILY PV天量
 * PV_MONTHLY PV月量
 * UV_TOTAL UV总量
 * UV_DAILY UV天量 UV测试总量
 * UV_PRODUCT_TOTAL
 * PAY_TOTAL 支付笔数
 * PAY_DAILY 每天支付数
 * CHANNEL_SALES 渠道销售
 * PRODUCT_SALES 测试销售
 * PRODUCT_SALES_MONTHLY 周测试销售
 * PAY_TOTAL_CONVERSION 转换率
 */
public enum StatisticsType {
    SALES_TOTAL, SALES_DAILY, SALES_MONTHLY, SALES_YEARLY, SALES_TOTAL_D2D,
    PV_TOTAL, PV_DAILY, PV_MONTHLY, PV_YEARLY, UV_TOTAL, UV_DAILY, PAY_TOTAL,
    PAY_DAILY, PRODUCT_SALES, PRODUCT_CONVERSION, PAY_TOTAL_CONVERSION,
    CHANNEL_SALES, PRODUCT_SALES_MONTHLY, UV_PRODUCT_TOTAL
}
