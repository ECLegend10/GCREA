package com.example.gcrea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {

    private User user;
    private EditText editTextName, editTextId, editTextCourse, editTextEmail, editTextContact;
    private TextView positionText, datejoinText, pointsText;
    private Button submitButton;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextId = findViewById(R.id.editTextId);
        positionText = findViewById(R.id.positionText);
        editTextCourse = findViewById(R.id.editTextCourse);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContact = findViewById(R.id.editTextContact);
        datejoinText = findViewById(R.id.datejoinText);
        pointsText = findViewById(R.id.pointsText);

        submitButton = findViewById(R.id.submitButton);
        backArrow = findViewById(R.id.arrowBack);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                User testUser = intent.getExtras().getParcelable(MainMenu.USER);
                if (testUser != null) {
                    // Override text
                    user = testUser;
                    editTextName.setText(user.getName());
                    editTextId.setText(user.getIdNo());
                    positionText.setText(user.getPosition());
                    editTextCourse.setText(user.getCourse());
                    editTextEmail.setText(user.getEmail());
                    editTextContact.setText(user.getContactNo());
                    datejoinText.setText(user.getDateJoined());
                    pointsText.setText(String.valueOf(user.getPoints()));
                }
            }
        }

        // Pressed submit button to update
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });

        // Go back
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void UpdateUser() {
        if (editTextName.getText().toString().equals("")
                || editTextId.getText().toString().equals("")
                || editTextCourse.getText().toString().equals("")
                || editTextEmail.getText().toString().equals("")
                || editTextContact.getText().toString().equals("")) {
            // Missing input
            Toast.makeText(getApplicationContext(), "Missing input exists. Please fill again.", Toast.LENGTH_LONG).show();
        } else {
            // Update user information
            user.setName(editTextName.getText().toString());
            user.setIdNo(editTextId.getText().toString());
            user.setCourse(editTextCourse.getText().toString());
            user.setEmail(editTextEmail.getText().toString());
            user.setContactNo(editTextContact.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(MainMenu.USER, user);
            setResult(MainMenu.EDITPROFILE, intent);
            finish();
        }
    }
}
