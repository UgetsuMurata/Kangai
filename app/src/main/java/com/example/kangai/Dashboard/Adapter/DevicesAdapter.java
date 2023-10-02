package com.example.kangai.Dashboard.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kangai.Objects.Device;
import com.example.kangai.R;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.RecyclerViewHolder> {

    private ArrayList<Device> deviceArrayList;

    public DevicesAdapter(ArrayList<Device> deviceArrayList) {
        this.deviceArrayList = deviceArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_dashboard_recyclerview_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Device device = deviceArrayList.get(position);
        holder.deviceName.setText(device.getName());
    }

    @Override
    public int getItemCount() {
        return deviceArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView deviceImage;
        private final TextView deviceName;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceImage = itemView.findViewById(R.id.device_image);
            deviceName = itemView.findViewById(R.id.device_name);
        }
    }
}
