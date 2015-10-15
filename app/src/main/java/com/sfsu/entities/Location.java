package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * <p>
 * <tt>Location </tt> contains the latitude and longitude of the user whenever the user
 * is on the ongoing {@link Activities}. By making use of Location, we can develop a trajectory
 * of each activity and the possible existence of the ticks in the proximity of activity.
 * This way {@link Location} helps to detect the presence of ticks and {@link User}'s path.
 * </p>
 * <p>
 * Location can also be built using the Factory design pattern.
 * </p>
 * <p/>
 * Created by Pavitra on 5/19/2015.
 */
public class Location implements Parcelable, Entity {

    private int location_id;
    private int fk_userid;
    private double latitude, longitude;
    private long timestamp;

    public Location() {
    }

    public Location(int location_id, int fk_userid, double latitude, double longitude, long timestamp) {
        this.location_id = location_id;
        this.fk_userid = fk_userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }


    @Override
    public Entity createEntityFactory(JSONObject jsonObject) {

        Location location = new Location();
        return location;
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
        return "Location{" +
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

    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "Locations";
    }

    @Override
    public String getJSONResourceIdentifier() {
        return "locations";
    }


}
