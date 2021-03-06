package com.yf107.teamwork.lostandfound.image;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yf107.teamwork.lostandfound.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImageFromWeb {


    /**
     * https://www.jianshu.com/p/e78407a18716
     * 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext()。
     * @param path
     * @param imageView
     * @param activity
     */
    public static void glideSetImageView(final String path, final ImageView imageView, final Context activity){
        Glide.with(activity)
                .load(path)
                //设置加载时的图
                .placeholder(R.mipmap.diai1)
                //设置失败图
                .error(R.mipmap.diai1)
                .into(imageView);
    }


    public static void httpSetImageView(final String path, final ImageView imageView, final Activity activity){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    int code=httpURLConnection.getResponseCode();
                    if (code==200){
                        int len=-1;
                        InputStream inputStream=httpURLConnection.getInputStream();
                        final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                    else {
                        return;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



}
