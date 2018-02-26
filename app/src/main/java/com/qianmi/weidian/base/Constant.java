package com.qianmi.weidian.base;

/**
 * Created by Chen Haitao on 2015/7/7.
 */
public class Constant {

    public static String INTERFACE_URL="https://gatewaytest.bm001.com/";

    public static String TTF_VERSION = "TTF_VERSION";

    public static String ISLOGIN = "ISLOGIN";
    public static String TOKEN = "TOKEN";
    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";

    public static String GUIDE_ONE = "GUIDE_ONE";//登录之前的引导页
    public static String GUIDE_TWO = "GUIDE_TWO";//登录之后的通讯录引导、行业引导、感兴趣的人引导
    public static String GUIDE_THREE = "GUIDE_THREE";//我的人脉的新手引导
    public static String GUIDE_FOUR = "GUIDE_FOUR";//我的生意引导

    public static String NEW_CONTACT_GUIDE_END = "NEW_CONTACT_GUIDE_END";//人脉的新手引导
    public static String NEW_BUSINESS_GUIDE_END = "NEW_BUSINESS_GUIDE_END";//生意的新手引导

    public static String WX_INFO = "WX_INFO";//登录之后的通讯录引导、行业引导、感兴趣的人引导

    public static final String BUGLY_APPID = "a8a211c243";

    public static final int SCAN_REQ_CODE = 49374;

    public static final String COMMON_BUNDLE = "common.android.bundle";

    public static final int PERMISSION_PHONE_REQ_CODE = 10;


    public static class IntentValue {
        public static final String SCAN_CODE_URL = "scan_code_url";
        public static final String AUTH_REQ_CODE = "auth_request_code";
        public static final String STORE_BEAN = "store_bean";

    }

    public static class GuideVisibilityKey {
        public static final String VERSION_NAME = "key_version_name";
        public static final String KNOW_WHAT_NEW_VERSION = "know_what_new_in_version";
        public static final String SERVER_URL_DATA = "server_url_data";
        public static final String SERVER_ADMIN_URL = "server_url_admin";
        public static final String SERVER_SSO_URL = "server_sso_admin";
    }

    public static class TimeFormat {
        public static final String Day = "yyyy.MM.dd";
        public static final String Time = "HH:mm:ss";
    }

    public static class ResponseStatus {
        public static final String SUCCESS = "ok";
        public static final String FAIL = "fail";
    }



}
