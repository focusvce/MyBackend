package com.hybrid.mybackend;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by monster on 6/5/16.
 */
public class NewsFeed implements Parcelable{

    private int id;
    private int up;
    private int down;
    private int comment_count;
    private String user_name ;
    private String link;
    private String title;
    private String description;
    private Date dop;
    private String user_pic;
    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public NewsFeed(){

    }

    public NewsFeed(Parcel input){

        id = input.readInt();
        up = input.readInt();
        down = input.readInt();
        comment_count = input.readInt();
        user_name = input.readString();
        link = input.readString();
        title = input.readString();
        description = input.readString();
        long dateMillis = input.readLong();
        dop = new Date(dateMillis);
        user_pic = input.readString();
        uid = input.readInt();

    }

    public NewsFeed(int comment_count, Date dop, String description, int down, int id, String link, String title, int up, String user_name,String user_pic) {
        this.comment_count = comment_count;
        this.dop = dop;
        this.description = description;
        this.down = down;
        this.id = id;
        this.link = link;
        this.title = title;
        this.up = up;
        this.user_name = user_name;
        this.user_pic = user_pic;
    }

    public Date getDop() {
        return dop;
    }

    public void setDop(Date dop) {
        this.dop = dop;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return id + " " + up + " " + down + " " + comment_count + " " + title + " " + link + " " + description + " " + dop + "\n \n ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(up);
        parcel.writeInt(down);
        parcel.writeInt(comment_count);
        parcel.writeString(user_name);
        parcel.writeString(link);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(dop.getTime());
        parcel.writeString(user_pic);
        parcel.writeInt(uid);

    }

    public static final Parcelable.Creator<NewsFeed> CREATOR
            = new Parcelable.Creator<NewsFeed>() {
        public NewsFeed createFromParcel(Parcel in) {
            return new NewsFeed(in);
        }

        public NewsFeed[] newArray(int size) {
            return new NewsFeed[size];
        }
    };
}
