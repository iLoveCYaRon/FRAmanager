package com.example.zmx.facerecognitionattendancemanager.util;

import com.example.zmx.facerecognitionattendancemanager.common.Constants;
import com.example.zmx.facerecognitionattendancemanager.model.ResponseStuList;
import com.example.zmx.facerecognitionattendancemanager.model.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

public class FaceListController {

    public static List<Student> getStudentList() {
        Request request = new Request.Builder()
                .url(Constants.SERVER_IP + "/studentList")
                .method("GET", null)
                .build();


        try {
            ResponseStuList response = NetworkUtil.gson.fromJson(NetworkUtil.client.newCall(request).execute().body().string(), ResponseStuList.class);
            return Arrays.asList(response.getData());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
