package com.example.zmx.facerecognitionattendancemanager.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.zmx.facerecognitionattendancemanager.R;
import com.example.zmx.facerecognitionattendancemanager.model.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;

    private List<History> mHistoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView historyName;
        TextView historyTime;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            historyName = (TextView) view.findViewById(R.id.history_name);
            historyTime = (TextView) view.findViewById(R.id.history_time);
        }
    }

    public HistoryAdapter(List<History> historyList) {
        mHistoryList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = mHistoryList.get(position);
        holder.historyName.setText(history.getFaceId());
        holder.historyTime.setText(history.getTime());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
}
