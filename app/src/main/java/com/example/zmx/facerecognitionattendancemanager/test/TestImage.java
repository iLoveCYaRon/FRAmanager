package com.example.zmx.facerecognitionattendancemanager.test;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.common.MyApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
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


        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "data:image/jpeg;base64," + imgBase64Str).build();
        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/detectFaces")
                .method("POST", body)
                .build();

        okhttp3.Callback callback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);

                Toast.makeText(MyApplication.getContext(), "22", Toast.LENGTH_SHORT).show();
            }
        };
        client.newCall(request).enqueue(callback);

    }
}