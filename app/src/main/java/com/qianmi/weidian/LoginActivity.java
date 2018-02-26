package com.qianmi.weidian;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qianmi.weidian.domain.BaseResponse;
import com.qianmi.weidian.util.BizNetworkHelp;
import com.qianmi.weidian.util.GsonHelper;
import com.qianmi.weidian.util.HttpNetwork;
import com.qianmi.weidian.util.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;

import static com.qianmi.weidian.util.BizNetworkHelp.getInstance;


public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private EditText phone;//电话

    private EditText code;//图片验证码

    private EditText phoneCode; //短信验证码

    private Button sendCode; //发送验证码

    private Button login;

    private ImageView codeImage;

    private Handler handler;

    private Bitmap codeBitmap;

    private int time = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        codeImage = findViewById(R.id.codeImage);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        phoneCode = findViewById(R.id.phoneCode);
        sendCode = findViewById(R.id.sendCode);
        login = findViewById(R.id.login);
        //加载验证码

        //https://bmweidiantest.bm001.com/proxy/oauth/login/base64Captcha.jpg
        getCodeImage();

        codeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodeImage();
            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneS = phone.getText().toString();
                String codeS = code.getText().toString();
                HashMap<String,String> params = new HashMap();
                params.put("shopCode","DP00004");
                params.put("phone",phoneS);
                params.put("behaviour","3");
                params.put("verifyCode",codeS);
                sendCode(params);
                final Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time--;
                                sendCode.setText(String.valueOf(time));
                                sendCode.setEnabled(false);
                                if (time <= 0) {
                                    time = 10;
                                    timer.cancel();
                                    sendCode.setEnabled(true);
                                    sendCode.setText("发送验证码");
                                }
                            }
                        });
                    }
                };
                timer.schedule(task, time, 1000);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneS = phone.getText().toString();
                String phoneCodeS = phoneCode.getText().toString();
                HashMap<String,Object> params = new HashMap();
                params.put("shopCode","DP00004");
                params.put("phone",phoneS);
                params.put("verifychkcode",phoneCodeS);
                params.put("openId","");
                login(params);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if(message.what == 999){
                    if(codeBitmap != null){
                        codeImage.setImageBitmap(codeBitmap);
                    }
                }
            }
        };
    }




    private void getCodeImage(){
        HttpNetwork.getInstance().getAsyncHttp("https://bmweidiantest.bm001.com/proxy/oauth/login/base64Captcha.jpg", new HttpNetwork.NetworkCallBack() {
            @Override
            public void onSuccess(Object obj) {
                    String result = obj.toString();
                    String base64Data = result.replaceAll("data:image/png;base64,","");
                    Bitmap bitmap = convertBase64ToBitmap(base64Data);
                    codeBitmap = bitmap;
                    Message msg = new Message();
                    msg.what = 999;
                    handler.dispatchMessage(msg);
                    Log.e(TAG,"返回结果:"+base64Data);

            }

            @Override
            public void onFailure(Object obj) {

            }
        });
    }

    private void sendCode(HashMap<String,String> params){
        HttpNetwork.getInstance().getAsyncHttp("https://bmweidiantest.bm001.com/proxy/common/sendPicMsg", params, new HttpNetwork.NetworkCallBack() {
            @Override
            public void onSuccess(Object obj) {
                String result = obj.toString();
               final BaseResponse baseResponse = GsonHelper.getInstance().fromJson(result,BaseResponse.class);
                if(baseResponse.getRescode() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"发送成功",Toast.LENGTH_SHORT).show();

                        }
                    });

                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,baseResponse.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Object obj) {
                Toast.makeText(LoginActivity.this,"网络故障,请检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void login(HashMap<String,Object> params){
        BizNetworkHelp.getInstance().postAsyncHttpJson("https://gatewaytest.bm001.com/jwt/oauth/phoneLoginCuser", params, new BizNetworkHelp.NetworkCallBack() {
            @Override
            public void onSuccess(Object obj) {
                String result = obj.toString();
                final BaseResponse baseResponse = GsonHelper.getInstance().fromJson(result,BaseResponse.class);
                if(baseResponse.getRescode() == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT);
                            String data =(String) baseResponse.getData();
                            SPUtils.put(CustomApplication.getInstance(),"loginInfo",data);
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,baseResponse.getMsg(),Toast.LENGTH_SHORT);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Object obj) {
                Toast.makeText(LoginActivity.this,"网络故障,请检查网络",Toast.LENGTH_SHORT);
            }
        });
    }

    private Bitmap convertBase64ToBitmap(String base64Data){
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public  void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }



}
