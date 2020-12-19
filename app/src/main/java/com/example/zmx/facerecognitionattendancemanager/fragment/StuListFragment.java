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
        ROOT_PATH = context.getFilesDir().getAbsolutePath();
        imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
        initRecyclerView(view);
        initStudents();

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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stu_list_recycler_view);
        //创建adapter
        StudentAdapter adapter = new StudentAdapter(stuList);
        //给RecyclerView设置adapter
        recyclerView.setAdapter(adapter);
        //设置layoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

    }

    private void initStudents() {

        ArrayList<File> list = new ArrayList<>();
        getFiles(list);
        for (int i = 0; i < list.size(); i++) {
            String mPicName = list.get(i).getName();

            File file = new File(imgDir + File.separator + mPicName);

            stuList.add(new Student(mPicName.substring(0, mPicName.length()-4), file));
        }
    }

    private void getFiles(ArrayList<File> list) {
        File[] allFiles = imgDir.listFiles();
        if (allFiles != null) { // 若文件不为空，则遍历文件长度
            for (File file : allFiles) {
                if (file.isFile()) {
                    list.add(file);
                }
            }
        }

    }
}
