package com.pdst.pdstserver.security;

public class SecurityConstants {

    public static final String JWT_SECRET = "JWTSecretKey";
    public static final String AUTHORITIES_KEY = "JWTAuthoritiesKey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String SIGN_UP_URL = "/account/sign-up";
    public static final String EDIT_URL = "/account/edit";
    public static final String GET_ALL_VIDEO = "/video/getAllVideosByDate";
    public static final String GET_ALL_CATEGORY = "/category/categories";

}
