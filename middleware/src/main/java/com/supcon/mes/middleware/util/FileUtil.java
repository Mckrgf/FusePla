package com.supcon.mes.middleware.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by wangshizhan on 2018/3/29.
 * Email:wangshizhan@supcon.com
 */

public class FileUtil {
    public static String getFileTypeCH(String name) {
        if (name.contains(".jpg") || name.contains(".png") || name.contains(".PNG")) {
            return "图片";
        } else if (name.contains(".txt") || name.contains(".doc")) {
            return "文本";
        } else if (name.contains(".mp4") || name.contains(".adv")) {
            return "视频";
        } else if (name.contains(".mp3") || name.contains(".wav")) {
            return "音乐";

        }
        return "文件";
    }

    public static void deleteFile(String filePath) {
        File uploadZip = new File(filePath);
        if (uploadZip.exists()) {
            uploadZip.delete();
        }
    }

    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }


    public static void save(String file, String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput(file);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static FileOutputStream openFileOutput(String file) {

        try {
            return new FileOutputStream(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 写入内容到SD卡中的txt文本中
     * str为内容
     */
    public static void write2File(String file, String input) {
        try {
//            FileWriter fw = new FileWriter(file);
//            File f = new File(file);
//            fw.write(input);
//            FileOutputStream os = new FileOutputStream(f);
//            DataOutputStream out = new DataOutputStream(os);
//            out.writeShort(2);
//            out.writeUTF("");
            Writer fw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file), "UTF-8"));
            fw.write(input);
            fw.flush();
            fw.close();
        } catch (Exception e) {

        }
    }

    public static void readFile() {

    }

    /**
     * 新建文件夹到手机本地
     *
     * @param fileFolder ,文件夹的路径名称
     * @return
     */
    public static boolean createDir(String fileFolder) {
        File dir = new File(fileFolder);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return false;
    }

    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }

        }
        return filename;
    }

    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
