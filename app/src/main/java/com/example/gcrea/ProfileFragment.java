package com.example.gcrea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView circleImageView;
    private TextView nameText, idText, positionText, courseText,
            emailText, contactText, datejoinText, pointsText;
    private User user;
    private Context context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @user user Current user.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainMenu.USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(MainMenu.USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getContext();
        nameText = view.findViewById(R.id.nameText);
        idText = view.findViewById(R.id.idText);
        positionText = view.findViewById(R.id.positionText);
        courseText = view.findViewById(R.id.courseText);
        emailText = view.findViewById(R.id.emailText);
        contactText = view.findViewById(R.id.contactText);
        datejoinText = view.findViewById(R.id.datejoinText);
        pointsText = view.findViewById(R.id.pointsText);

        circleImageView = view.findViewById(R.id.profile_picture);
        FloatingActionButton editButton = view.findViewById(R.id.editButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button checkUsersButton = view.findViewById(R.id.checkUserButton);

        if (user != null) {
            if (user.getRole().equals("user")) {
                checkUsersButton.setVisibility(View.GONE);
            }

            nameText.setText(user.getName());
            idText.setText(user.getIdNo());
            positionText.setText(user.getPosition());
            courseText.setText(user.getCourse());
            emailText.setText(user.getEmail());
            contactText.setText(user.getContactNo());
            datejoinText.setText(user.getDateJoined());
            pointsText.setText(String.valueOf(user.getPoints()));

            if (user.getProfilePic() != null) {
                circleImageView.setImageBitmap(user.getProfilePic());
            }
        }

        // Apply on click listener
        // Allow user to take picture and replace
        circleImageView.setOnClickListener(view1 -> Toast.makeText(context, "Sorry. Take picture feature not available yet.", Toast.LENGTH_LONG).show());

        // Allow user to edit
        editButton.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), EditProfile.class);
            intent.putExtra(MainMenu.USER, user);
            startActivityForResult(intent, MainMenu.EDITUSER);
        });

        // Allow user to log out
        logoutButton.setOnClickListener(view13 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(MainActivity.REQUEST_LOG_OUT, 1);
            startActivityForResult(intent, MainActivity.LOGOUT);
            Toast.makeText(context, "Logging out. Please wait.", Toast.LENGTH_LONG).show();
        });

        // Allow admin to check on all users
        checkUsersButton.setOnClickListener(view14 -> {
            // add adapter (parceable array list)
            Toast.makeText(context, "Sorry. Check all users feature not available yet.", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MainMenu.EDITUSER) {
            if (resultCode == RESULT_OK) {
                // Update information after edit
                UpdateUserInformation(data);
                // Update user information to database
                Intent intent = new Intent();
                intent.putExtra(MainMenu.USER, user);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        }

    }

    public void UpdateUserInformation(Intent intent) {
        if (intent != null) {
            if (intent.getExtras() != null) {
                User testUser = intent.getExtras().getParcelable(MainMenu.USER);
                if (testUser != null) {
                    // Override text
                    user = testUser;
                    nameText.setText(user.getName());
                    idText.setText(user.getIdNo());
                    positionText.setText(user.getPosition());
                    courseText.setText(user.getCourse());
                    emailText.setText(user.getEmail());
                    contactText.setText(user.getContactNo());
                    datejoinText.setText(user.getDateJoined());
                    pointsText.setText(String.valueOf(user.getPoints()));

                    if (user.getProfilePic() != null) {
                        circleImageView.setImageBitmap(user.getProfilePic());
                    }
                }
            }
        }
    }
}