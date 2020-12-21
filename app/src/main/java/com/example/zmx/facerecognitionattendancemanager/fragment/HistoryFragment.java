package com.example.zmx.facerecognitionattendancemanager.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zmx.facerecognitionattendancemanager.adapter.StudentAdapter;
import com.example.zmx.facerecognitionattendancemanager.model.History;
import com.example.zmx.facerecognitionattendancemanager.adapter.HistoryAdapter;
import com.example.zmx.facerecognitionattendancemanager.R;
import com.example.zmx.facerecognitionattendancemanager.util.FaceListController;
import com.example.zmx.facerecognitionattendancemanager.util.HistoryController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private Context context;

    private List<History> historyList = new ArrayList<>();


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    loadingCardView();
                    break;
                default:
                    Toast.makeText(getActivity(),
                            "未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        getHistoryList();

        //获取RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        //给RecyclerView创建并设置adapter
        recyclerView.setAdapter(new HistoryAdapter(historyList));
        //设置布局为纵向
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));


        return view;
    }


    public HistoryFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getHistoryList() {
        new Thread(() -> {
            historyList = HistoryController.getHistoryList("admin");
            if(!historyList.isEmpty()) {
                Message msg = new Message(); msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void loadingCardView() {
        //设置布局
        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
    }


}
