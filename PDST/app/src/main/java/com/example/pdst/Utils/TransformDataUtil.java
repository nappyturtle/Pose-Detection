package com.example.pdst.Utils;

import java.util.Date;

public class TransformDataUtil {
    //This func calculate Time like format "17 gio truoc"
    public static String dateToTimestampAgoText(Date date){
        String text_time = "17 giờ trước";
        //Transform code here


        return  text_time;
    }
    //This func calculate Time like format "1,4 tr luot xem"
    public static String totalViewToText(int totalView){
        String total_text = "1,4 Tr lượt xem";
        //Transform code here

        return total_text;
    }
}
