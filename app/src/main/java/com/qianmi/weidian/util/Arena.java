package com.qianmi.weidian.util;

import android.content.Context;

import okhttp3.OkHttpClient;

//import com.qianmi.ares.douban.network.ResourceProxy;
//import com.qianmi.ares.douban.route.RouteManager;
//import com.qianmi.ares.utils.AppContext;
//import com.tencent.smtt.sdk.QbSdk;
//import com.tencent.smtt.sdk.TbsDownloader;

/**
 * Created by su on 2017/1/11.
 */

public class Arena {

    public static final String TAG = Arena.class.getSimpleName();
    public static boolean DEBUG = false;
    public static boolean SHOWERROR = false;
    /**
     * 可以额外设置主app的user-agent
     */
    private static String mHostUserAgent;

    /**
     * 可以通过设置OkHttpClient的方式实现共用
     */
    private static OkHttpClient mOkHttpClient;


    public static void initialize(final Context context) {
        AppContext.init(context);
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }
}
