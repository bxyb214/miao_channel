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

    //总销售额，月同比
    public static final String TOTAL_SALES_M2M = "total_sales_m2m";

    //总销售额，日同比
    public static final String TOTAL_SALES_D2D = "total_sales_d2d";

    //日均销售额
    public static final String TOTAL_SALES_DAILY = "日销售额";

    //访问量pv
    public static final String TOTAL_PV = "total_pv";

    //日访问量pv
    public static final String TOTAL_PV_DAILY = "日访问量";

    //月访问量pv
    public static final String TOTAL_PV_MONTHLY = "total_pv_monthly";

    //年访问量pv
    public static final String TOTAL_PV_YEARLY = "total_pv_yearly";

    //访问量uv
    public static final String TOTAL_UV = "total_uv";

    //日访问量uv
    public static final String TOTAL_UV_DAILY = "日访客数";

    //交易笔数
    public static final String TOTAL_SALES_NUMBER = "total_sales_number";

    //交易转化率
    public static final String TOTAL_SALES_CONVERSION = "total_sales_conversion";


    public static final String SEX_MALE = "male";

    public static final String SEX_FEMALE = "female";

    public static final String AGE_SEVENTEEN = "0-17";

    public static final String AGE_TWENTY_FOUR = "18-24";

    public static final String AGE_TWENTY_NINE = "25-29";

    public static final String AGE_THIRTY_FOUR = "30-34";

    public static final String AGE_THIRTY_NINE = "35-39";

    public static final String AGE_FIFTY = "40-50";

    public static final String UNKNOWN = "unknown";

    private Constants() {
    }


}
