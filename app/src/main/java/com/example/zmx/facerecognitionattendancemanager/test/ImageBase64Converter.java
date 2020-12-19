package com.example.zmx.facerecognitionattendancemanager.test;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.util.Base64;

public class ImageBase64Converter {
    /**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param file
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertFileToBase64(File file) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            System.out.println("文件大小（字节）="+in.available());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组进行Base64编码，得到Base64编码的字符串
        String base64Str = Base64.getEncoder().encodeToString(data);

        return base64Str;
    }

    /**
     * 将base64字符串，生成文件
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static File convertBase64ToFile(String fileBase64String, String filePath, String fileName) {

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }

            byte[] bfile = Base64.getDecoder().decode(fileBase64String);

            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}