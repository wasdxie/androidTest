package com.qianmi.weidian.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qianmi.weidian.R;
import com.qianmi.weidian.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    private ViewPager imagePager;//图片滑块

    private ViewPager productPager;//产品滑块

    private List<View> list_view = new ArrayList<View>();

    private ImagePageAdapter imagePageAdapter;

    // 带颜色的引导点
    ImageView img_colorPoint;

    List<ImageView> list_pointView = new ArrayList<ImageView>();

    LinearLayout layout_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagePager = findViewById(R.id.imagePager);
        layout_point = findViewById(R.id.layout_point);
        initImageViewPager();
    }


    private void initImageViewPager(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.main_image,null);
        View view2 = inflater.inflate(R.layout.main_image,null);
        View view3 = inflater.inflate(R.layout.main_image,null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        ImageView imageView2 = view2.findViewById(R.id.image);
        ImageView imageView3 = view3.findViewById(R.id.image);
        beginDownImage("http://pic.qianmi.com/ejz/ejzc_app2.0/img/banner/banner1.jpg",imageView1);
        beginDownImage("http://pic.qianmi.com/ejz/ejzc_app2.0/img/banner/banner2.jpg",imageView2);
        beginDownImage("http://pic.qianmi.com/ejz/ejzc_app2.0/img/banner/banner3.jpg",imageView3);
        list_view.add(view1);
        list_view.add(view2);
        list_view.add(view3);

        //添加引导点
        for (int i = 0; i < list_view.size(); i++) {
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10,10);
            layoutParams.setMargins(10,0,10,0);
            point.setLayoutParams(layoutParams);

            //设置暗点
            point.setBackgroundResource(R.color.gray_1);

            list_pointView.add(point);
            layout_point.addView(point);
        }

        imagePageAdapter = new ImagePageAdapter(list_view);
        imagePager.setAdapter(imagePageAdapter);
        imagePager.addOnPageChangeListener(new ImageSimpleOnPageChangeListener());
    }
    public class ImagePageAdapter extends PagerAdapter {

        private List<View> views;

        public ImagePageAdapter(List<View> list) {
            super();
            this.views = list;
        }

        // 从当前container中删除指定位置（position）的View
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        // 第一：将当前视图添加到container中，第二：返回当前View
        @Override
        public Object instantiateItem(View container, int position) {
            // 将当前视图添加到container中
            ((ViewPager) container).addView(views.get(position));
            // 设置当前视图的唯一标示Key
            return views.get(position);
        }

        // getCount():返回要滑动的View的个数
        @Override
        public int getCount() {
            return views.size();
        }

        // 通过标识arg1找到view
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }


    private void beginDownImage(final String url,final ImageView imageView){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getImageBitmap(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    class ImageSimpleOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // This space for rent
           int count =  3;
            for(int i=0;i<count;i++){
               final ImageView imageView = (ImageView) layout_point.getChildAt(i);
               int color = R.color.colorAccent;
                if(i == position){
                    color = R.color.colorPrimary;
                }else {
                    color = R.color.gray_1;
                }
                changeColor(color,imageView);
            }
        }

        @Override
        public void onPageSelected(int position) {
            // This space for rent
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // This space for rent
        }

        private void changeColor(final int color,final ImageView imageView){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setBackgroundResource(color);
                }
            });
        }
    }

}
