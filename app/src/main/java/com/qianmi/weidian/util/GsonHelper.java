package com.qianmi.weidian.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 */
public class GsonHelper {

    private static Gson sInstance;

    public static Gson getInstance() {
        if (null == sInstance) {
            synchronized (GsonHelper.class) {
                if (null == sInstance) {
                    sInstance = new GsonBuilder().serializeNulls()
                            .create();
                }
            }
        }
        return sInstance;
    }
}
