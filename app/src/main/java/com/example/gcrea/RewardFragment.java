package com.example.gcrea;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RewardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RewardFragment extends Fragment {

    private User user;

    public RewardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @user user Current user.
     * @return A new instance of fragment RewardFragment.
     */
    public static RewardFragment newInstance(User user) {
        RewardFragment fragment = new RewardFragment();
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
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        TextView pointsText = view.findViewById(R.id.pointsText);
        View point_fill = view.findViewById(R.id.view_fill);

        pointsText.setText(String.valueOf(user.getPoints()));
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, user.getPoints(), getResources().getDisplayMetrics());
        point_fill.setLayoutParams(new ConstraintLayout.LayoutParams(width, height));

        return view;
    }
}