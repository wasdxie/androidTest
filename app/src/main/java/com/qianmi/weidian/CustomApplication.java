package com.qianmi.weidian;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.qianmi.weidian.util.Arena;
import com.qianmi.weidian.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 初始化application
 * <p>
 * Created by Chen Haitao on 2015/7/25.
 */
public class CustomApplication extends Application {

    {

    }

    private ArrayList<Activity> activities = new ArrayList<>();
    private Activity topActivity;

    private static CustomApplication instance;

    private boolean isDownloadRouter = false;



    private String routerPath;

    public static CustomApplication getInstance() {
        return instance;
    }

    private boolean isRunForeground = false;

    private static HashMap<String, String> sessionStorage = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        //arena初始化设置
        Arena.initialize(this);
        Arena.setDebug(BuildConfig.DEBUG);
        Arena.SHOWERROR = false;


    }


    private int count = 0;

    /**
     * 判断是否进入了后台（如果进入后台，才会检查微应用更新）
     */
    private void initLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.v("viclee", activity + "onActivityStopped");
                count--;
                if (count == 0) {
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                    isRunForeground = false;

                    startRequestRouter(routerPath, false);

                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.v("viclee", activity + "onActivityStarted");
                if (count == 0) {
                    isRunForeground = true;
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                }
                count++;

                topActivity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.v("viclee", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.v("viclee", activity + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.v("viclee", activity + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.v("viclee", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.v("viclee", activity + "onActivityCreated");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        try {
            MultiDex.install(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRequestRouter(String path, boolean preLoad) {
        if (isDownloadRouter) {
            return;
        }

    }

    public void addActivity(Activity activity) {
        if (null != activity) {
            LogUtils.d("********* add Activity " + activity.getClass().getName());
            activities.add(activity);
        }
    }


    public void removeActivity(Activity activity) {
        if (null != activity) {
            LogUtils.d("********* remove Activity " + activity.getClass().getName());
            activities.remove(activity);
        }
    }

    public void exit() {
        printActivityStackInfo();

        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();

        try {
            ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

            System.exit(0);
        } catch (Exception e) {

        }

//        System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public String printActivityStackInfo() {
        LogUtils.d(" -- bottom --");
        StringBuilder stringBuilder = new StringBuilder();

        for (Activity activity : activities) {
            stringBuilder.append(activity.getClass().getSimpleName());
            stringBuilder.append(activity.getTitle());
        }

        LogUtils.d(" -- top --");
        return stringBuilder.toString();
    }


    /**
     * 退出登录后，清除缓存，回到引导页
     */
    public void logout() {
        clearCache();
        goTop();
    }


    public void goTop() {
        Intent intent;

    }

    public void clearCache() {

    }


    public void gotoMainPage() {


    }

    public void putSession(String key, String value) {
        sessionStorage.put(key, value);
    }

    public String getSession(String key) {
        return sessionStorage.get(key);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        //TODO 应用退出时清除资源
    }

    /**
     * 获取最顶层activity
     *
     * @return
     */
    public Activity getTopActivity() {
        //先判断是否有最新的activity加入进来，如果有直接返回
        if (topActivity != null) {
            if (!topActivity.isFinishing()) {
                return topActivity;
            }
        }

        //如果没有，在遍历所有的activity取最上面的一个
        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity != null && !activity.isFinishing()) {
                return activity;
            }
        }
        return null;
    }

}