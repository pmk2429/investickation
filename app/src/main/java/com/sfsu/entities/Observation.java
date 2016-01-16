package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * <tt>Observation</tt> defines tick related data created by the {@link Account}. The {@link Observation} specifies the process of
 * capturing the tick and posting the data on the central server. Each observation contains tick image and specifying the
 * details(if known) and then posting the data on the server.
 * </p>
 * Observation holds reference to the {@link EntityLocation} for a each Observation captured by the Account. When the user makes an
 * observation, the it also sends the <tt>latitude, longitude</tt> with each observation.
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

    public final static transient int ID_LENGTH = 23;

    private String id;
    @SerializedName("name")
    private String tickName;
    private String species;
    @SerializedName("tick_image")
    private String imageUrl;
    private String geo_location;
    private int num_of_ticks;
    private long timestamp, updated_at;
    private double latitude, longitude;
    // references
    private String user_id;
    private String activity_id;
    private String tick_id;
    // flag for storage
    private transient boolean isOnCloud;
    private transient boolean isVerified;

    // REQUIRED : Default Constructor
    public Observation() {
    }

    /**
     * Constructor overloading to create the Observation object for sending it over to Server via Retrofit.
     *
     * @param tickName
     * @param species
     * @param num_ticks
     * @param timestamp
     * @param latitude
     * @param longitude
     * @param activity_id
     * @param user_id
     */
    public Observation(String tickName, String species, int num_ticks, long timestamp, String activity_id, String user_id) {
        this.tickName = tickName;
        this.species = species;
        this.num_of_ticks = num_ticks;
        this.timestamp = timestamp;
        this.activity_id = activity_id;
        this.user_id = user_id;
    }

    protected Observation(Parcel in) {
        id = in.readString();
        tickName = in.readString();
        species = in.readString();
        geo_location = in.readString();
        num_of_ticks = in.readInt();
        timestamp = in.readLong();
        updated_at = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public boolean isOnCloud() {
        return isOnCloud;
    }

    public void setIsOnCloud(boolean isOnCloud) {
        this.isOnCloud = isOnCloud;
    }

    public String getGeo_location() {
        return geo_location;
    }

    public void setGeo_location(String geo_location) {
        this.geo_location = geo_location;
    }

    public int getNum_of_ticks() {
        return num_of_ticks;
    }

    public void setNum_of_ticks(int num_of_ticks) {
        this.num_of_ticks = num_of_ticks;
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

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getTick_id() {
        return tick_id;
    }

    public void setTick_id(String tick_id) {
        this.tick_id = tick_id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getGeoLocation() {
        return geo_location;
    }

    public void setGeoLocation(String geoLocation) {
        this.geo_location = geoLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTickName() {
        return tickName;
    }

    public void setTickName(String tickName) {
        this.tickName = tickName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getNum_ticks() {
        return num_of_ticks;
    }

    public void setNum_ticks(int num_ticks) {
        this.num_of_ticks = num_ticks;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tickName);
        dest.writeString(species);
        dest.writeString(geo_location);
        dest.writeString(imageUrl);
        dest.writeInt(num_of_ticks);
        dest.writeLong(timestamp);
        dest.writeLong(updated_at);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }


    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getGeneralName() {
        return "Observations";
    }

    @Override
    public String getResourceType() {
        return "observations";
    }

    @Override
    public String toString() {
        return id +
                ":" + tickName +
                ":" + species +
                ":" + num_of_ticks +
                ":" + timestamp +
                ":" + latitude +
                ":" + longitude +
                ":" + geo_location +
                ":" + imageUrl +
                ":" + updated_at +
                ":" + user_id +
                ":" + activity_id;
    }
}
