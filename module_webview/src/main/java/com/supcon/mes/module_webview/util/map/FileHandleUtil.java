package com.supcon.mes.module_webview.util.map;

import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.middleware.util.ZipUtils;

import java.io.File;

/**
 * Time:    2020-05-30  14: 31
 * Author： nina
 * Des:
 */
public class FileHandleUtil {

   public static String filePath = MBapApp.getAppContext().getExternalFilesDir(null) + "/cache/mapcache/" ;

    public static String getFilePathAndName(String fileName) {
        return filePath + fileName;
    }


    public static boolean unZipFile(String zipFilePath, String destFilePath, String packetName) {
        File destFile = new File(destFilePath);

        File desFilewithPacket = new File(getFilePathAndName(packetName));
        if (desFilewithPacket.exists()) {
            FileUtil.deleteFile(desFilewithPacket);
        }

        try {
            ZipUtils.unzipFileByKeyword(new File(zipFilePath), destFile, "");

            FileUtil.deleteFile(new File(zipFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
