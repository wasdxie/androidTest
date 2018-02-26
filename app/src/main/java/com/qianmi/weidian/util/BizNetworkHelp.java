package com.qianmi.weidian.util;


import com.qianmi.weidian.CustomApplication;
import com.qianmi.weidian.base.AppConfig;
import com.qianmi.weidian.base.Constant;
import com.qianmi.weidian.domain.BaseResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dingle on 2017/10/11.
 */

public class BizNetworkHelp {

    private static BizNetworkHelp sInstance;

    private OkHttpClient mOkHttpClient;

    public static final MediaType JSONTYPE = MediaType.parse("application/json;charset=utf-8");

    public interface NetworkCallBack {
        void onSuccess(Object obj);

        void onFailure(Object obj);
    }

    // 构造函数
    private BizNetworkHelp() {
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
                .build();
    }

    // 获取单例
    public static BizNetworkHelp getInstance() {
        if (sInstance == null) {
            synchronized (BizNetworkHelp.class) {
                sInstance = new BizNetworkHelp();
            }
        }
        return sInstance;
    }

    // get 请求
    public void getAsyncHttp(String path, final NetworkCallBack callBack) {
        this.getAsyncHttp(path, null, callBack);
    }


    // get 请求
    public void getAsyncHttp(String path, HashMap<String, String> paramMap, final NetworkCallBack callBack) {
        // 拼接url
        String url = getUrl(path, paramMap);

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


    // get 请求，带token
    public void getAsyncHttpToken(String path, HashMap<String, String> paramMap, final NetworkCallBack callBack) {
        // 拼接url
        String url = getUrl(path, paramMap);

        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", AppConfig.getToken())
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                dealNetFail(callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dealNetResponse(callBack,response);
            }
        });
    }


    // Post 请求
    public void postAsyncHttp(String path, HashMap<String, String> map, final HttpNetwork.NetworkCallBack callBack) {

        // 拼接url
        String url = getUrl(path, null);

        FormBody.Builder builder = new FormBody.Builder();

        //  插入参数
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
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
                    callBack.onSuccess(in);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    // Post 请求
    public void postAsyncHttpJson(String path, HashMap<String, Object> map, final NetworkCallBack callBack) {

        // 拼接url
        String url = getUrl(path, null);

        String postJson = GsonHelper.getInstance().toJson(map);
        RequestBody body = RequestBody.create(JSONTYPE, postJson);

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
                    callBack.onSuccess(in);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    // Post 请求，带token
    public void postAsyncHttpJsonToken(String path, HashMap<String, Object> map, final NetworkCallBack callBack) {

        // 拼接url
        String url = getUrl(path, null);

        String postJson = GsonHelper.getInstance().toJson(map);
        RequestBody body = RequestBody.create(JSONTYPE, postJson);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",AppConfig.getToken())
                .post(body).build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                dealNetFail(callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dealNetResponse(callBack,response);
            }
        });
    }

    //  文件上传
    public void uploadAsyncHttp(String path, String fieldName, String fileurl, MediaType type, final NetworkCallBack callBack) {

        // 拼接url
        String url = getUrl(path, null);

        File file = new File(fileurl);
        RequestBody body = RequestBody.create(type, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart(fieldName, fileurl, body)
                .setType(MultipartBody.FORM)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", AppConfig.getToken())
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                dealNetFail(callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dealNetResponse(callBack,response);
            }
        });
    }

    // 文件下载
    public void downloadAsyncHttp(String path, final NetworkCallBack callBack) {

        this.downloadAsyncHttp(path, null, callBack);
    }

    // 文件下载
    public void downloadAsyncHttp(String path, HashMap<String, String> paramMap, final NetworkCallBack callBack) {

        // 拼接url
        String url = getUrl(path, paramMap);


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
                InputStream in = response.body().byteStream();
                try {
                    callBack.onSuccess(in);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    //  文件上传 带参数
    public void uploadAsyncHttp(String path, String fileurl, HashMap<String, String> map, MediaType type, final NetworkCallBack callBack) {
        // 拼接url
        String url = getUrl(path, null);

        File file = new File(fileurl);
        RequestBody body = null;
        if (map == null) {
            body = RequestBody.create(type, file);
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            //  插入参数
            for (String key : map.keySet()) {
                builder.addFormDataPart(key, map.get(key));
            }

            body = builder.addPart(RequestBody.create(type, file)).build();
//            body = builder.addFormDataPart("","",RequestBody.create(type,file)).build();
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", AppConfig.getToken())
                .post(body).build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                dealNetFail(callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                dealNetResponse(callBack,response);
            }
        });

    }

    // 整理 URL
    private String getUrl(String path, HashMap<String, String> paramMap) {

        // 如果不是全路径就 路径拼接。
        String url = path;
        if (!path.contains("http")) {
            path = path.startsWith("/") ? path.substring(1, path.length()) : path;
            url = AppConfig.ADMIN_URL + path;
        }

        if (paramMap == null) {
            return url;
        } else {
            Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();

                url = url + (url.contains("?") ? "&" : "?") + key + "=" + value;
            }

            return url;
        }
    }

    /**
     * 登录后的正常结果处理
     * @param callBack
     * @param response
     */
    private void dealNetResponse(NetworkCallBack callBack,Response response) {

        try {
            String in = response.body().string();

            BaseResponse res = GsonHelper.getInstance().fromJson(in, BaseResponse.class);

            //如果是未登录则跳转到登录页
            if (Constant.ResponseStatus.FAIL.equals(res.getResult())) {
                if (res.getRescode() == 202){
                    CustomApplication.getInstance().logout();
                }

            } else {
                callBack.onSuccess(in);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 异常结果处理
     * @param callBack
     */
    private void dealNetFail(NetworkCallBack callBack) {

        try {
            callBack.onFailure("error");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
