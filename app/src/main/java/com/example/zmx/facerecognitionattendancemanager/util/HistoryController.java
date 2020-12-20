package com.example.zmx.facerecognitionattendancemanager.util;

import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.model.History;
import com.example.zmx.facerecognitionattendancemanager.model.ResponseHisList;
import com.example.zmx.facerecognitionattendancemanager.model.ResponseStuList;
import com.example.zmx.facerecognitionattendancemanager.model.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryController {

    public static List<History> getHistoryList(String faceId) {

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id", faceId)
                .build();
        Request request = new Request.Builder()
                .url(Constants.SERVER_IP+"/historyList")
                .method("POST", body)
                .build();

        try {
            Response response = NetworkUtil.client.newCall(request).execute();
            ResponseHisList re = NetworkUtil.gson.fromJson(response.body().string(), ResponseHisList.class);
            return Arrays.asList(re.getData());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
