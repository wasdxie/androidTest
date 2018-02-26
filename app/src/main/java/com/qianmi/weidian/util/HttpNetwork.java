package com.qianmi.weidian.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xiedejun on 2018/2/13.
 */

public class HttpNetwork {

    private static HttpNetwork sInstance;

    private OkHttpClient mOkHttpClient;

    public interface NetworkCallBack{
        void onSuccess(Object obj);
        void onFailure(Object obj);
    }

    // 构造函数
    private HttpNetwork(){
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Content-Encoding", "gzip")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS) //
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        if("/proxy/oauth/login/base64Captcha.jpg".equals(url.encodedPath())){
                            cookieStore.put("/proxy/oauth/login/base64Captcha.jpg",cookies);
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        if("/proxy/common/sendPicMsg".equals(url.encodedPath())){//发送验证码添加cookie
                            List cookies = cookieStore.get("/proxy/oauth/login/base64Captcha.jpg");
                            return cookies != null? cookies: new ArrayList<Cookie>();
                        }
                        return new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    // 获取单例
    public static HttpNetwork getInstance(){
        if (sInstance == null){
            synchronized (HttpNetwork.class){
                sInstance = new HttpNetwork();
            }
        }
        return sInstance;
    }

    // get 请求
    public void getAsyncHttp(String path, final NetworkCallBack callBack){
        this.getAsyncHttp(path,null,callBack);
    }


    // get 请求
    public void getAsyncHttp(String path, HashMap<String,String> paramMap, final NetworkCallBack callBack){
        // 拼接url
        String url = getUrl(path,paramMap);

        Request request = new Request.Builder().url(url).build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                try {
                    callBack.onFailure("error");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String in = response.body().string();
                try {
                    callBack.onSuccess(in);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    // Post 请求
    public void postAsyncHttp(String path, HashMap<String,String> map, final NetworkCallBack callBack){

        // 拼接url
        String url = getUrl(path,null);

        FormBody.Builder builder = new FormBody.Builder();

        //  插入参数
        for (String key : map.keySet()){
            builder.add(key,map.get(key));
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder().url(url).post(body).build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    callBack.onFailure("error");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String in = response.body().string();
                try {
                    callBack.onSuccess(response);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }









    // 整理 URL
    private String getUrl(String path,HashMap<String,String> paramMap){

        // 如果不是全路径就 路径拼接。
        String url = path;
        if(!path.contains("http")){
            path = path.startsWith("/") ? path.substring(1,path.length()) : path;
            url = "http://www.baidu.com/" + path;
        }

        if(paramMap == null){
            return url;
        }else {
            Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();

                url = url + (url.contains("?") ?"&":"?") + key + "=" + value;
            }

            return url;
        }
    }
}
