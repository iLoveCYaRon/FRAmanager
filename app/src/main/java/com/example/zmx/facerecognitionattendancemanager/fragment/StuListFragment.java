package com.example.zmx.facerecognitionattendancemanager.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zmx.facerecognitionattendancemanager.R;
import com.example.zmx.facerecognitionattendancemanager.model.Student;
import com.example.zmx.facerecognitionattendancemanager.adapter.StudentAdapter;
import com.example.zmx.facerecognitionattendancemanager.test.TestImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StuListFragment extends Fragment {
    /**
     * 传入上下文
     */
    private Context context;

    private List<Student> stuList = new ArrayList<>();
    public static final String SAVE_IMG_DIR = "register" + File.separator + "imgs";
    public String ROOT_PATH;
    private File imgDir;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_list, container, false);

        if(context!=null) {
            ROOT_PATH = context.getFilesDir().getAbsolutePath();
            imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
        }

        //从文件中读入数据到实体并初始化RecyclerView（先初始化View也可以，View中内容随List更新而更新，但View的更新一般不会影响List）
        initStudents();
        initRecyclerView(view);

        //发送http请求的初尝试，要新开线程，以防与UI冲突
        new Thread(() -> {
            try {
                TestImage.main1(imgDir.getAbsolutePath()+ File.separator  + "111.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return view;
    }

    public StuListFragment(Context context) {
        this.context = context;
    }

    private void initRecyclerView(View view) {
        //获取RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.stu_list_recycler_view);
        //给RecyclerView创建并设置adapter
        recyclerView.setAdapter(new StudentAdapter(stuList));
        //设置布局为纵向
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    }

    private void initStudents() {
        List<File> files = getImageDirFiles();
        for (int i = 0; i < files.size(); i++) {
            String mPicName = files.get(i).getName();
            File file = new File(imgDir + File.separator + mPicName);

            stuList.add(new Student(mPicName.substring(0, mPicName.length()-4), file));
        }
    }

    private List<File> getImageDirFiles() {
        List<File> list = new ArrayList<>();
        File[] allFiles = imgDir.listFiles();
        if (allFiles != null) { // 若文件不为空，则遍历文件长度
            for (File file : allFiles) {
                if (file.isFile()) {
                    list.add(file);
                }
            }
        }
        return list;

    }
}
