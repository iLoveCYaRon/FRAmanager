package com.example.zmx.facerecognitionattendancemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.zmx.facerecognitionattendancemanager.faceserver.FaceServer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    //定义文件存放的目录
    private static final String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "arcface";
    private static final String REGISTER_DIR = ROOT_DIR + File.separator + "register";
    private static final String REGISTER_FAILED_DIR = ROOT_DIR + File.separator + "failed";

    //界面控件相关
    private ProgressDialog waitingDialog;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.photo)
    public ImageView photo;

    //业务逻辑相关
    //用于判断是register（注册人脸）还是transmit（校验人脸）
    private int request_flag;
    final int TRANSMIT = 0;
    final int REGISTER = 1;
    public static final int TAKE_PHOTO = 1;
    //用于拍照

    private Uri imageUri;
    private File outputImage;
    //处理图像的事务处理器
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ButterKnife.bind(this);

        //设置等待dialog外观参数
        waitingDialog = new ProgressDialog(TakePhotoActivity.this);
        waitingDialog.setMessage("上传中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);

        //从Intent中获取request_flag存到成员变量中
        Intent intent_flag = getIntent();
        request_flag = intent_flag.getIntExtra("request_flag", -1);

        //初始化事务处理器
        executorService = Executors.newSingleThreadExecutor();

        //初始化外部存储位置用于存储拍摄照片
        outputImage = new File(getExternalCacheDir(), "face_check_take_photo_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //不同安卓版本不同
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                    "com.example.cameraalbumtest.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //使用Intent启动相机程序，在Intent中指定好捕获到的图像的储存位置
        Intent intent_photo = new Intent("android.media.action.IMAGE_CAPTURE");
        intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent_photo, TAKE_PHOTO);
    }

    //重载onActivityResult方法，获得拍照的结果并显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                switch (resultCode) {
                    case RESULT_OK:             //拍照成功后加载图片，注意图片的方向
                        Glide.with(this)
                                .load(outputImage)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(photo);
                        break;
                    case RESULT_CANCELED:       //取消拍照后结束此Activity
                        TakePhotoActivity.this.finish();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        waitingDialog.show();                //上传中...等待dialog
        switch (request_flag) {
            case TRANSMIT:
                //TODO: 加入人脸检测逻辑
                break;
            case REGISTER:
                //判定是否捕捉到了图片，创建新的人脸数据
                if (photo.getDrawable()!=null) {
                    //新建一个Dialog输入学生姓名（user_id）
                    final EditText editText = new EditText(TakePhotoActivity.this);
                    AlertDialog.Builder inputDialog =
                            new AlertDialog.Builder(TakePhotoActivity.this);
                    inputDialog.setTitle("在此输入学生姓名").setView(editText);
                    inputDialog.setPositiveButton("提交",
                            (dialog, which) -> doRegister(editText.getText().toString()));
                    inputDialog.show();
                }

        }
    }


    private void doRegister(String username) {
        //获取方才捕捉到的图片
        outputImage = new File(getExternalCacheDir(), "face_check_take_photo_image.jpg");

        executorService.execute(() -> {
            //将图像转码为Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getAbsolutePath());
            if (bitmap == null) {
                File failedFile = new File(REGISTER_FAILED_DIR + File.separator + outputImage.getName());
                if (!failedFile.getParentFile().exists()) {
                    failedFile.getParentFile().mkdirs();
                }
                outputImage.renameTo(failedFile);
            }
            bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
            if (bitmap == null) {
                File failedFile = new File(REGISTER_FAILED_DIR + File.separator + outputImage.getName());
                if (!failedFile.getParentFile().exists()) {
                    failedFile.getParentFile().mkdirs();
                }
                outputImage.renameTo(failedFile);

            }
            //从Bitmap转化为bgr24数据
            byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
            int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
            if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.dismiss();
                    }
                });
                return;
            }
            
            //发起注册请求并获取结果，该图片注册成功后会被保存，下次初始化引擎自动读取文件列表到已识别人脸中
            boolean success = FaceServer.getInstance().registerBgr24(TakePhotoActivity.this, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                    username);
            if (!success) {
                File failedFile = new File(REGISTER_FAILED_DIR + File.separator + outputImage.getName());
                if (!failedFile.getParentFile().exists()) {
                    failedFile.getParentFile().mkdirs();
                }
                outputImage.renameTo(failedFile);
            } else {
                Log.d("Register", "run: 注册成功");
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show());
            }
        });
    }

}
