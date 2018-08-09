package cn.hyperchain.application.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author faxiang
 * @version v1.0
 * @since 2017/12/20
 */
public class DateUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间（格式 yyyy-MM-dd HH:mm:ss）
     *
     * @return 时间字符串
     */
    public static String now() {
        return convertDate2String(DATE_PATTERN);
    }

    /**
     * 获取昨天的时间（格式 yyyy-MM-dd HH:mm:ss）
     *
     * @return 时间字符串


    /**
     * 转换当前日期为指定的格式
     *
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String convertDate2String(String pattern) {
        return convertDate2String(new Date(), pattern);
    }


    /**
     * 转换日期为指定的格式
     *
     * @param date    待转换的日期对象
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String convertDate2String(Date date, String pattern) {
        SimpleDateFormat df;
        String returnValue = "";

        if (date != null) {
            df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }

        return returnValue;
    }
}
