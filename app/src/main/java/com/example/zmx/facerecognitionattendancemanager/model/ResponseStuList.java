package com.example.zmx.facerecognitionattendancemanager.model;

public class ResponseStuList {
    private int code = -1;
    private String msg = "success";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Student[] getData() {
        return data;
    }

    public void setData(Student[] data) {
        this.data = data;
    }

    private Student[] data;
}
