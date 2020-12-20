package com.example.zmx.facerecognitionattendancemanager.model;

import java.io.File;

public class Student {

    private String faceId;

    public Student(String faceId, String name, String faceFeature) {
        this.faceId = faceId;
        this.name = name;
        this.faceFeature = faceFeature;
    }

    private String name;
    private String faceFeature;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(String faceFeature) {
        this.faceFeature = faceFeature;
    }
}
