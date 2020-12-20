package com.example.zmx.facerecognitionattendancemanager.util;

import android.os.Build;
import android.os.Message;

import androidx.annotation.RequiresApi;

import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.test.ImageBase64Converter;

import java.io.File;
import java.io.IOException;

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

        String imgBase64Str = ImageBase64Converter.convertFileToBase64(file);

        //建立请求
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "data:image/jpeg;base64," + imgBase64Str)
                .addFormDataPart("name", faceId)
                .build();

        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/register")
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

        String imgBase64Str = ImageBase64Converter.convertFileToBase64(file);
        //String imgBase64Str = Constants.IMG_STR;

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

        return NetworkUtil.client.newCall(request).execute().body().string();
    }
}
