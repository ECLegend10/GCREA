package com.example.gcrea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private Event event;
    private User user;
    private ArrayList<Event> events;
    private DBInteractionListener dbInteractionListener;

    public EventAdapter(User user, ArrayList<Event> events, DBInteractionListener dbInteractionListener) {
        this.user = user;
        this.events = events;
        this.dbInteractionListener = dbInteractionListener;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event,parent,false);
        context = parent.getContext();
        return new ViewHolder(view, dbInteractionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        event = events.get(position);
        holder.eventName.setText(event.getName());
        holder.eventDescription.setText(event.getDescription());
        if (event.getPoster()!=null) {
            holder.eventImage.setImageBitmap(event.getPoster());
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView eventName, eventDescription;
        public ImageView eventImage;
        public Button joinButton;
        public DBInteractionListener dbInteractionListener;

        public ViewHolder(@NonNull View itemView, final DBInteractionListener dbInteractionListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDescription = itemView.findViewById(R.id.event_desc);
            eventImage = itemView.findViewById(R.id.event_image);
            joinButton = itemView.findViewById(R.id.joinButton);
            this.dbInteractionListener = dbInteractionListener;

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Event event = events.get(position);
                    event.addParticipant(user.getId());
                    user.setPoints(user.getPoints() + event.getPoints());
                    dbInteractionListener.UpdateEvent(event);
                    dbInteractionListener.UpdateUser(user);
                    notifyDataSetChanged();
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Activity activity = (Activity) context;
            Intent intent = new Intent(activity, EventDetails.class);
            intent.putExtra(MainMenu.USER, user);
            intent.putExtra(MainMenu.SELECTED_EVENT, event);
            activity.startActivity(intent);
        }
    }

    public interface EventDetails {
        int viewEvent(User user, Event event);
    }

    public interface DBInteractionListener {
        // Get list
        ArrayList<Event> getEventList();
        ArrayList<User> getUserList();
        ArrayList<User> getUserListInEvent(int id);
        ArrayList<RecycleRequest> getRequestList();

        // Add
        void AddUser(User user);
        void AddEvent(Event event);
        void AddRequest(RecycleRequest recycleRequest);

        // Update
        void UpdateUser(User user);
        void UpdateEvent(Event event);
        void UpdateRequest(RecycleRequest recycleRequest);

        // Delete
        void DeleteUser(int id);
        void DeleteEvent(int id);
        void DeleteRequest(int id);

        // Count
        int CountUser();
        int CountEvent();
        int CountRequest();
    }
}
