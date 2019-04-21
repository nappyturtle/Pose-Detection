package com.pdst.pdstserver.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        return sdf.format(date);
    }
}
