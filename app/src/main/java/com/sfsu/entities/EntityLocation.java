package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * <tt>EntityLocation </tt> consists of the current latitude and longitude of the Entity(Account, Tick) whenever the user
 * has an ongoing {@link Activities}. It is used to determine the Location of Account for its ongoing Activity as well
 * as the Location of the Tick when the Observation is made by the Account.
 * By making use of EntityLocation, a trajectory of each {@link Activities} of Account is developed. It also helps to possibly
 * determine the existence of the ticks in the proximity of user's activity.
 * </p>
 * <p>
 * This way {@link EntityLocation} helps to detect the presence of {@link Tick} and also determine {@link Account}'s path.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class EntityLocation implements Parcelable, Entity {

    public static final Creator<EntityLocation> CREATOR = new Creator<EntityLocation>() {
        @Override
        public EntityLocation createFromParcel(Parcel in) {
            return new EntityLocation(in);
        }

        @Override
        public EntityLocation[] newArray(int size) {
            return new EntityLocation[size];
        }
    };

    private String id;
    private double latitude, longitude;
    private long timestamp;
    private String observation_id;
    private String activity_id;
    private String user_id;

    public EntityLocation() {
    }

    /**
     * Constructor overloading to create a EntityLocation object to send it to server
     *
     * @param latitude
     * @param longitude
     * @param timestamp
     */
    public EntityLocation(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    /**
     * Constructor overloading to set Account's Location and display it on Google Maps as well as build a Composite Observation
     * Object to parse it to JSON and send it over to Server.
     *
     * @param latitude
     * @param longitude
     */
    public EntityLocation(double latitude, double longitude, String activity_id, String user_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity_id = activity_id;
        this.user_id = user_id;
    }

    /**
     * Constructor overloading for building the {@link EntityLocation} object from Response.
     *
     * @param id
     * @param latitude
     * @param longitude
     * @param timestamp
     * @param observation_id
     * @param activity_id
     * @param user_id
     */
    public EntityLocation(String id, double latitude, double longitude, long timestamp, String observation_id, String activity_id, String user_id) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.observation_id = observation_id;
        this.activity_id = activity_id;
        this.user_id = user_id;
    }

    protected EntityLocation(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        activity_id = in.readString();
        observation_id = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
    }

    /**
     * factory design pattern to create the EntityLocation object.
     *
     * @return
     */
    public static EntityLocation createUserLocation(double latitude, double longitude, long timestamp) {
        return new EntityLocation(latitude, longitude, timestamp);
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getObservation_id() {
        return observation_id;
    }

    public void setObservation_id(String observation_id) {
        this.observation_id = observation_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return id +
                ":" + latitude +
                ":" + longitude +
                ":" + timestamp +
                ":" + observation_id +
                ":" + activity_id +
                ":" + user_id;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(user_id);
        parcel.writeString(activity_id);
        parcel.writeString(observation_id);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeLong(timestamp);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getGeneralName() {
        return "EntityLocation";
    }

    @Override
    public String getResourceType() {
        return "locations";
    }


}
