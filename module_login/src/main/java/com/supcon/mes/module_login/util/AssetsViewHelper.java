package com.supcon.mes.module_login.util;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yangkai
 * @email yangkai2@supcon.com
 */
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuge on 2017/11/8.
 */

public class AssetsViewHelper {
    private static Context mcontext;
    private static AssetsViewHelper assetsViewHelper;
    /**
     * assets 目录前缀
     */
    private static String assetsFile="assets/";
    private AssetsViewHelper(){
    }


    public static AssetsViewHelper with(Context context){
        mcontext=context.getApplicationContext();
        if(assetsViewHelper==null){
            synchronized (AssetsViewHelper.class){
                if(assetsViewHelper==null){
                    assetsViewHelper=new AssetsViewHelper();
                }
            }
        }
        return assetsViewHelper;
    }


    /**
     * 获取layout方法
     * @param filename
     * @return
     */
    public  View  getAssetsLayout(String filename) {
        AssetManager am = mcontext.getResources().getAssets();
        try {
            XmlResourceParser parser = am.openXmlResourceParser(assetsFile + filename);
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            View inflate = inflater.inflate(parser, null);
            return inflate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据 tag 获取 view 对象
     * @param viewGroup 父容器也就是activity的根布局
     * @param tag
     * @return
     */
    public  View getViewByTag(View viewGroup,Object tag){
        return viewGroup.findViewWithTag(tag);
    }

    /**
     * 获取assets 中图片的方法
     * @param fileName
     * @return
     */
    Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = mcontext.getResources().getAssets();
        try
        {
            InputStream is = am.open(assetsFile+fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }
}