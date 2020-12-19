package com.example.zmx.facerecognitionattendancemanager.test;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestImage {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main1(String arg) throws IOException {
        long start = System.currentTimeMillis();
        File file = new File(arg);
        String imgBase64Str = ImageBase64Converter.convertFileToBase64(file);
        //        System.out.println("本地图片转换Base64:" + imgBase64Str);
        System.out.println("Base64字符串length="+imgBase64Str.length()+"\n");
        System.out.println(imgBase64Str);


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "data:image/jpeg;base64," + imgBase64Str).build();
        Request request = new Request.Builder()
                .url("http://110.64.90.71:8089/detectFaces")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}