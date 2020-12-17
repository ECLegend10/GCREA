package com.example.gcrea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static java.sql.Types.NULL;

public class DBHandler extends SQLiteOpenHelper {
    // All static variables
    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "sgcdatabase";
    // Table name for user
    private static final String TABLE_USERS = "users";
    // Table columns names for user
    private static final String USER_ID = "id";
    private static final String USER_NAME = "name";
    private static final String USER_IDNO = "idno";
    private static final String USER_ROLE = "role";
    private static final String USER_POSITION = "position";
    private static final String USER_COURSE = "course";
    private static final String USER_EMAIL = "email";
    private static final String USER_CONTACT = "contactno";
    private static final String USER_DATEJOINED = "datejoined";
    private static final String USER_POINTS = "points";
    private static final String USER_PIC = "profilepic";

    // Table name for event
    private static final String TABLE_EVENTS = "events";
    // Table columns names for event
    private static final String EVENT_ID = "id";
    private static final String EVENT_NAME = "name";
    private static final String EVENT_VENUE = "venue";
    private static final String EVENT_DATE = "date";
    private static final String EVENT_TIME = "time";
    private static final String EVENT_DESCRIPTION = "description";
    private static final String EVENT_FEES = "fees";
    private static final String EVENT_HASENDED = "hasEnded";
    private static final String EVENT_POINTS = "points";
    private static final String EVENT_POSTER = "poster";
    private static final String EVENT_PARTICIPANTIDS = "participantids";

    // Table name for request
    private static final String TABLE_REQUESTS = "recyclerequests";
    // Table columns names for request
    private static final String REQUEST_ID = "id";
    private static final String REQUEST_USERID = "userid";
    private static final String REQUEST_DATE = "date";
    private static final String REQUEST_TIME = "time";
    private static final String REQUEST_ITEMS = "items";
    private static final String REQUEST_REMARKS = "remarks";
    private static final String REQUEST_STATUS = "status";


