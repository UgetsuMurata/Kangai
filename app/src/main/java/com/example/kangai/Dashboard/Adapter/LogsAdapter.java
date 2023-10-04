package com.example.kangai.Dashboard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kangai.Dashboard.ViewPlants;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.R;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.RecyclerViewHolder> {

    private ArrayList<Logs> logsArrayList;

    public LogsAdapter(ArrayList<Logs> logsArrayList) {
        this.logsArrayList = logsArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_dashboard_recyclerview_logs_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Logs log = logsArrayList.get(position);
        holder.logView.setText(log.getLog());
        holder.timestamp.setText(log.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return logsArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView logView, timestamp;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            logView = itemView.findViewById(R.id.log);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
