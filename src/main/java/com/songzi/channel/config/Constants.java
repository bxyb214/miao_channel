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
    public static final String SALES_TOTAL = "总销售额";

    //日销售额
    public static final String SALES_DAILY = "日销售额";

    public static final String SALES_MONTHLY = "月销售额";

    public static final String SALES_YEARLY = "年销售额";

    public static final String SALES_PRODUCT_CHANNEL_DAILY = "销售额";

    public static final String SALES_TOTAL_D2D = "日增量";

    public static final String PRODUCT_CONVERSION = "转化率";

    public static final String PV_DAILY = "日访客量";

    public static final String PV_MONTHLY = "月访客量";

    public static final String PV_YEARLY = "年访客量";

    public static final String PV_TOTAL = "总访客量";

    public static final String UV_TOTAL = "总访客数";

    public static final String UV_DAILY = "日访客数";

    public static final String PAY_DAILY = "日支付笔数";

    public static final String PAY_TOTAL = "总支付笔数";

    public static final String SEX_MALE = "male";

    public static final String SEX_FEMALE = "female";

    public static final String AGE_SEVENTEEN = "0-17";

    public static final String AGE_TWENTY_FOUR = "18-24";

    public static final String AGE_TWENTY_NINE = "25-29";

    public static final String AGE_THIRTY_FOUR = "30-34";

    public static final String AGE_THIRTY_NINE = "35-39";

    public static final String AGE_FIFTY = "40-50";

    public static final String UNKNOWN = "unknown";

    private Constants() {}

    public class PAY_DAILY {
    }
}
