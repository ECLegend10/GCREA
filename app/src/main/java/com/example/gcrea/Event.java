package com.example.gcrea;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Event implements Parcelable {
    private int id;
    private String name;
    private String venue;
    private String date;
    private String time;
    private String description;
    private Double fees;
    private int hasEnded;
    private int points;
    private Bitmap poster;
    private ArrayList<Integer> participantids;

    public Event(String name, String venue, String date, String time, String description, double fees, int points, Bitmap poster) {
        this.id = 0;
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.description = description;
        this.fees = fees;
        this.points = points;
        this.poster = poster;
        this.hasEnded = 0; // 1 = ended, 0 = not ended
        this.participantids = new ArrayList<>();
    }

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        venue = in.readString();
        date = in.readString();
        time = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            fees = null;
        } else {
            fees = in.readDouble();
        }
        hasEnded = in.readInt();
        points = in.readInt();
        //poster = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public int getHasEnded() {
        return hasEnded;
    }

    public void setHasEnded(int hasEnded) {
        this.hasEnded = hasEnded;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public String getParticipantids() {
        return participantids.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    // Get all id from strings
    public void setParticipantids(String participantidList) {
        try {
            if (!participantidList.equals("")) {
                if (!participantids.isEmpty()) {
                    participantids.clear();
                }
                String[] ids = participantidList.split(",");
                for (String id : ids) {
                    Integer idd = Integer.parseInt(id);
                    participantids.add(idd);
                }
            }
        } catch (Exception e) {
            // Error
            int u = 0;
        }
    }

    // Get number of participants
    public int getNumberofparticipants(){
        return participantids.size();
    }

    // Add participant id
    public void addParticipant(int userid){
        participantids.add(userid);
    }

    // Remove participant id
    public void removeParticipant(int userid){
        participantids.remove(userid);
    }

    // Check participant has joined
    public boolean hasJoined(int userid) {
        for(int id : participantids){
            if (id == userid) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(venue);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(description);
        if (fees == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(fees);
        }
        parcel.writeInt(hasEnded);
        parcel.writeInt(points);
       // parcel.writeParcelable(poster, i);
    }
}
