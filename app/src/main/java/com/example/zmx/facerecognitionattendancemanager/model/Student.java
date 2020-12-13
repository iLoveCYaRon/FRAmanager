package com.example.zmx.facerecognitionattendancemanager.model;

import java.io.File;

public class Student {

    private String stuName;

    private File stuImage;

    public Student(String name, File image) {
        this.stuName = name;
        this.stuImage = image;
    }

    public String getStuName() { return stuName; }

    public File getStuImage() { return stuImage; }

    public void setStuName(String stuName) { this.stuName = stuName; }

    public void setStuImage(File stuImage) { this.stuImage = stuImage; }
}