    public DBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create user table
        String CREATE_USERS_TABLE = "create table " + TABLE_USERS + "(" + USER_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, "
                + USER_IDNO + " TEXT, " + USER_ROLE + " TEXT, "
                + USER_POSITION + " TEXT, " + USER_COURSE + " TEXT, "
                + USER_EMAIL + " TEXT, " + USER_CONTACT + " TEXT, "
                + USER_DATEJOINED + " TEXT, " + USER_POINTS + " INTEGER DEFAULT 1, "
                + USER_PIC + " BLOB);";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);

        // Create event table
        String CREATE_EVENTS_TABLE = "create table " + TABLE_EVENTS + "(" + EVENT_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME + " TEXT, "
                + EVENT_VENUE + " TEXT, " + EVENT_DATE + " TEXT, "
                + EVENT_TIME + " TEXT, " + EVENT_DESCRIPTION + " TEXT, " + EVENT_FEES + " REAL DEFAULT 0.0, "
                + EVENT_HASENDED + " INTEGER DEFAULT 0, " + EVENT_POINTS + " INTEGER DEFAULT 0, "
                + EVENT_POSTER + " BLOB, " + EVENT_PARTICIPANTIDS + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);

        // Create request table
        String CREATE_REQUESTS_TABLE = "create table " + TABLE_REQUESTS + "(" + REQUEST_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REQUEST_USERID + " TEXT NOT NULL, "
                + REQUEST_DATE + " TEXT, " + REQUEST_TIME + " TEXT, "
                + REQUEST_ITEMS + " TEXT, " + REQUEST_REMARKS + " TEXT, "
                + REQUEST_STATUS + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_REQUESTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        // Create table again
        onCreate(sqLiteDatabase);
    }

    // Add user
    public long AddUser(User user){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, user.getName());
        contentValues.put(USER_IDNO, user.getIdNo());
        contentValues.put(USER_ROLE, user.getRole());
        contentValues.put(USER_POSITION, user.getPosition());
        contentValues.put(USER_COURSE, user.getCourse());
        contentValues.put(USER_EMAIL, user.getEmail());
        contentValues.put(USER_CONTACT, user.getContactNo());
        contentValues.put(USER_DATEJOINED, user.getDateJoined());
        contentValues.put(USER_POINTS, user.getPoints());
        contentValues.put(USER_PIC, getImageBytes(user.getProfilePic()));
        return sqLiteDatabase.insert(TABLE_USERS,null, contentValues);
    }

    // Add event
    public long AddEvent(Event event){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_NAME, event.getName());
        contentValues.put(EVENT_VENUE, event.getVenue());
        contentValues.put(EVENT_DATE, event.getDate());
        contentValues.put(EVENT_TIME, event.getTime());
        contentValues.put(EVENT_DESCRIPTION, event.getDescription());
        contentValues.put(EVENT_FEES, event.getFees());
        contentValues.put(EVENT_HASENDED, event.getHasEnded());
        contentValues.put(EVENT_POINTS, event.getPoints());
        contentValues.put(EVENT_POSTER, getImageBytes(event.getPoster()));
        contentValues.put(EVENT_PARTICIPANTIDS, event.getParticipantids());
        return sqLiteDatabase.insert(TABLE_EVENTS,null, contentValues);
    }

    // Add request
    public long AddRequest(RecycleRequest recycleRequest){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_USERID, recycleRequest.getUserid());
        contentValues.put(REQUEST_DATE, recycleRequest.getDate());
        contentValues.put(REQUEST_TIME, recycleRequest.getTime());
        contentValues.put(REQUEST_ITEMS, recycleRequest.getRecycleItemCatsToString());
        contentValues.put(REQUEST_REMARKS, recycleRequest.getRemarks());
        contentValues.put(REQUEST_STATUS, recycleRequest.getStatus());
        return sqLiteDatabase.insert(TABLE_REQUESTS,null, contentValues);
    }

    // Count database rows
    public long CountUsers(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_USERS);
    }

    public long CountEvents(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_EVENTS);
    }

    public long CountRequests(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_REQUESTS);
    }

    // Update user
    public long UpdateUser(User user){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, user.getName());
        contentValues.put(USER_IDNO, user.getIdNo());
        contentValues.put(USER_ROLE, user.getRole());
        contentValues.put(USER_POSITION, user.getPosition());
        contentValues.put(USER_COURSE, user.getCourse());
        contentValues.put(USER_EMAIL, user.getEmail());
        contentValues.put(USER_CONTACT, user.getContactNo());
        contentValues.put(USER_DATEJOINED, user.getDateJoined());
        contentValues.put(USER_POINTS, user.getPoints());
        contentValues.put(USER_PIC, getImageBytes(user.getProfilePic()));
        return sqLiteDatabase.update(TABLE_USERS, contentValues,USER_ID + "=" + user.getId(),null);
    }

    // Update event
    public long UpdateEvent(Event event){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_NAME, event.getName());
        contentValues.put(EVENT_VENUE, event.getVenue());
        contentValues.put(EVENT_DATE, event.getDate());
        contentValues.put(EVENT_TIME, event.getTime());
        contentValues.put(EVENT_DESCRIPTION, event.getDescription());
        contentValues.put(EVENT_FEES, event.getFees());
        contentValues.put(EVENT_HASENDED, event.getHasEnded());
        contentValues.put(EVENT_POINTS, event.getPoints());
        contentValues.put(EVENT_POSTER, getImageBytes(event.getPoster()));
        contentValues.put(EVENT_PARTICIPANTIDS, event.getParticipantids());
        return sqLiteDatabase.update(TABLE_EVENTS, contentValues,EVENT_ID + "=" + event.getId(),null);
    }

    // Update request
    public long UpdateRequest(RecycleRequest recycleRequest){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REQUEST_USERID, recycleRequest.getUserid());
        contentValues.put(REQUEST_DATE, recycleRequest.getDate());
        contentValues.put(REQUEST_TIME, recycleRequest.getTime());
        contentValues.put(REQUEST_ITEMS, recycleRequest.getRecycleItemCatsToString());
        contentValues.put(REQUEST_REMARKS, recycleRequest.getRemarks());
        contentValues.put(REQUEST_STATUS, recycleRequest.getStatus());
        return sqLiteDatabase.update(TABLE_REQUESTS, contentValues,REQUEST_ID + "=" + recycleRequest.getId(),null);
    }

    // Delete user
    public long DeleteUser(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.delete(TABLE_USERS,USER_ID + "=" + id,null);
    }

    // Delete event
    public long DeleteEvent(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.delete(TABLE_EVENTS,EVENT_ID + "=" + id,null);
    }

    // Remove request
    public long DeleteRequest(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.delete(TABLE_REQUESTS,REQUEST_ID + "=" + id,null);
    }

    // Get user list
    public ArrayList<User> getAllUsers(){
        ArrayList<User> userArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String idno = cursor.getString(2);
                String role = cursor.getString(3);
                String position = cursor.getString(4);
                String course = cursor.getString(5);
                String email = cursor.getString(6);
                String contactno = cursor.getString(7);
                String datejoined = cursor.getString(8);
                int points = cursor.getInt(9);
                byte[] profilepicbyte = cursor.getBlob(10);
                Bitmap profilePic = getBitmap(profilepicbyte);

                User user = new User(name, idno, role, position, course, email, contactno, datejoined, points, profilePic);
                user.setId(id);
                userArrayList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userArrayList;
    }

    // Get event list
    public ArrayList<Event> getAllEvent(){
        ArrayList<Event> events = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String venue = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String description = cursor.getString(5);
                double fees = cursor.getDouble(6);
                int hasEnded = cursor.getInt(7);
                int points = cursor.getInt(8);
                byte[] imgByte = cursor.getBlob(9);
                Bitmap posterImg = getBitmap(imgByte);
                String participantids = cursor.getString(10);
                Event event = new Event(name, venue, date, time, description, fees, points, posterImg);
                event.setId(id);
                event.setParticipantids(participantids);
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    //get user (used for getting the specific user who log in or for participant list)
    public User getUser(int id) {
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + USER_ID + " = " + id;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        Log.d("Find user", selectQuery);

        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            String name = cursor.getString(1);
            String idno = cursor.getString(2);
            String role = cursor.getString(3);
            String position = cursor.getString(4);
            String course = cursor.getString(5);
            String email = cursor.getString(6);
            String contactno = cursor.getString(7);
            String datejoined = cursor.getString(8);
            int points = cursor.getInt(9);
            byte[] profilepicbyte = cursor.getBlob(10);
            Bitmap profilePic = getBitmap(profilepicbyte);

            User user = new User(name, idno, role, position, course, email, contactno, datejoined, points, profilePic);

            cursor.close();
            return user;
        }
        return null;
    }

    // get user list from event id
    public ArrayList<User> getUserlistfromEvent(int eventId){
        ArrayList<User> participants = new ArrayList<>();

        // Find the participants list
        String selectQuery = "SELECT " + EVENT_PARTICIPANTIDS + " FROM " + TABLE_EVENTS + " WHERE "
                + EVENT_ID + " = " + eventId;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // Get the participants list first
        if(cursor.moveToFirst()) {
            String ids = cursor.getString(0);
            ArrayList<String> tempUserIds = new ArrayList<>(Arrays.asList(ids.split(",")));
            for (String id : tempUserIds) {
                int idd = Integer.parseInt(id);
                participants.add(getUser(idd));
            }
        }
        cursor.close();
        return participants;
    }

    // Get userid from input email
    public int getUserid(String email) {
        String selectQuery = "SELECT " + USER_ID + " FROM " + TABLE_USERS + " WHERE "
                + USER_EMAIL + " ='" + email + "'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getInt(0); // return user id
        }
        return 0;
    }

    // Get request list
    public ArrayList<RecycleRequest> getAllRequest(){
        ArrayList<RecycleRequest> recycleRequests = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REQUESTS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do{
                int id = cursor.getInt(0);
                String userid = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String recycleItemCats = cursor.getString(4);
                String remarks = cursor.getString(5);
                String status = cursor.getString(6);
                RecycleRequest recycleRequest = new RecycleRequest(userid, date, time, remarks, status);
                recycleRequest.setId(id);
                recycleRequest.setRecycleItemCats(recycleItemCats);
                recycleRequests.add(recycleRequest);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recycleRequests;
    }

    // convert from bitmap to byte array
    public static byte[] getImageBytes(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    // convert from byte array to bitmap
    public static Bitmap getBitmap(byte[] image) {
        if (image!=null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return null;
    }
}
