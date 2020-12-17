package com.example.gcrea;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RecycleRequest implements Parcelable{
    private int id;
    private String userid;
    private String date;
    private String time;
    private ArrayList<String> recycleItemCats;
    private String remarks;
    private String status; // ACCEPT / PENDING // DECLINE

    public RecycleRequest(String userid, String date, String time, String remarks, String status) {
        this.id = 0;
        this.userid = userid;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.status = status;
        recycleItemCats = new ArrayList<>();
    }

    protected RecycleRequest(Parcel in) {
        id = in.readInt();
        userid = in.readString();
        date = in.readString();
        time = in.readString();
        recycleItemCats = in.createStringArrayList();
        remarks = in.readString();
        status = in.readString();
    }

    public static final Creator<RecycleRequest> CREATOR = new Creator<RecycleRequest>() {
        @Override
        public RecycleRequest createFromParcel(Parcel in) {
            return new RecycleRequest(in);
        }

        @Override
        public RecycleRequest[] newArray(int size) {
            return new RecycleRequest[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public ArrayList<String> getRecycleItemCats() {
        return recycleItemCats;
    }

    public void setRecycleItemCats(ArrayList<String> recycleItemCats) {
        this.recycleItemCats = recycleItemCats;
    }

    public String getRecycleItemCatsToString() {
        return recycleItemCats.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    public void setRecycleItemCats(String recycleItemCats) {
        String[] items = recycleItemCats.split(",");
        if(!this.recycleItemCats.isEmpty()) {
            this.recycleItemCats.clear();
        }
        this.recycleItemCats.addAll(Arrays.asList(items));
    }

    public void AddItem(String item) {
        recycleItemCats.add(item);
    }

    public void RemoveItem(String item) {
        recycleItemCats.remove(item);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userid);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeStringList(recycleItemCats);
        parcel.writeString(remarks);
        parcel.writeString(status);
    }
}
