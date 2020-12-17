package com.example.gcrea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddRequest extends AppCompatActivity {

    private String userid;
    private RecycleRequest request;
    private TextView dateText, timeText;
    private EditText remarksText, otherText;
    private CheckBox paperCbx, pbCbx, eleCbx, otherCbx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        ImageView backArrow = findViewById(R.id.arrowBack);
        dateText = findViewById(R.id.editText_reDate);
        timeText = findViewById(R.id.editText_reTime);
        paperCbx = findViewById(R.id.checkBox_papers);
        pbCbx = findViewById(R.id.checkBox_plasticBottles);
        eleCbx = findViewById(R.id.checkBox_electronics);
        otherCbx = findViewById(R.id.checkBox_other);
        otherText = findViewById(R.id.editText_otherItems);
        remarksText = findViewById(R.id.editText_reRemarks);
        Button submitButton = findViewById(R.id.recycleForm_submitButton);
        Button resetButton = findViewById(R.id.recycleForm_resetButton);

        // Try get user
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            User user = intent.getExtras().getParcelable(MainMenu.USER);
            if (user != null) {
                userid = user.getIdNo();
            }
        }


        backArrow.setOnClickListener(view -> finish());

        // Show other text when checkbox clicked
        otherCbx.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                otherText.setVisibility(View.VISIBLE);
            } else {
                otherText.setVisibility(View.GONE);
                otherText.setText(null);
            }
        });

        // Submit button pressed
        submitButton.setOnClickListener(view -> {
            String errMsg = "";
            ArrayList<String> items = new ArrayList<>();
            String date = dateText.getText().toString();
            String time = timeText.getText().toString();
            boolean papersChecked = paperCbx.isChecked();
            if (papersChecked) items.add("Paper");
            boolean bottlesChecked = pbCbx.isChecked();
            if (papersChecked) items.add("Plastic Bottles");
            boolean electronicsChecked = eleCbx.isChecked();
            if (papersChecked) items.add("Electronics");
            boolean othersChecked = otherCbx.isChecked();
            String otherItem = otherText.getText().toString();
            if (papersChecked) items.add(otherItem);
            boolean oneChecked = !(papersChecked || bottlesChecked || electronicsChecked || (othersChecked && !otherItem.isEmpty()));
            String remarks = remarksText.getText().toString();

            errMsg += date.isEmpty() ? "Please specify a date" : "";
            errMsg += time.isEmpty() ? "\nPlease specify a time" : "";
            errMsg += oneChecked ? "\nPlease specify at least 1 Item" : "";

            if (!errMsg.equals("")){
                request = new RecycleRequest(userid, date, time, remarks, MainMenu.REQUEST_PENDING);
                request.setRecycleItemCats(items);

                Intent intent2 = new Intent();
                intent2.putExtra(MainMenu.SELECTED_REQUEST, request);
                setResult(RESULT_OK, intent2);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });


        // Reset button pressed
        resetButton.setOnClickListener(v -> {
            dateText.setText(null);
            timeText.setText(null);
            paperCbx.setChecked(false);
            pbCbx.setChecked(false);
            eleCbx.setChecked(false);
            otherCbx.setChecked(false);
            otherText.setText(null);
            remarksText.setText(null);
        });

        // Date picker
        dateText.setOnClickListener(view -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            // Launch Date Picker Dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (datePicker, i, i1, i2) -> {
                        String date = i2 + "-" + (i1 + 1) + "-" + i;
                        dateText.setText(date);
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
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (timePicker, i, i1) -> {
                        String time = i + ":" + i1 + ":00";
                        timeText.setText(time);
                    }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePickerDialog.show();
        });
    }
}