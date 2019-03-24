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
    public static final String GET_ACCOUNT_BY_ID = "/account/update/{id}";
    public static final String GET_ALL_VIDEO_RELATED= "/video/getAllVideosRelatedByCourseId";
    public static final String GET_ALL_VIDEO_TRENDING = "/video/getAllVideosByTopNumOfView";
    public static final String GET_ALL_COURSE_WITH_PRICE = "/course/getAllCourses";
    public static final String CREATE_SUGGESTIONDETAIL = "/suggestiondetail/createSuggestionDetails";
    public static final String GET_ALL_FREE_VIDEO_BY_ACCOUNTID = "/video/getAllFreeVideosByAccount";
    public static final String GET_ALL_COURSES_WITH_PRICE_BY_ACCOUNTID = "/course/getAllCoursesWithPriceByAccountId";
    public static final String GET_TRAINER_INFO = "/account/getTrainerInfo";
    public static final String SEARCH_COURSE = "/course/search";
    public static final String SEARCH_VIDEO_ORDERED_BY_DATE = "/video/searchOrderByDate";
    public static final String SEARCH_VIDEO_ORDERED_BY_VIEW = "/video/searchOrderByView";
    public static final String GET_UNBOUGHT_COURSES = "/course/getUnboughtCourses";
    public static final String GET_ALL_ACCOUNT_BY_ROLE = "/account/getAllAccountByRoleId";
    public static final String GET_ALL_SUGGESTION = "/suggestion/suggestions";
    public static final String UPDATE_ACCOUNT = "/account/updateAccount";
    public static final String GET_DATAFORDASHBOARD = "/account/getDataForDashboard";
}
