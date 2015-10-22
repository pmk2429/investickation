package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>Observation</tt> defines individual observations created by the {@link User}.
 * The {@link Observation} is the process of capturing the tick and posting the
 * data on the central server. Each observation observes a behavior of capturing tick
 * image and specifying thr details(if known) and then posting the data on the server.
 * </p>
 * <p>
 * The Observation provides a factory design pattern to create observations by collecting
 * the data from Ticks and Activities and posting the data on the server.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */

// TODO: does this class require Tick and geoLocation object??? Creating a Custom object.
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
    private String tickName, tickSpecies, geoLocation, tickImageUrl;
    private int num_ticks;
    private double latitude, longitude;
    private long timestamp, created_at, updated_at;

    // constructor to get the data from the Object.
    public Observation(long observation_id, String tickName, String tickSpecies, String geoLocation, String tickImageUrl, int num_ticks, double latitude, double longitude, long timestamp, long created_at, long updated_at) {
        this.observation_id = observation_id;
        this.tickName = tickName;
        this.tickSpecies = tickSpecies;
        this.geoLocation = geoLocation;
        this.tickImageUrl = tickImageUrl;
        this.num_ticks = num_ticks;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Default Constructor
    public Observation() {
    }

    // constructor for demo purposes
    public Observation(String tickName, String geoLocation, long timestamp) {
        this.tickName = tickName;
        this.geoLocation = geoLocation;
        this.timestamp = timestamp;
    }


    // Constructor to create the Observation object.
    public Observation(String tickName, String tickSpecies, String geoLocation, String tickImageUrl, double latitude, double
            longitude, long timestamp) {
        this.tickName = tickName;
        this.geoLocation = geoLocation;
        this.tickImageUrl = tickImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.tickSpecies = tickSpecies;
    }

    protected Observation(Parcel in) {
        tickName = in.readString();
        geoLocation = in.readString();
        observation_id = in.readLong();
        num_ticks = in.readInt();
        tickImageUrl = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
        created_at = in.readLong();
        updated_at = in.readLong();
    }

    public static List<Observation> initializeData() {
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation("American Dog Tick", "Presidio of San Francisco", System.currentTimeMillis()));
        observations.add(new Observation("Spotted Red Tick", "Yosemite National Park", System.currentTimeMillis()));
        observations.add(new Observation("Deer Tick", "Lands End trail", System.currentTimeMillis()));
        return observations;
    }

    // Factory method to return the Observation object.
    public static Observation createObservation(String tickName, String tickSpecies, String geoLocation, String imageUrl,
                                                double latitude, double longitude, long timestamp) {

        return new Observation(tickName, tickSpecies, geoLocation, imageUrl, latitude, longitude, timestamp);
    }

    public String getTickName() {
        return tickName;
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

    public String getTickImageUrl() {
        return tickImageUrl;
    }

    public void setTickImageUrl(String tickImageUrl) {
        this.tickImageUrl = tickImageUrl;
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
                ", tickImageUrl='" + tickImageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tickName);
        parcel.writeString(geoLocation);
        parcel.writeLong(observation_id);
        parcel.writeInt(num_ticks);
        parcel.writeString(tickImageUrl);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeLong(timestamp);
        parcel.writeLong(created_at);
        parcel.writeLong(updated_at);
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
    public String getJSONResourceIdentifier() {
        return "observations";
    }

    @Override
    public String getResourceType() {
        return AppConfig.OBSERVATION_RESOURCE;
    }
}
