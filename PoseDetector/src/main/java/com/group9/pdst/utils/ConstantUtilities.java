package com.group9.pdst.utils;

import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;

public class ConstantUtilities {
    public static int imgSize;
    public static Jedis jedis;
    public static String domain = "http://localhost:8090/";
    public static LocalDateTime startTime;
}
