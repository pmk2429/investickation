package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * <tt>EntityLocation </tt> consists of the current latitude and longitude of the Entity(User, Tick) whenever the user
 * has an ongoing {@link Activities}. It is used to determine the Location of User for its ongoing Activity as well
 * as the Location of the Tick when the Observation is made by the User.
 * By making use of EntityLocation, a trajectory of each {@link Activities} of User is developed. It also helps to possibly
 * determine the existence of the ticks in the proximity of user's activity.
 * </p>
 * <p>
 * This way {@link EntityLocation} helps to detect the presence of {@link Tick} and also determine {@link User}'s path.
 * </p>
 * <p/>
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
    private int location_id;
    private int fk_userid;
    private double latitude, longitude;
    private long timestamp;

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
     * Constructor overloading to set User's Location and display it on Google Maps as well as build a Composite Observation
     * Object to parse it to JSON and send it over to Server.
     *
     * @param latitude
     * @param longitude
     */
    public EntityLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * IMP : Constructor to create the Location object while retrieving the data from the Server.
     *
     * @param location_id
     * @param fk_userid
     * @param latitude
     * @param longitude
     * @param timestamp
     */
    public EntityLocation(int location_id, int fk_userid, double latitude, double longitude, long timestamp) {
        this.location_id = location_id;
        this.fk_userid = fk_userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    protected EntityLocation(Parcel in) {
        location_id = in.readInt();
        fk_userid = in.readInt();
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
        return "EntityLocation{" +
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
        return "EntityLocation";
    }

    @Override
    public String getResourceType() {
        return "locations";
    }


}