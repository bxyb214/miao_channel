package com.songzi.channel.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "zh-cn";

    //总销售额
    public static final String TOTAL_SALES = "total_sales";

    //总销售额，周同比
    public static final String TOTAL_SALES_W2W = "total_sales_w2w";

    //总销售额，日同比
    public static final String TOTAL_SALES_D2D = "total_sales_d2d";

    //日均销售额
    public static final String TOTAL_SALES_DAILY = "total_sales_daily";

    //访问量pv
    public static final String TOTAL_PV = "total_pv";

    //日访问量pv
    public static final String TOTAL_PV_DAILY = "total_pv_daily";

    //月访问量pv
    public static final String TOTAL_PV_MONTHLY = "total_pv_monthly";

    //访问量uv
    public static final String TOTAL_UV = "total_uv";

    //日访问量uv
    public static final String TOTAL_UV_DAILY = "total_uv_daily";

    //交易笔数
    public static final String TOTAL_SALES_NUMBER = "total_sales_number";

    //交易转化率
    public static final String TOTAL_SALES_CONVERSION = "total_sales_conversion";

    private Constants() {
    }
}
