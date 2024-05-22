package org.iot.hotelitybackend.common.constant;

public class Constant {
    /* API Response */
    public static final int PAGE_SIZE = 20;
    public static final String KEY_TOTAL_PAGES_COUNT = "totalPagesCount";
    public static final String KEY_CURRENT_PAGE_INDEX = "currentPageIndex";
    public static final String KEY_CONTENT = "content";

    /* JWT attribute */
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_REFRESH_TOKEN = "refreshToken";
    public static final String REDIS_PREFIX = KEY_REFRESH_TOKEN;
    public static final int REDIS_TIME_TO_LIVE = 86400;     // 1day (60 * 60 * 24)
    public static final String JWT_ATTR_LOGIN_CODE = "loginCode";
    public static final String JWT_ATTR_ROLE = "role";

    public static final String KEY_AUTHORIZATION = "Authorization";

    public static final String MESSAGE_TOKEN_EXPIRED = "token expired..";
}
