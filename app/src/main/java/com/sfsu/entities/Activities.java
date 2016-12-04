package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * <p>
 * <tt>Activity</tt> refers to the state of the user where the Account is performing
 * an action in progress. The example of such activities can be walking, running,
 * dog walking, hiking etc. The Activity class provides all the basic behaviors and
 * methods to define an activity.
 * </p>
 * <p>
 * Activity also defines the Factory design pattern to create activities using
 * creational design pattern by abstracting the logic from user.
 * <p/>
 * In addition to the properties possessed by the Activities, it also references the current Account to know which Account is logged in
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class Activities implements Entity, Parcelable {

    public final static transient int ID_LENGTH = 23;
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
    private int num_of_people;
    private int num_of_pets;
    private int num_of_ticks;
    private int distance;
    private String location_area;
    private long timestamp, updated_at;
    private String user_id;
    private String image_url;
    // Enum identifier for setting State of Object.
    private transient STATE activityState;
    private transient boolean isOnCloud;

    public Activities() {
    }

    /**
     * Constructor overloading for creating the Activities Model and sending the object to ActivityRunningFragment fragment.
     *
     * @param name
     * @param num_people
     * @param num_pets
     * @param timestamp
     */
    public Activities(String name, int num_of_people, int num_pets, long timestamp, String user_id) {
        this.activityName = name;
        this.num_of_people = num_of_people;
        this.num_of_pets = num_pets;
        this.timestamp = timestamp;
        this.user_id = user_id;
    }

    /**
     * Constructor overloading for creating {@link Activities} instance. This instance will hold all the attributes of an
     * Activity.
     *
     * @param name
     * @param location_area
     * @param num_of_people
     * @param num_pets
     * @param image_url
     * @param distance
     * @param timestamp
     * @param created_at
     * @param updated_at
     */
    public Activities(String name, String location_area, int num_of_people, int num_pets, String image_url, int distance, long
            timestamp, long updated_at) {
        this.activityName = name;
        this.location_area = location_area;
        this.num_of_people = num_of_people;
        this.num_of_pets = num_pets;
        this.distance = distance;
        this.image_url = image_url;
        this.timestamp = timestamp;
        this.updated_at = updated_at;
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
        num_of_people = in.readInt();
        num_of_pets = in.readInt();
        num_of_ticks = in.readInt();
        distance = in.readInt();
        location_area = in.readString();
        timestamp = in.readLong();
        updated_at = in.readLong();
        user_id = in.readString();
        image_url = in.readString();
    }

    public boolean isOnCloud() {
        return isOnCloud;
    }

    public void setIsOnCloud(boolean isOnCloud) {
        this.isOnCloud = isOnCloud;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getNum_of_people() {
        return num_of_people;
    }

    public void setNum_of_people(int num_of_people) {
        this.num_of_people = num_of_people;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                num_of_ticks + " : " + image_url + " : " + timestamp + " : " + updated_at + " : " + user_id + "-> " +
                activityState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(activityName);
        dest.writeInt(num_of_people);
        dest.writeInt(num_of_pets);
        dest.writeInt(num_of_ticks);
        dest.writeInt(distance);
        dest.writeString(location_area);
        dest.writeLong(timestamp);
        dest.writeLong(updated_at);
        dest.writeString(user_id);
        dest.writeString(image_url);
    }

    /**
     * A list of constant values assigned to Activities. Started represents the
     */
    public static enum STATE {
        RUNNING,
        CREATED;
    }
}
