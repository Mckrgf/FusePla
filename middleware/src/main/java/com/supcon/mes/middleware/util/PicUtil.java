package com.supcon.mes.middleware.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by wangshizhan on 2017/8/18.
 * Email:wangshizhan@supcon.com
 */

public class PicUtil {

    /**
     * 获取本地视频的第一帧
     *
     * @param file
     * @return
     */
    public static Bitmap getVideoFirstFrame(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
//        File file = new File(filePath);//实例化File对象，文件路径为/storage/sdcard/Movies/music1.mp4
        if (file.exists()) {
            mmr.setDataSource(file.getAbsolutePath());//设置数据源为该文件对象指定的绝对路径
            Bitmap bitmap = mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
            return bitmap;
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param file
     * @param bitmap
     */
    public static void saveFile(File file, Bitmap bitmap) {
        if (file != null && bitmap != null) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                if (!bitmap.isRecycled() && null != bitmap) {
                    bitmap = null;
                }

                out.flush();
                out.close();
            } catch (FileNotFoundException var3) {

            } catch (IOException var4) {
                var4.printStackTrace();
            }

        }
    }

    public static File writeToDisk(String name, String path, ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdirs();
            }
            File file = new File(path, name);
            if (file.exists()) {
                return file;
//                file.delete();
//                file= new File(path,suffix);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File writeResponseBodyToDisk(String url, String path, ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            String suffix = url.substring(url.lastIndexOf("/") + 1);
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(path, suffix);
            if (file.exists()) {
                return file;
//                file.delete();
//                file= new File(path,suffix);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File savePicToDisk(String name, String path, ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            String suffix = name;
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(path, suffix);
            if (file.exists()) {
                return file;
//                file.delete();
//                file= new File(path,suffix);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static File writeSBPicture(String fileName, String filePath, ResponseBody body) {
        try {
            InputStream is = body.byteStream();
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                // 获取父文件
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();  //创建所有父文件夹
                }
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();
            is.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File rearSBPicture(String fileName, String filePath) {
        String path = filePath + fileName;
        return new File(path);
    }

    public static boolean isPic(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        name = name.toLowerCase();
        return name.contains(".jpg") || name.contains(".png");

    }

    public static boolean isVideo(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        name = name.toLowerCase();
        return name.contains(".mp4") || name.contains(".adv");

    }

    public static Bitmap createBitmap(ResponseBody body) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = body.bytes();
             bitmap= BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
             return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }
}
