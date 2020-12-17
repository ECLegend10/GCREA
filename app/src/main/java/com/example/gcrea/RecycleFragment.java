package com.example.gcrea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecycleFragment extends Fragment {

    private static final String REQUESTS = "param2";

    private User user;
    private RecycleRequest recycleRequest;
    private ArrayList<RecycleRequest> requests;
    private EventAdapter.DBInteractionListener dbInteractionListener;

    public RecycleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @param requests Parameter 2.
     * @return A new instance of fragment RecycleFragment.
     */
    public static RecycleFragment newInstance(User user, ArrayList<RecycleRequest> requests) {
        RecycleFragment fragment = new RecycleFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainMenu.USER, user);
        args.putParcelableArrayList(REQUESTS, requests);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(MainMenu.USER);
            requests = getArguments().getParcelableArrayList(REQUESTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);

        ConstraintLayout forUserRequest = view.findViewById(R.id.ForUserRequest);
        ConstraintLayout forAdminRequest = view.findViewById(R.id.ForAdminRequest);

        if (user.getRole().equals("user")) {
            // for user
            forUserRequest.setVisibility(View.VISIBLE);
            forAdminRequest.setVisibility(View.GONE);
            TextView noRequest = view.findViewById(R.id.noRequest);
            ConstraintLayout hasRequest = view.findViewById(R.id.hasRequest);
            FloatingActionButton addButton = view.findViewById(R.id.addButton);

            TextView request_date = view.findViewById(R.id.request_date);
            TextView request_time = view.findViewById(R.id.request_time);
            TextView request_items = view.findViewById(R.id.request_items);
            TextView request_remarks = view.findViewById(R.id.request_remarks);
            TextView request_status = view.findViewById(R.id.request_status);

            if (requestExists(user.getIdNo())) {
                // Have request
                noRequest.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                request_date.setText("Date: " + recycleRequest.getDate());
                request_time.setText("Time: " + recycleRequest.getTime());
                request_items.setText("Items: " + recycleRequest.getRecycleItemCatsToString());
                request_remarks.setText("Remarks: " + recycleRequest.getRemarks());
                request_status.setText("Status: " + recycleRequest.getStatus());
            } else {
                // No request yet
                hasRequest.setVisibility(View.GONE);
            }

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddRequest();
                }
            });

            MapsFragment mapsFragment = new MapsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.mapView, mapsFragment)
                    .commit();
        } else {
            // for admin
            /*
            forUserRequest.setVisibility(View.VISIBLE);
            forAdminRequest.setVisibility(View.GONE);
            Context context = getContext();

            TextView noPendingRequest = view.findViewById(R.id.noPendingRequest);
            RecyclerView pendingRecycler = view.findViewById(R.id.pendingRecyclerView);
            TextView noAcceptedRequest = view.findViewById(R.id.noAcceptedRequest);
            RecyclerView acceptedRecycler = view.findViewById(R.id.acceptedRecyclerView);

            ArrayList<RecycleRequest> pendingRequests = GenerateTempRequests(MainMenu.REQUEST_PENDING);
            ArrayList<RecycleRequest> acceptedRequests = GenerateTempRequests(MainMenu.REQUEST_ACCEPT);
            if (pendingRequests.isEmpty()) {
                noPendingRequest.setVisibility(View.VISIBLE);
                pendingRecycler.setVisibility(View.GONE);
            } else {
                noPendingRequest.setVisibility(View.GONE);
                pendingRecycler.setVisibility(View.VISIBLE);
                pendingRecycler.setLayoutManager(new LinearLayoutManager(context));
                DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
                pendingRecycler.addItemDecoration(divider);
                RequestAdapter requestAdapter = new RequestAdapter(pendingRequests, dbInteractionListener);
                pendingRecycler.setAdapter(requestAdapter);

            }

            if (acceptedRequests.isEmpty()) {
                noAcceptedRequest.setVisibility(View.VISIBLE);
                acceptedRecycler.setVisibility(View.GONE);
            } else {
                noAcceptedRequest.setVisibility(View.GONE);
                acceptedRecycler.setVisibility(View.VISIBLE);
                acceptedRecycler.setLayoutManager(new LinearLayoutManager(context));
                DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
                acceptedRecycler.addItemDecoration(divider);
                RequestAdapter requestAdapter = new RequestAdapter(acceptedRequests, dbInteractionListener);
                acceptedRecycler.setAdapter(requestAdapter);
            }*/
        }
        return view;
    }

    public void AddRequest() {
        Intent intent = new Intent(getActivity(), AddRequest.class);
        intent.putExtra(MainMenu.USER, user);
        startActivityForResult(intent, MainMenu.REQUEST);
    }

    public boolean requestExists(String idNo) {
        if (requests != null) {
            if (!requests.isEmpty()) {
                for (RecycleRequest request : requests) {
                    if (request.getUserid().equals(idNo)) {
                        recycleRequest = request;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<RecycleRequest> GenerateTempRequests(String status) {
        ArrayList<RecycleRequest> tempRequests = new ArrayList<>();
        for (RecycleRequest request : requests) {
            if(request.getStatus().equals(status)) {
                tempRequests.add(request);
            }
        }
        return tempRequests;
    }

    public void setDbInteractionListener(EventAdapter.DBInteractionListener dbInteractionListener) {
        this.dbInteractionListener = dbInteractionListener;
    }
}