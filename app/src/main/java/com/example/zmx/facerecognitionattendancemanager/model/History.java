package com.example.zmx.facerecognitionattendancemanager.model;

public class History {

    private String faceId;
    private String time;

    public History(String faceId, String time) {
        this.faceId = faceId;
        this.time = time;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "History{" +
                "faceId='" + faceId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
