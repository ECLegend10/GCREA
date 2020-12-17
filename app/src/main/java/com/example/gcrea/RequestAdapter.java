package com.example.gcrea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private RecycleRequest recycleRequest;
    private ArrayList<RecycleRequest> requests;
    private EventAdapter.DBInteractionListener dbInteractionListener;

    public RequestAdapter(ArrayList<RecycleRequest> requests, EventAdapter.DBInteractionListener dbInteractionListener) {
        this.requests = requests;
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request,parent,false);
        return new RequestAdapter.ViewHolder(view, dbInteractionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        recycleRequest = requests.get(position);
        holder.id.setText("Requested By: " + recycleRequest.getUserid());
        holder.date.setText("Date: " + recycleRequest.getDate());
        holder.time.setText("Time: " + recycleRequest.getTime());
        holder.items.setText("Items: " + recycleRequest.getRecycleItemCatsToString());
        holder.remarks.setText("Remarks: " + recycleRequest.getRemarks());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView id, date, time, items, remarks;

        public ViewHolder(@NonNull View itemView, final EventAdapter.DBInteractionListener dbInteractionListener) {
            super(itemView);

            ImageView accept = itemView.findViewById(R.id.acceptButton);
            ImageView reject = itemView.findViewById(R.id.rejectButton);
            id = itemView.findViewById(R.id.request_student_id);
            date = itemView.findViewById(R.id.request_date);
            time = itemView.findViewById(R.id.request_time);
            items = itemView.findViewById(R.id.request_items);
            remarks = itemView.findViewById(R.id.request_remarks);

            accept.setOnClickListener(view -> {
                int position = getAdapterPosition();
                recycleRequest = requests.get(position);
                recycleRequest.setStatus(MainMenu.REQUEST_ACCEPT);
                dbInteractionListener.UpdateRequest(recycleRequest);
                notifyDataSetChanged();
            });

            reject.setOnClickListener(view -> {
                int position = getAdapterPosition();
                recycleRequest = requests.get(position);
                recycleRequest.setStatus(MainMenu.REQUEST_DECLINE);
                dbInteractionListener.UpdateRequest(recycleRequest);
                notifyDataSetChanged();
            });
        }
    }
}
