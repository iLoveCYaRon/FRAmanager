package com.example.zmx.facerecognitionattendancemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zmx.facerecognitionattendancemanager.R;
import com.example.zmx.facerecognitionattendancemanager.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> mStuList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stuName;
        TextView regTime;
        TextView signCount;

        public ViewHolder(View view) {
            super(view);
            stuName = (TextView) view.findViewById(R.id.student_name);
            regTime = (TextView) view.findViewById(R.id.student_register_time);
            signCount = (TextView) view.findViewById(R.id.history_sign_time);
        }
    }

    public StudentAdapter(List<Student> StuList) {
        mStuList = StuList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stu_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = mStuList.get(position);
        //Glide.with(holder.itemView).load(student.getStuImage()).into(holder.stuImage);
        holder.stuName.setText(student.getName());
        holder.regTime.setText(student.getRegisterTime());
        holder.signCount.setText(String.valueOf(student.getCount()));
    }

    @Override
    public int getItemCount() {
        return mStuList.size();
    }


}
