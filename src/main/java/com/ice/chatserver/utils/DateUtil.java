package com.ice.chatserver.utils;


import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 时间格式（yyyy-MM-dd HH:mm:ss）
     */
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";


    /**
    * @author ice2020x
    * @Date: 2021/12/20
    * @Description: 时间格式（yyyy-MM）
    **/
    public static final String yyyy_MM = "yyyy-MM";


    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parseDate(String dateStr, String format) {
        Date date = null;
        if (!StringUtils.isEmpty(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/20
    * @Description: 获取两个时间相差（毫秒）
    **/
    public static long getTimeDelta(Date oldTime, Date newTime) {
        long NTime = newTime.getTime();
        long OTime = oldTime.getTime();
        return (NTime - OTime);
    }
}
