package com.example.zmx.facerecognitionattendancemanager.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

public class ImageProcess {

    public static Bitmap fileToBitmap(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }

    public static byte[] bitmapToByte(Bitmap bitmap){
        int bytes = bitmap.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buf);

        byte[] byteArray = buf.array();

        return byteArray;
    }

    public static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        int length = baos.toByteArray().length / 1024;

        if (length>5000){
            //重置baos即清空baos
            baos.reset();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        }else if (length>4000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        }else if (length>3000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }else if (length>2000){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        }
        //循环判断如果压缩后图片是否大于1M,大于继续压缩
//        while (baos.toByteArray().length / 1024>1024) {
//            //重置baos即清空baos
//            baos.reset();
//            //这里压缩options%，把压缩后的数据存放到baos中
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
//            //每次都减少10
//            options -= 10;
//        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        return baos.toByteArray();
    }

}
