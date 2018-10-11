package com.xujiaji.todo.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * author: xujiaji
 * created on: 2018/9/4 10:19
 * description:
 */
public class DateFormatUtil {
    private static DateFormatUtil mInstance;
    private SimpleDateFormat format;

    private DateFormatUtil() {
        format = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    }

    public static DateFormatUtil getInstance() {
        if (mInstance == null) {
            synchronized (DateFormatUtil.class) {
                mInstance = new DateFormatUtil();
            }
        }
        return mInstance;
    }

    public String format(Object time) {
        return format.format(time);
    }

    public String format(long timestamp) {
        return format.format(timestamp);
    }

}
