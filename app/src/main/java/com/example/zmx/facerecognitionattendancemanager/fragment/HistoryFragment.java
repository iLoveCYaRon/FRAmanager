package com.example.zmx.facerecognitionattendancemanager.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zmx.facerecognitionattendancemanager.model.History;
import com.example.zmx.facerecognitionattendancemanager.adapter.HistoryAdapter;
import com.example.zmx.facerecognitionattendancemanager.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HistoryFragment extends Fragment {

    private List<History> historyList;



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 200:
                    loadingCardView(msg.obj.toString());
//                    Toast.makeText(getActivity(),
//                            msg.obj.toString(), Toast.LENGTH_SHORT).show();
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
        sendRequest();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void sendRequest(){
        // 发送查询请求
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://72156fb1c1fcb432.natapp.cc/history")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();

                Message msg = new Message();
                msg.what = 200;
                msg.obj = data;

                handler.sendMessage(msg);

            }
        });


    }

    private void loadingCardView(String jsonData) {

//        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                History history = new History(jsonObject.getString("register_time"), jsonObject.getString("user_id"));
//                historyList.add(history);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Gson解析
        Gson gson = new Gson();
        historyList = gson.fromJson(jsonData, new TypeToken<List<History>>() {}.getType());
        for (History h : historyList) {
            System.out.println(h.toString());
        }

        //设置布局
        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);
    }


}
