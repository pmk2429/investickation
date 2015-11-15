package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * <tt>UserLocation </tt> contains the latitude and longitude of the user whenever the user
 * is on the ongoing {@link Activities}. By making use of UserLocation, we can develop a trajectory
 * of each activity and the possible existence of the ticks in the proximity of activity.
 * This way {@link UserLocation} helps to detect the presence of ticks and {@link User}'s path.
 * </p>
 * <p>
 * UserLocation can also be built using the Factory design pattern.
 * </p>
 * <p/>
 * Created by Pavitra on 5/19/2015.
 */
public class UserLocation implements Parcelable, Entity {

    public static final Creator<UserLocation> CREATOR = new Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };
    private int location_id;
    private int fk_userid;
    private double latitude, longitude;
    private long timestamp;

    public UserLocation() {
    }

    /**
     * Constructor to create a UserLocation object from the params
     *
     * @param latitude
     * @param longitude
     * @param timestamp
     */
    private UserLocation(double latitude, double longitude, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    /**
     * Constructor to create the Location object while retrieving the data from the Server.
     *
     * @param location_id
     * @param fk_userid
     * @param latitude
     * @param longitude
     * @param timestamp
     */
    public UserLocation(int location_id, int fk_userid, double latitude, double longitude, long timestamp) {
        this.location_id = location_id;
        this.fk_userid = fk_userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    protected UserLocation(Parcel in) {
        location_id = in.readInt();
        fk_userid = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
    }

    /**
     * factory design pattern to create the UserLocation object.
     *
     * @return
     */
    public static UserLocation createUserLocation(double latitude, double longitude, long timestamp) {
        return new UserLocation(latitude, longitude, timestamp);
    }

    public int getLocation_id() {
        return location_id;
    }


    public int getFk_userid() {
        return fk_userid;
    }


    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    public long getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "UserLocation{" +
                "location_id=" + location_id +
                ", fk_userid=" + fk_userid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(location_id);
        parcel.writeInt(fk_userid);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeLong(timestamp);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "UserLocation";
    }

    @Override
    public String getResourceType() {
        return "locations";
    }


}
