package com.example.gcrea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddEditEventActivity extends AppCompatActivity {

    private EditText nameText, descText, venueText, feesText, pointsText;
    private Button pickButton, addButton;
    private TextView dateText, timeText, titleText;
    private Event event;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);
        action = "";

        titleText = findViewById(R.id.addedit_action_bar);
        nameText = findViewById(R.id.edit_text_name);
        descText = findViewById(R.id.edit_text_desc);
        venueText = findViewById(R.id.edit_text_venue);
        dateText = findViewById(R.id.edit_text_date);
        timeText = findViewById(R.id.edit_text_time);
        feesText = findViewById(R.id.edit_text_fees);
        pointsText = findViewById(R.id.editTextPoints);
        pickButton = findViewById(R.id.event_poster_input);
        addButton = findViewById(R.id.addEditButton);

        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            // Check is it either add or edit
            event = intent.getExtras().getParcelable(MainMenu.SELECTED_EVENT);
            if (event != null) {
                // Edit
                action = "edit";
                titleText.setText(R.string.edit_event_title);
                nameText.setText(event.getName());
                descText.setText(event.getDescription());
                venueText.setText(event.getVenue());
                dateText.setText(event.getDate());
                timeText.setText(event.getTime());
                feesText.setText(String.valueOf(event.getFees()));
                pointsText.setText(String.valueOf(event.getPoints()));
                addButton.setText(R.string.edit_event_button);
            } else {
                // add
                action = "add";
                titleText.setText(R.string.add_event_title);
                addButton.setText(R.string.add_event_text);
            }

            // Date picker
            dateText.setOnClickListener(view -> {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                String date = i2 + "-" + (i1 + 1) + "-" + i;
                                dateText.setText(date);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                // Set today's date as minimum date and all the past dates are disabled.
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            });

            // Time picker
            timeText.setOnClickListener(view -> {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getParent(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                String time = i + ":" + i1 + ":00";
                                timeText.setText(time);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            });

            pickButton.setOnClickListener(view -> {
                // Select a picture from gallery
                Toast.makeText(getApplicationContext(), "Sorry. Pick image from gallery feature not available yet.", Toast.LENGTH_SHORT).show();
            });

            addButton.setOnClickListener(view -> {
                String name, desc, venue, date, time;
                Double fees;
                int points;
                boolean passed = true;
                // Check empty
                if (nameText.getText().toString().equals(""))
                {
                    nameText.setError("Please enter the event name!");
                    passed = false;
                }

                if (passed) {
                    name = nameText.getText().toString();
                    desc = descText.getText().toString();
                    venue = venueText.getText().toString();
                    date = dateText.getText().toString();
                    time = timeText.getText().toString();
                    fees = Double.parseDouble(feesText.getText().toString());
                    points = Integer.parseInt(pointsText.getText().toString());

                    if (action.equals("edit")) {
                        // for edit
                        event.setName(name);
                        event.setDescription(desc);
                        event.setVenue(venue);
                        event.setDate(date);
                        event.setTime(time);
                        event.setFees(fees);
                        event.setPoints(points);
                    } else {
                        // for add
                        event = new Event(name, venue, date, time, desc, fees, points, null);
                    }

                    if (event != null) {
                        // back to main activity
                        Intent intent1 = new Intent();
                        intent1.putExtra(MainMenu.SELECTED_EVENT, event);
                        setResult(MainMenu.ADDEVENT, intent1);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Event is invalid!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }

    }
}