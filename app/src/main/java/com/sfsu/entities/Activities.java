package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>Activity</tt> refers to the state of the user where the User is performing
 * an action in progress. The example of such activities can be walking, running,
 * dog walking, hiking etc. The Activity class provides all the basic behaviors and
 * methods to define an activity.
 * </p>
 * <p>
 * Activity also defines the Factory design pattern to create activities using
 * creational design pattern by abstracting the logic from user.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class Activities implements Parcelable, Entity {

    private int activity_id;
    private String activityName, imageUrl, location_area;
    private int num_people, num_pets, num_ticks;
    private long timestamp, created_at, update_at;

    public Activities() {
    }

    public Activities(String name, int num_people, int num_pets, int num_ticks) {
        this.activityName = name;
        this.num_people = num_people;
        this.num_pets = num_pets;
        this.num_ticks = num_ticks;
    }

    public Activities(int activity_id, String name, String imageUrl, String location_area, int num_people, int num_pets, int num_ticks, long timestamp, long created_at, long update_at) {
        this.activity_id = activity_id;
        this.activityName = name;
        this.imageUrl = imageUrl;
        this.location_area = location_area;
        this.num_people = num_people;
        this.num_pets = num_pets;
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    public static List<Activities> initializeData() {
        List<Activities> activities = new ArrayList<>();
        activities.add(new Activities("Golden Gate Park", 5, 1, 8));
        activities.add(new Activities("SF Presidio", 0, 0, 4));
        activities.add(new Activities("Yosemite", 8, 0, 11));
        return activities;
    }


    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocation_area() {
        return location_area;
    }

    public int getNum_people() {
        return num_people;
    }

    public int getNum_pets() {
        return num_pets;
    }

    public int getNum_ticks() {
        return num_ticks;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getCreated_at() {
        return created_at;
    }

    public long getUpdate_at() {
        return update_at;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activity_id=" + activity_id +
                ", name='" + activityName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location_area='" + location_area + '\'' +
                ", num_people=" + num_people +
                ", num_pets=" + num_pets +
                ", num_ticks=" + num_ticks +
                ", timestamp=" + timestamp +
                ", created_at=" + created_at +
                ", update_at=" + update_at +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "Activity";
    }

    @Override
    public String getJSONResourceIdentifier() {
        return "activities";
    }
}
