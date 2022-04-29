package com.twoteatotwo.lesson02.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.twoteatotwo.lesson02.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Context context;
    private ArrayList<String> stringArrayList;
    public Drawable drawable;

    public MainAdapter(Context context, Drawable dr) {
        this.context = context;
        this.drawable = dr;
        stringArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false); //рисуем разметку на экране
        return new MainViewHolder(view); //передача элемента
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.setData(stringArrayList.get(position),drawable);
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ConstraintLayout constraintLayout;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.ConstraintLayoutList);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void setData(String text, Drawable drawable) {
            if(text.contains("*")){
                constraintLayout.setBackground(drawable);
                constraintLayout.setMaxHeight(40);
            } else tvTitle.setText(text);
        }
    }
        public void updateAdapter(List<String> newList) {

            stringArrayList.clear();
            stringArrayList.addAll(newList);
            notifyDataSetChanged(); //update recycler view

        }

}
