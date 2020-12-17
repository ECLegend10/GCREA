package com.example.gcrea;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String idNo;
    private String role;
    private String position;
    private String course;
    private String email;
    private String contactNo;
    private String dateJoined;
    private int points;
    private Bitmap profilePic;

    public User(String name, String idNo, String role, String position, String course, String email, String contactNo, String dateJoined, int points, Bitmap profilePic) {
        this.id = 0;
        this.name = name;
        this.idNo = idNo;
        this.role = role;
        this.position = position;
        this.course = course;
        this.email = email;
        this.contactNo = contactNo;
        this.dateJoined = dateJoined;
        this.points = points;
        this.profilePic = profilePic;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        idNo = in.readString();
        role = in.readString();
        position = in.readString();
        course = in.readString();
        email = in.readString();
        contactNo = in.readString();
        dateJoined = in.readString();
        points = in.readInt();
        //profilePic = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(idNo);
        parcel.writeString(role);
        parcel.writeString(position);
        parcel.writeString(course);
        parcel.writeString(email);
        parcel.writeString(contactNo);
        parcel.writeString(dateJoined);
        parcel.writeInt(points);
        //parcel.writeParcelable(profilePic, i);
    }
}
