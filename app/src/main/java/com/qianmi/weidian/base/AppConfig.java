package com.qianmi.weidian.base;



import com.qianmi.weidian.CustomApplication;
import com.qianmi.weidian.util.SPUtils;
import com.qianmi.weidian.util.StrUtils;

import java.io.File;

/**
 * App配置类
 */
public final class AppConfig {

    // ---------- EPOS API ------------
    public static String SERVER_URL = "http://gatewaytest.bm001.com";//测试 http://gatewaytest.bm001.com;//开发http://gatewaykf.bm001.com

    public static String ADMIN_URL = "http://arena.bm001.com/";//线上

    public static String IMAGE_URL = "http://img.1000.com/qm-a-img/test2/";//测试2

    public static final String SIGN_SECRET = "secret";

    public static String SCAN_CODE_LOGIN_URL = "";

    public static final int PROJECT_ID = 13;

    public static final int SMALL_VERSION = 1;


    // 请不要直接使用，使用下面方法掉用;
    private static String TOKEN = "";
    private static String USER_ID = "";
    private static String USER_NAME = "";
    private static String ENV_ID = "";
    private static String TTF_VERSION = "";
    private static String NAME = "";
    //系统字体目录
    private static String ASSETS_FONT_ICONFONT = "fonts/iconfont.ttf";

    //路由接口地址
    private static String ROUTE_MAP_URL = "http://arena.bm001.com/api/map";

    //网络OK
    private static boolean NETWORK_OK = false;





    public static void setToken(String key) {
        AppConfig.TOKEN = key;
        SPUtils.put(CustomApplication.getInstance(), Constant.TOKEN, key);
    }

    public static String getToken() {
        if (StrUtils.isNull(TOKEN)) {
            TOKEN = (String) SPUtils.get(CustomApplication.getInstance(), Constant.TOKEN, TOKEN);
        }
        return TOKEN;
    }


    public static String getTTFVersion() {
        if (StrUtils.isNull(TTF_VERSION)) {
            TTF_VERSION = (String) SPUtils.get(CustomApplication.getInstance(), Constant.TTF_VERSION, TTF_VERSION);
        }
        return TTF_VERSION;
    }

    public static void setTTFVersion(String ttfVersion) {
        AppConfig.TTF_VERSION = ttfVersion;
        SPUtils.put(CustomApplication.getInstance(), Constant.TTF_VERSION, ttfVersion);
    }


    public static void setIsLogin(boolean isLogin) {
        SPUtils.put(CustomApplication.getInstance(), Constant.ISLOGIN, isLogin);
    }

    public static boolean getIsLogin() {
        if (CustomApplication.getInstance() == null) {
            return false;
        }
        Object obj = SPUtils.get(CustomApplication.getInstance(), Constant.ISLOGIN, false);
        if (obj != null) {
            return (boolean) obj;
        }

        return false;
    }


    public static String getAssetsFontIconfont(){
        return  ASSETS_FONT_ICONFONT;
    }

    /**
     * 设置登录之前的引导
     * @param isDid
     */
    public static void setGuideOne(boolean isDid) {
        SPUtils.put(CustomApplication.getInstance(), Constant.GUIDE_ONE, isDid);
    }

    public static boolean getGuideOne() {
        Object obj =  SPUtils.get(CustomApplication.getInstance(), Constant.GUIDE_ONE, false);
        if (obj != null){
            return (boolean) obj;
        }
        return false;
    }

    public static String getGuideTwo() {
        return (String) SPUtils.get(CustomApplication.getInstance(), Constant.GUIDE_TWO, "");
    }

    /**
     * 记录登录之后的引导
     * @param guideClass
     */
    public static void setGuideTwo(String guideClass) {
        SPUtils.put(CustomApplication.getInstance(), Constant.GUIDE_TWO, guideClass);
    }


    /**
     * 判断新手引导是否结束了，如果没结束，就是false，如果结束了就设置为true
     * @param isDid
     */
    public static void setContactNewGuideEnd(boolean isDid) {
        SPUtils.put(CustomApplication.getInstance(), Constant.NEW_CONTACT_GUIDE_END, isDid);
    }

    public static boolean getContactNewGuideEnd() {
        Object obj =  SPUtils.get(CustomApplication.getInstance(), Constant.NEW_CONTACT_GUIDE_END, false);
        if (obj != null){
            return (boolean) obj;
        }
        return false;
    }


    /**
     * 判断新手引导是否结束了，如果没结束，就是false，如果结束了就设置为true
     * @param isDid
     */
    public static void setBusinessNewGuideEnd(boolean isDid) {
        SPUtils.put(CustomApplication.getInstance(), Constant.NEW_BUSINESS_GUIDE_END, isDid);
    }

    public static boolean getBusinessNewGuideEnd() {
        Object obj =  SPUtils.get(CustomApplication.getInstance(), Constant.NEW_BUSINESS_GUIDE_END, false);
        if (obj != null){
            return (boolean) obj;
        }
        return false;
    }


    public static String getWxInfo() {
        return (String) SPUtils.get(CustomApplication.getInstance(), Constant.WX_INFO, "");
    }

    public static void setWxInfo(String wxInfo) {
        SPUtils.put(CustomApplication.getInstance(), Constant.WX_INFO, wxInfo);
    }

}
