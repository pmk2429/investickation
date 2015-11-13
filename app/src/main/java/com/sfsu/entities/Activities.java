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
 * <p/>
 * In addition to the properties possessed by the Activities, it also references the current User to know which User is logged in
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class Activities implements Parcelable, Entity {

    public static final Creator<Activities> CREATOR = new Creator<Activities>() {
        @Override
        public Activities createFromParcel(Parcel in) {
            return new Activities(in);
        }

        @Override
        public Activities[] newArray(int size) {
            return new Activities[size];
        }
    };
    private int activity_id;
    private String activityName, location_area;
    private int num_people, num_pets;
    private long timestamp, created_at, update_at;
    private long fk_user_id;
    // Enum identifier for setting State of Object.
    private STATE activityState;

    // Default constructor -> REQUIRED
    public Activities() {
    }


    /**
     * Constructor for creating the Model object to send it over to retrofit for storing on Server.
     *
     * @param name
     * @param num_people
     * @param num_pets
     * @param timestamp
     */
    public Activities(String name, int num_people, int num_pets, long timestamp) {
        this.activityName = name;
        this.num_people = num_people;
        this.num_pets = num_pets;
        this.timestamp = timestamp;
    }


    /**
     * Constructor for DEMO purpose.
     *
     * @param name
     * @param num_people
     * @param num_pets
     * @param num_ticks
     */
    public Activities(String name, int num_people, int num_pets) {
        this.activityName = name;
        this.num_people = num_people;
        this.num_pets = num_pets;
    }

    protected Activities(Parcel in) {
        activity_id = in.readInt();
        activityName = in.readString();
        location_area = in.readString();
        num_people = in.readInt();
        num_pets = in.readInt();
        timestamp = in.readLong();
        created_at = in.readLong();
        update_at = in.readLong();
    }

    /**
     * Constructor to create the Retrofit Model and pass it over to RetrofitController.
     *
     * @param name
     * @param location_area
     * @param num_people
     * @param num_pets
     * @param num_ticks
     * @param timestamp
     * @param created_at
     * @param update_at
     */
    public Activities(String name, String location_area, int num_people, int num_pets, long timestamp, long created_at, long update_at) {
        this.activityName = name;
        this.location_area = location_area;
        this.num_people = num_people;
        this.num_pets = num_pets;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    public static List<Activities> initializeData() {
        List<Activities> activities = new ArrayList<>();
        activities.add(new Activities("Golden Gate Park", 5, 1));
        activities.add(new Activities("SF Presidio", 0, 0));
        activities.add(new Activities("Yosemite", 8, 0));
        return activities;
    }

    /**
     * Method to set the State of current ongoing Activity
     *
     * @param state
     */
    public void setState(STATE state) {
        this.activityState = state;
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

    public String getLocation_area() {
        return location_area;
    }

    public int getNum_people() {
        return num_people;
    }

    public int getNum_pets() {
        return num_pets;
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
                ", location_area='" + location_area + '\'' +
                ", num_people=" + num_people +
                ", num_pets=" + num_pets +
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
        parcel.writeInt(activity_id);
        parcel.writeString(activityName);
        parcel.writeString(location_area);
        parcel.writeInt(num_people);
        parcel.writeInt(num_pets);
        parcel.writeLong(timestamp);
        parcel.writeLong(created_at);
        parcel.writeLong(update_at);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "Activity";
    }


    /**
     * A list of constant values assigned to Activities. Started represents the
     */
    public static enum STATE {
        RUNNING, CREATED;
    }
}
