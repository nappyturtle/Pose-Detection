package com.capstone.self_training.helper;

import android.text.format.DateUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        return sdf.format(date);
    }

    public static String showPeriodOfTime(String createdTime) {
        try {
            String resultStr = null;
            long result = sdf.parse(getCurrentTime()).getTime() - sdf.parse(createdTime).getTime();
            if (result / DateUtils.YEAR_IN_MILLIS > 1) {
                result = result / DateUtils.YEAR_IN_MILLIS;
                resultStr = result + " năm trước";
            } else if (result / (DateUtils.DAY_IN_MILLIS * 30) > 1) {
                result = result / (DateUtils.DAY_IN_MILLIS * 30);
                resultStr = result + " tháng trước";
            } else if (result / DateUtils.WEEK_IN_MILLIS > 1) {
                result = result / DateUtils.WEEK_IN_MILLIS;
                resultStr = result + " tuần trước";
            } else if (result / DateUtils.DAY_IN_MILLIS > 1) {
                result = (int) result / DateUtils.DAY_IN_MILLIS;
                resultStr = result + " ngày trước";
            } else if (result / DateUtils.HOUR_IN_MILLIS > 1) {
                result = (int) result / DateUtils.HOUR_IN_MILLIS;
                resultStr = result + " giờ trước";
            } else if (result / DateUtils.MINUTE_IN_MILLIS > 1) {
                result = (int) result / DateUtils.MINUTE_IN_MILLIS;
                resultStr = result + " phút trước";
            } else if (result / DateUtils.SECOND_IN_MILLIS >= 0) {
                result = (int) result / DateUtils.SECOND_IN_MILLIS;
                resultStr = result + " giây trước";
            }
            return resultStr;
        } catch (Exception e) {
            System.out.println("sai");
        }
        return null;
    }
}
