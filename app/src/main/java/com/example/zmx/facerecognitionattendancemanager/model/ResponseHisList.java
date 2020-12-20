package com.example.zmx.facerecognitionattendancemanager.model;

public class ResponseHisList {

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

        public History[] getData() {
            return data;
        }

        public void setData(History[] data) {
            this.data = data;
        }

        private History[] data;

}
