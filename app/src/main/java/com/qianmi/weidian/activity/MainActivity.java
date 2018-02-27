package com.qianmi.weidian.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qianmi.weidian.R;
import com.qianmi.weidian.adapter.GridViewAdapter;
import com.qianmi.weidian.bean.GridItem;
import com.qianmi.weidian.domain.BaseResponse;
import com.qianmi.weidian.domain.ProductBo;
import com.qianmi.weidian.domain.ProductList;
import com.qianmi.weidian.util.BizNetworkHelp;
import com.qianmi.weidian.util.GsonHelper;
import com.qianmi.weidian.util.HttpNetwork;
import com.qianmi.weidian.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.qianmi.weidian.base.Constant.INTERFACE_URL;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    private ViewPager imagePager;//图片滑块

    private ViewPager productPager;//产品滑块

    private List<View> list_view = new ArrayList<View>();

    private List<View> product_list_view = new ArrayList<View>();

    private MyPageAdapter imagePageAdapter;

    // 带颜色的引导点
    ImageView img_colorPoint;

    LinearLayout layout_point;

    LinearLayout product_point;

    RelativeLayout reProduct;

    private List<ProductBo> productBos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagePager = findViewById(R.id.imagePager);
        layout_point = findViewById(R.id.layout_point);
        productPager = findViewById(R.id.productPager);
        product_point = findViewById(R.id.product_point);
        reProduct = findViewById(R.id.reProduct);

        initImageViewPager();
        initProductViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height = reProduct.getHeight();
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int density = (int)dm.density;
        int width = dm.widthPixels;
        int height1 = dm.heightPixels;
        LinearLayout.LayoutParams layoutParams11 = new LinearLayout.LayoutParams(width1-20,(int)density*150);
        layoutParams11.setMargins(0,density*(-10),0,0);
        reProduct.setLayoutParams(layoutParams11);
        reProduct.invalidate();
    }

    /**
     * 初始化图片轮播
     * */
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
        initPoint(layout_point,list_view.size());

        imagePageAdapter = new MyPageAdapter(list_view);
        imagePager.setAdapter(imagePageAdapter);
        imagePager.addOnPageChangeListener(new ImageSimpleOnPageChangeListener(layout_point));
    }


    /**
     * 初始化产品轮播
     * */
    private void initProductViewPager(){
        HashMap<String,Object> params = new HashMap<>();
        params.put("duserCode","DP00004");
        BizNetworkHelp.getInstance().postAsyncHttpJson(INTERFACE_URL+"/microstore/product/queryProductList",params, new BizNetworkHelp.NetworkCallBack() {
            @Override
            public void onSuccess(Object obj) {
                String result = obj.toString();
                JSONObject jsonObject;
               final BaseResponse<ProductList> baseResponse = GsonHelper.getInstance().fromJson(result,BaseResponse.class);
                String data = GsonHelper.getInstance().toJson(baseResponse.getData());
                 ProductList productList = GsonHelper.getInstance().fromJson(data,ProductList.class);
                productBos = productList.getProductBos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initProuctPager();
                    }
                });

                LogUtils.d(TAG,result);
            }

            @Override
            public void onFailure(Object obj) {

            }
        });
    }
    
    private void initProuctPager(){
        int everyCount = 8; //每页的产品数量
        int count = 0;
        if(productBos != null){
            int size = productBos.size();
            int mod = size%8;
            count = mod == 0?size/8:((size - mod)/8 +1);
            for(int i =0;i<count;i++){
                LayoutInflater inflater = LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.product_page,null);
                GridView gridView = view.findViewById(R.id.gridview);
                ArrayList<GridItem> arrayList = new ArrayList<>();
                //初始化数据源
                for(int j=0;j<8;j++){
                    int current = j+8*i;
                    GridItem girdItem = new GridItem();
                    if(current < size){
                        ProductBo productBo = productBos.get(j+8*i);
                        girdItem.setImage(productBo.getIcon());
                        girdItem.setTitle(productBo.getName());
                        girdItem.setProductBo(productBo);
                    }
                    arrayList.add(girdItem);
                }
                GridViewAdapter gridViewAdapter = new GridViewAdapter(this,R.layout.product_page_item,arrayList);
                gridView.setAdapter(gridViewAdapter);
                product_list_view.add(view);
            }
            productPager.setAdapter(new MyPageAdapter(product_list_view));
            //添加引导点
            initPoint(product_point,count);
            productPager.addOnPageChangeListener(new ImageSimpleOnPageChangeListener(product_point));

        }
    }

    public class MyPageAdapter extends PagerAdapter {

        private List<View> views;

        public MyPageAdapter(List<View> list) {
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

        LinearLayout container;

        public ImageSimpleOnPageChangeListener(LinearLayout container){
            this.container = container;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // This space for rent
           int count =  3;
            for(int i=0;i<count;i++){
               final ImageView imageView = (ImageView) container.getChildAt(i);
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


    /**
     * 添加引导点
     * */
    private void initPoint(LinearLayout container,int childLength){
        for (int i = 0; i < childLength; i++) {
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10,10);
            layoutParams.setMargins(10,0,10,0);
            point.setLayoutParams(layoutParams);

            //设置暗点
            point.setBackgroundResource(R.color.gray_1);

            container.addView(point);
        }
    }

}
