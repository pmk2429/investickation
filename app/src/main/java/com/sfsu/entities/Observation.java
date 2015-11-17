package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>Observation</tt> defines tick related data created by the {@link User}.
 * The {@link Observation} is the process of capturing the tick and posting the
 * data on the central server. Each observation observes a behavior of capturing tick
 * image and specifying thr details(if known) and then posting the data on the server.
 * </p>
 * <p>
 * The Observation provides a factory design pattern to create observations by collecting
 * the data from Ticks and Activities and posting the data on the server.
 * </p>
 * Observation holds reference to the Tick and Location captured by the User for a specific Observation.
 * Created by Pavitra on 5/19/2015.
 */

public class Observation implements Parcelable, Entity {


    public static final Creator<Observation> CREATOR = new Creator<Observation>() {
        @Override
        public Observation createFromParcel(Parcel in) {
            return new Observation(in);
        }

        @Override
        public Observation[] newArray(int size) {
            return new Observation[size];
        }
    };

    private long observation_id;
    private String geoLocation, tickName;
    private int num_ticks;
    private long timestamp, created_at, updated_at;
    private EntityLocation locationObj;
    private Tick tickObj;

    // REQUIRED : Default Constructor
    public Observation() {
    }

    // constructor to get the data from the Object.
    public Observation(long observation_id, Tick tickObj, EntityLocation locationObj, String geoLocation, int num_ticks, long
            timestamp, long created_at, long updated_at) {
        this.observation_id = observation_id;
        this.geoLocation = geoLocation;
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tickObj = tickObj;
        this.locationObj = locationObj;
    }

    /**
     * IMP : Constructor to create the Observation object for sending it over to Server.
     *
     * @param num_ticks
     * @param timestamp
     * @param location
     * @param tickObj
     */
    public Observation(Tick tickObj, int num_ticks, long timestamp, EntityLocation locationObj) {
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.locationObj = locationObj;
        this.tickObj = tickObj;
    }


    // constructor for demo purposes
    public Observation(String tickName, String geoLocation, long timestamp) {
        this.geoLocation = geoLocation;
        this.timestamp = timestamp;
    }


    protected Observation(Parcel in) {
        observation_id = in.readLong();
        geoLocation = in.readString();
        num_ticks = in.readInt();
        timestamp = in.readLong();
        created_at = in.readLong();
        updated_at = in.readLong();
        locationObj = in.readParcelable(EntityLocation.class.getClassLoader());
        tickObj = in.readParcelable(Tick.class.getClassLoader());
    }

    public static List<Observation> initializeData() {
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation("American Dog Tick", "Presidio of San Francisco", System.currentTimeMillis()));
        observations.add(new Observation("Spotted Red Tick", "Yosemite National Park", System.currentTimeMillis()));
        observations.add(new Observation("Deer Tick", "Lands End trail", System.currentTimeMillis()));
        return observations;
    }

    public EntityLocation getLocation() {
        return locationObj;
    }

    public void setLocation(EntityLocation location) {
        this.locationObj = location;
    }

    public Tick getTickObj() {
        return tickObj;
    }

    public void setTickObj(Tick tickObj) {
        this.tickObj = tickObj;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public long getObservation_id() {
        return observation_id;
    }

    public void setObservation_id(long observation_id) {
        this.observation_id = observation_id;
    }

    public int getNum_ticks() {
        return num_ticks;
    }

    public void setNum_ticks(int num_ticks) {
        this.num_ticks = num_ticks;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Observation{" +
                "observation_id=" + observation_id +
                ", num_ticks=" + num_ticks +
                ", timestamp=" + timestamp +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(observation_id);
        dest.writeString(geoLocation);
        dest.writeInt(num_ticks);
        dest.writeLong(timestamp);
        dest.writeLong(created_at);
        dest.writeLong(updated_at);
        dest.writeParcelable(locationObj, flags);
        dest.writeParcelable(tickObj, flags);
    }


    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "Observations";
    }

    @Override
    public String getResourceType() {
        return "observations";
    }
}
