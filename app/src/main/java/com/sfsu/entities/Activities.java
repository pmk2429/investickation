package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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

    private String id;
    @SerializedName("name")
    private String activityName;
    private int num_of_people, num_of_pets, num_of_ticks;
    private String location_area;
    private long timestamp1, created_at1, updated_at1;
    private String user_id;
    // Enum identifier for setting State of Object.
    private STATE activityState;
    private String UUID;
    // Default constructor -> REQUIRED
    public Activities() {
    }
    /**
     * Constructor overloading for creating the Activities Model and sending the object to ActivityRunning fragment.
     *
     * @param name
     * @param num_people
     * @param num_pets
     * @param timestamp
     */
    public Activities(String name, int num_of_people, int num_pets, long timestamp, String UUID) {
        this.activityName = name;
        this.num_of_people = num_of_people;
        this.num_of_pets = num_pets;
        this.timestamp1 = timestamp;
        this.UUID = UUID;
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
    public Activities(String name, String location_area, int num_of_people, int num_pets, long timestamp, long created_at, long
            updated_at) {
        this.activityName = name;
        this.location_area = location_area;
        this.num_of_people = num_of_people;
        this.num_of_pets = num_pets;
        this.timestamp1 = timestamp;
        this.created_at1 = created_at;
        this.updated_at1 = updated_at;
    }

    /**
     * Constructor for DEMO purpose.
     *
     * @param name
     * @param num_people
     * @param num_pets
     * @param num_ticks
     */
    public Activities(String name, int num_of_people, int num_pets) {
        this.activityName = name;
        this.num_of_people = num_of_people;
        this.num_of_pets = num_pets;
    }

    protected Activities(Parcel in) {
        id = in.readString();
        activityName = in.readString();
        location_area = in.readString();
        num_of_people = in.readInt();
        num_of_pets = in.readInt();
        timestamp1 = in.readLong();
        created_at1 = in.readLong();
        updated_at1 = in.readLong();
    }

    public int getNum_of_ticks() {
        return num_of_ticks;
    }

    public void setNum_of_ticks(int num_of_ticks) {
        this.num_of_ticks = num_of_ticks;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum_of_pets() {
        return num_of_pets;
    }

    public void setNum_of_pets(int num_of_pets) {
        this.num_of_pets = num_of_pets;
    }

    public long getUpdated_at() {
        return updated_at1;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at1 = updated_at;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * Returns the state of the this Activity
     *
     * @param state
     */
    public STATE getState() {
        return activityState;
    }

    /**
     * Method to set the State of current ongoing Activity
     *
     * @param state
     */
    public void setState(STATE state) {
        this.activityState = state;
    }


    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getLocation_area() {
        return location_area;
    }

    public void setLocation_area(String location_area) {
        this.location_area = location_area;
    }

    public int getNum_people() {
        return num_of_people;
    }

    public void setNum_people(int num_people) {
        this.num_of_people = num_people;
    }


    public long getTimestamp() {
        return timestamp1;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp1 = timestamp;
    }

    public long getCreated_at() {
        return created_at1;
    }

    public void setCreated_at(long created_at) {
        this.created_at1 = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(activityName);
        parcel.writeString(location_area);
        parcel.writeInt(num_of_people);
        parcel.writeInt(num_of_pets);
        parcel.writeLong(timestamp1);
        parcel.writeLong(created_at1);
        parcel.writeLong(updated_at1);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getGeneralName() {
        return "Activity";
    }

    @Override
    public String getResourceType() {
        return "activities";
    }

    @Override
    public String toString() {
        return id + " : " + activityName + " : " + location_area + " : " + num_of_people + " : " + num_of_pets + " : " +
                num_of_ticks + " : " + timestamp1 + " : " + created_at1 + " : " + updated_at1 + " : " + user_id;
    }

    /**
     * A list of constant values assigned to Activities. Started represents the
     */
    public static enum STATE {
        RUNNING, CREATED;
    }
}
