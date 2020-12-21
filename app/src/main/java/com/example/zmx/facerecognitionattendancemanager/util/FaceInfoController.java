package com.example.zmx.facerecognitionattendancemanager.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.test.ImageBase64Converter;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FaceInfoController {

    /**
     * 发起注册请求 网络操作 不要在主线程运行
     * @author iLoveCYaRon Blade Xu
     * @time 2020/12/20 16:41
     * @param file 图片文件
     * @param faceId 人物名称
     * @return 注册请求body字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String register(File file, String faceId) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        byte[] bitmapByte = ImageProcess.compressImage(bitmap);
        String imgBase64Str = ImageBase64Converter.convertByteToBase64(bitmapByte);

        //String imgBase64Str = ImageBase64Converter.convertFileToBase64(file);
        //建立请求
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "data:image/jpeg;base64," + imgBase64Str)
                .addFormDataPart("id", faceId)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/register")
                .method("POST", body)
                .build();


        return NetworkUtil.client.newCall(request).execute().body().string();
    }

    /**
     * 发起注册请求 网络操作 不要在主线程运行
     * @author iLoveCYaRon Blade Xu
     * @time 2020/12/20 22:16
     * @param file 图片文件
     * @param faceId 人物名称
     * @return 注册请求body字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String register2(File file, String faceId) throws IOException {

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        bitmap = Bitmap.createScaledBitmap(bitmap, 600,800, false);

        byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
        ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);

        String imgBase64Str = Base64.getEncoder().encodeToString(bgr24);

        //建立请求
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("imageData", imgBase64Str)
                .addFormDataPart("id", faceId)
                .addFormDataPart("width", String.valueOf(bitmap.getWidth()))
                .addFormDataPart("height", String.valueOf(bitmap.getHeight()))
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/register2")
                .method("POST", body)
                .build();

        return NetworkUtil.client.newCall(request).execute().body().string();
    }

    /**
     * 发起签到请求 网络操作 不要在主线程运行
     * @author iLoveCYaRon Blade Xu
     * @time 2020/12/20 16:41
     * @param file 图片文件
     * @param faceId 人物名称
     * @return 注册请求body字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign(File file, String faceId) throws IOException {
        Bitmap initBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        byte[] processedImage = ImageProcess.compressImage(initBitmap);
        String imgBase64Str = ImageBase64Converter.convertByteToBase64(processedImage);


        //建立请求
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "data:image/jpeg;base64," + imgBase64Str)
                .addFormDataPart("id", faceId)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/sign")
                .method("POST", body)
                .build();
        Long beforeSend = System.currentTimeMillis();
        Log.e("BeforeSend" , beforeSend.toString());
        return NetworkUtil.client.newCall(request).execute().body().string();
    }

    /**
     * 发起注册请求 网络操作 不要在主线程运行
     * @author iLoveCYaRon Blade Xu
     * @time 2020/12/20 22:16
     * @param file 图片文件
     * @param faceId 人物名称
     * @return 注册请求body字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign2(File file, String faceId) throws IOException {
        Long time1 = System.currentTimeMillis();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        bitmap = Bitmap.createScaledBitmap(bitmap, 600,800, false);

        byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
        ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
        Long time2 = System.currentTimeMillis() - time1;
        Log.e("toImageData: " , time2.toString() );

        String imgBase64Str = Base64.getEncoder().encodeToString(bgr24);

        //建立请求
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("imageData", imgBase64Str)
                .addFormDataPart("id", faceId)
                .addFormDataPart("width", String.valueOf(bitmap.getWidth()))
                .addFormDataPart("height", String.valueOf(bitmap.getHeight()))
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/sign2")
                .method("POST", body)
                .build();
        Long beforeSend = System.currentTimeMillis();
        Log.e("BeforeSend" , beforeSend.toString());
        return NetworkUtil.client.newCall(request).execute().body().string();
    }
}
