package com.example.zmx.facerecognitionattendancemanager.fragment;

import android.annotation.SuppressLint;
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

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zmx.facerecognitionattendancemanager.R;
import com.example.zmx.facerecognitionattendancemanager.model.Student;
import com.example.zmx.facerecognitionattendancemanager.adapter.StudentAdapter;
import com.example.zmx.facerecognitionattendancemanager.util.FaceListController;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class StuListFragment extends Fragment {
    /**
     * 传入上下文
     */
    private Context context;

    private List<Student> stuList = new ArrayList<>();
    public static final String SAVE_IMG_DIR = "register" + File.separator + "imgs";
    public String ROOT_PATH;
    private File imgDir;

    private OkHttpClient client;
    private Gson gson;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_list, container, false);

        if(context!=null) {
            ROOT_PATH = context.getFilesDir().getAbsolutePath();
            imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
        }

        client = new OkHttpClient();
        gson = new Gson();
        //从云端读取学生列表
        getStudentList();

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

    private void getStudentList() {
        new Thread(() -> {
            stuList = FaceListController.getStudentList();
            if(!stuList.isEmpty()) {
                Message msg = new Message(); msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //消息处理者,创建一个Handler的子类对象,目的是重写Handler的处理消息的方法(handleMessage())
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //从服务端接收到学生列表信息
                case 1:
                    initRecyclerView(getView());
                    break;
            }
        }
    };

}
