package com.example.gcrea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetails extends AppCompatActivity {

    private Event event;
    private User user;
    private ImageView imageView;
    private TextView nameText, venueText, dateText, timeText,
            feesText, pointsText, descriptionText, noText;
    private Button joinButton, editButton, viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        imageView = findViewById(R.id.image_display);
        nameText = findViewById(R.id.event_name_disp);
        venueText = findViewById(R.id.event_venue_disp);
        dateText = findViewById(R.id.event_date_disp);
        timeText = findViewById(R.id.event_time_disp);
        feesText = findViewById(R.id.event_fees_disp);
        pointsText = findViewById(R.id.event_points_disp);
        noText = findViewById(R.id.event_participantsno_disp);
        descriptionText = findViewById(R.id.event_desc_disp);
        joinButton = findViewById(R.id.joinButton);
        editButton = findViewById(R.id.editButton);
        viewButton = findViewById(R.id.viewParticipantsButton);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Event tempEvent = intent.getExtras().getParcelable(MainMenu.SELECTED_EVENT);
            User user = intent.getExtras().getParcelable(MainMenu.USER);
            if (tempEvent != null && user != null) {
                event = tempEvent;
                if (event.getPoster()!=null) {
                    imageView.setImageBitmap(event.getPoster());
                }
                nameText.setText(event.getName());
                venueText.setText("Venue" + event.getVenue());
                dateText.setText("Date: " + event.getDate());
                timeText.setText("Time: " + event.getTime());
                feesText.setText("Fees:" + event.getFees());
                pointsText.setText("Points: " + event.getPoints());
                noText.setText("Current Number of Participants: " + event.getNumberofparticipants());
                descriptionText.setText(event.getDescription());


                // Remove edit button
                if (user.getRole().equals("user")) {
                    editButton.setVisibility(View.GONE);
                    viewButton.setVisibility(View.GONE);
                }
                // Remove join button
                if (event.getHasEnded() == 1 || event.hasJoined(user.getId())){
                    joinButton.setVisibility(View.GONE);
                }

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Go edit page
                        EditEvent();
                    }
                });

                viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // View the participants who join
                        Toast.makeText(getApplicationContext(), "Sorry. View participants feature not available yet.", Toast.LENGTH_LONG).show();
                    }
                });

                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // User join
                        AddUser();
                    }
                });
            }
        }
    }

    public void AddUser() {
        event.addParticipant(user.getId());
        user.setPoints(user.getPoints() + event.getPoints());
        Intent intent = new Intent();
        intent.putExtra(MainMenu.SELECTED_EVENT, event);
        intent.putExtra(MainMenu.USER, user);
        setResult(MainMenu.JOINEVENT, intent);
        finish();
    }

    public void EditEvent() {
        Intent intent = new Intent((Activity) getApplicationContext(), AddEditEventActivity.class);
        intent.putExtra(MainMenu.SELECTED_EVENT, event);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == MainMenu.EDITUSER) {
                if (data.getExtras() != null) {
                    event = data.getExtras().getParcelable(MainMenu.SELECTED_EVENT);
                    if (event != null) {
                        Intent intent = new Intent();
                        intent.putExtra(MainMenu.SELECTED_EVENT, event);
                        setResult(MainMenu.EDITEVENT, intent);
                        finish();
                    }
                }
            }
        }
    }
}