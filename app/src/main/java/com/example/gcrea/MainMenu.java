package com.example.gcrea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainMenu extends AppCompatActivity implements EventAdapter.DBInteractionListener {

    public static final String USER = "CURRENT_USER";
    public static final String SELECTED_USER = "SELECTED_USER";
    public static final String SELECTED_EVENT = "SELECTED_EVENT";
    public static final String SELECTED_REQUEST = "SELECTED_REQUEST";
    public static final String REQUEST_PENDING = "PENDING";
    public static final String REQUEST_ACCEPT = "ACCEPTED";
    public static final String REQUEST_DECLINE = "DECLINED";
    public static final String ACTION = "ACTION";
    public static final int REQUEST = 1111;
    public static final int EDITUSER = 333;
    public static final int ADDEVENT = 555;
    public static final int EDITEVENT = 777;
    public static final int JOINEVENT = 456;
    public static final int EDITPROFILE = 678;

    private User user;
    private DBHandler dbHandler;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<Event> events;
    private ArrayList<RecycleRequest> requests;
    private ArrayList <User> users;
    private EventAdapter eventAdapter;
    private EventAdapter.DBInteractionListener dbInteractionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Disable default title bar
        getSupportActionBar().hide();

        dbInteractionListener = this;
        viewPager = findViewById(R.id.view_pager);
        dbHandler = new DBHandler(this, null, null, 1);
        PopulateDB();
        events = new ArrayList<>();
        requests = new ArrayList<>();
        users = new ArrayList<>();
        ResetEvents();
        ResetRequests();
        ResetUsers();

        // Get user from main activity
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            String email = intent.getExtras().getString(MainActivity.INPUT_EMAIL);

            // use email to find user
            int userid = searchEmailGetUserid(email);
            if(userid != 0) {
                // user id found, this is existing user
                // get user from user id
                user = dbHandler.getUser(userid);
            } else {
                // user id not found, create new user
                // Get current date
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String dateJoined = simpleDateFormat.format(date);

                // use email as id first to prevent error
                user = new User("Not filled yet","Not filled yet","user","Member","Not filled yet",
                        email,"Not filled yet", dateJoined,1, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));

                dbHandler.AddUser(user);
                // Reset user (get new userid)
                ResetUsers();
                user = searchUser(searchEmailGetUserid(user.getEmail()));
            }
        } else {
            finish();
        }

        // adapter classes
        eventAdapter = new EventAdapter(user, events,this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        GenerateFragments();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            //@SuppressLint("SetTextI18n")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_about:
                        // To About Fragment
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_recycle:
                        // To recycle request Fragment
                        RecycleFragment recycleFragment = RecycleFragment.newInstance(user, requests);
                        recycleFragment.setDbInteractionListener(dbInteractionListener);
                        pagerAdapter.ReplaceFragment(recycleFragment,1);
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_home:
                        // To home/event Fragment
                        HomeFragment homeFragment = HomeFragment.newInstance();
                        homeFragment.setEventAdapter(eventAdapter);
                        pagerAdapter.ReplaceFragment(homeFragment,2);
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_reward:
                        // To reward Fragment
                        RewardFragment rewardFragment = RewardFragment.newInstance(user);
                        pagerAdapter.ReplaceFragment(rewardFragment,3);
                        viewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_profile:
                        // To profile Fragment
                        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
                        pagerAdapter.ReplaceFragment(profileFragment,4);
                        viewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
    }

    public int searchEmailGetUserid(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)){
                return user.getId();
            }
        }
        return 0;
    }

    public User searchUser(int id){
        for (User user : users) {
            if (user.getId() == id){
                return user;
            }
        }
        return null;
    }

    public void ResetEvents() {
        if (!events.isEmpty()){
            events.clear();
        }
        events = dbHandler.getAllEvent();
    }

    public void ResetRequests() {
        if (!requests.isEmpty()){
            requests.clear();
        }
        requests = dbHandler.getAllRequest();
    }

    public void ResetUsers() {
        if (!users.isEmpty()){
            users.clear();
        }
        users = dbHandler.getAllUsers();
    }

    public void GenerateFragments() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        pagerAdapter.addFragment(aboutFragment);

        RecycleFragment recycleFragment = RecycleFragment.newInstance(user, requests);
        recycleFragment.setDbInteractionListener(this);
        pagerAdapter.addFragment(recycleFragment);

        HomeFragment homeFragment = HomeFragment.newInstance();
        homeFragment.setEventAdapter(eventAdapter);
        pagerAdapter.addFragment(homeFragment);

        RewardFragment rewardFragment = RewardFragment.newInstance(user);
        pagerAdapter.addFragment(rewardFragment);

        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
        pagerAdapter.addFragment(profileFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == ADDEVENT) {
                // Add event
                if (resultCode == RESULT_OK) {
                    if (data.getParcelableExtra(SELECTED_EVENT) != null) {
                        Event event = data.getParcelableExtra(SELECTED_EVENT);
                        dbHandler.AddEvent(event);
                        eventAdapter.notifyDataSetChanged();
                        ResetEvents();
                    }
                }
            }

            if(resultCode == JOINEVENT) {
                // Update event and user
                if (data.getParcelableExtra(USER) != null && data.getParcelableExtra(SELECTED_EVENT) != null) {
                    user = data.getParcelableExtra(USER);
                    Event event = data.getParcelableExtra(SELECTED_EVENT);
                    dbHandler.UpdateUser(user);
                    dbHandler.UpdateEvent(event);
                    eventAdapter.notifyDataSetChanged();
                    ResetEvents();
                }
            }

            if(resultCode == EDITEVENT) {
                // Update event
                if (data.getParcelableExtra(SELECTED_EVENT) != null) {
                    Event event = data.getParcelableExtra(SELECTED_EVENT);
                    dbHandler.UpdateEvent(event);
                    eventAdapter.notifyDataSetChanged();
                    ResetEvents();
                }
            }

            if(resultCode == EDITPROFILE) {
                // Update user
                if (data.getParcelableExtra(USER) != null) {
                    user = data.getParcelableExtra(USER);
                    if (user != null) {
                        dbHandler.UpdateUser(user);
                        ResetUsers();
                        ProfileFragment profileFragment = ProfileFragment.newInstance(user);
                        pagerAdapter.ReplaceFragment(profileFragment,4);
                        viewPager.setCurrentItem(4);
                        eventAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

    }

    @Override
    public ArrayList<Event> getEventList() {
        return dbHandler.getAllEvent();
    }

    @Override
    public ArrayList<User> getUserList() {
        return dbHandler.getAllUsers();
    }

    @Override
    public ArrayList<User> getUserListInEvent(int id) {
        return dbHandler.getUserlistfromEvent(id);
    }

    @Override
    public ArrayList<RecycleRequest> getRequestList() {
        return dbHandler.getAllRequest();
    }

    @Override
    public void AddUser(User user) {
        dbHandler.AddUser(user);
    }

    @Override
    public void AddEvent(Event event) {
        dbHandler.AddEvent(event);
    }

    @Override
    public void AddRequest(RecycleRequest recycleRequest) {
        dbHandler.AddRequest(recycleRequest);
    }

    @Override
    public void UpdateUser(User user) {
        dbHandler.UpdateUser(user);
    }

    @Override
    public void UpdateEvent(Event event) {
        dbHandler.UpdateEvent(event);
    }

    @Override
    public void UpdateRequest(RecycleRequest recycleRequest) {
        dbHandler.UpdateRequest(recycleRequest);
    }

    @Override
    public void DeleteUser(int id) {
        dbHandler.DeleteUser(id);
    }

    @Override
    public void DeleteEvent(int id) {
        dbHandler.DeleteEvent(id);
    }

    @Override
    public void DeleteRequest(int id) {
        dbHandler.DeleteRequest(id);
    }

    @Override
    public int CountUser() {
        return (int) dbHandler.CountUsers();
    }

    @Override
    public int CountEvent() {
        return (int) dbHandler.CountEvents();
    }

    @Override
    public int CountRequest() {
        return (int) dbHandler.CountRequests();
    }

    private void PopulateDB() {
        // Add some users
        if (dbHandler.CountUsers() == 0) {
            dbHandler.AddUser(new User("Laurie Carney", "123456789", "admin",
                    "President", "Biotech", "laurie123@gmail.com",
                    "0123456789", "03-04-2020", 101,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people1)));
            dbHandler.AddUser(new User("Kade Alston", "234567891", "admin",
                    "Vice President", "Engineer", "Fourswinburnesarawa@gmail.com",
                    "1234567890", "03-04-2020", 81,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people2)));
            dbHandler.AddUser(new User("Manuel Conrad", "345678912", "user",
                    "Secretary", "Marketing", "elwinchankw@gmail.com",
                    "2345678901", "03-04-2020", 51,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people3)));
            dbHandler.AddUser(new User("Jimmy Mccall", "456789123", "admin",
                    "Treasurer", "Marketing", "jimjim@gmail.com",
                    "3456789012", "03-04-2020", 41,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people4)));
            dbHandler.AddUser(new User("Conan O'Brien", "678912345", "admin",
                    "Designer", "Computer Science", "cone345@gmail.com",
                    "5678901234", "03-04-2020", 35,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people5)));
            dbHandler.AddUser(new User("James Charles", "789123456", "user",
                    "Member", "Design", "james789@gmail.com",
                    "7890123456", "13-05-2020", 1,
                    BitmapFactory.decodeResource(getResources(), R.drawable.people4)));
            dbHandler.AddUser(new User("Franklie Chong", "891234567", "user",
                    "Member", "Computer Science", "franklie99@gmail.com",
                    "8901234567", "31-08-2020", 56, null));
        }

        if (dbHandler.CountEvents() == 0) {
            // Add some events
            dbHandler.AddEvent(new Event("Taking Care of Mother Earth", "Google Meet",
                    "25-10-2021", "19:00:00", "An awareness talk", 0.0,
                    10, BitmapFactory.decodeResource(getResources(), R.drawable.people1)));
            dbHandler.AddEvent(new Event("Green Club Kahoot Day", "Google Meet",
                    "3-3-2021", "14:00:00", "Let's play Kahoot!", 0.0,
                    5, BitmapFactory.decodeResource(getResources(), R.drawable.people2)));
            dbHandler.AddEvent(new Event("World Clean-up Day Talk", "Google Meet",
                    "19-09-2021", "14:00:00", "An awareness talk", 0.0,
                    10, BitmapFactory.decodeResource(getResources(), R.drawable.people3)));
        }

        if (dbHandler.CountRequests() == 0) {
            // Add some requests //userid, String date, String time, String remarks, String status
            RecycleRequest recycleRequest = new RecycleRequest("234567891",
                    "05-01-2021", "14:00:00", "I can carry the items myself.", MainMenu.REQUEST_PENDING);
            recycleRequest.setRecycleItemCats("plastic bottles, aluminium cans");
            dbHandler.AddRequest(recycleRequest);
            recycleRequest = new RecycleRequest("345678912",
                    "21-03-2021", "09:40:00", "I might be late for 5 minutes!", MainMenu.REQUEST_PENDING);
            recycleRequest.setRecycleItemCats("old phones, used papers, aluminium cans");
            dbHandler.AddRequest(recycleRequest);
            recycleRequest = new RecycleRequest("123456789",
                    "29-12-2021", "10:00:00", "Thank you Green Club! I really need this feature!", MainMenu.REQUEST_ACCEPT);
            recycleRequest.setRecycleItemCats("old newspapers, used papers, car battery");
            dbHandler.AddRequest(recycleRequest);
        }
    }
}