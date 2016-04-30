package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

public class Observation implements Parcelable, Entity, Serializable {


    public final static transient int ID_LENGTH = 23;
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
    private String id;
    @SerializedName("name")
    private String tickName;
    private String species;
    @SerializedName("tick_image")
    private String imageUrl;
    private String geo_location;
    private int num_of_ticks;
    private String description;
    private long timestamp, updated_at;
    @SerializedName("lati")
    private double latitude;
    @SerializedName("longi")
    private double longitude;
    // references
    private String user_id;
    private String activity_id;
    private String tick_id;
    //
    private EntityLocation location;
    // flag for storage
    private transient boolean isOnCloud;
    private boolean verified;

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
    public Observation(String tickName, int num_ticks, String description, long timestamp, String activity_id, String user_id) {
        this.tickName = tickName;
        this.num_of_ticks = num_ticks;
        this.timestamp = timestamp;
        this.activity_id = activity_id;
        this.user_id = user_id;
        this.description = description;
    }

    protected Observation(Parcel in) {
        id = in.readString();
        tickName = in.readString();
        species = in.readString();
        imageUrl = in.readString();
        geo_location = in.readString();
        num_of_ticks = in.readInt();
        description = in.readString();
        timestamp = in.readLong();
        updated_at = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        user_id = in.readString();
        activity_id = in.readString();
        tick_id = in.readString();
        location = in.readParcelable(EntityLocation.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Observation)) return false;
        Observation that = (Observation) o;
        return getNum_of_ticks() == that.getNum_of_ticks() &&
                getTimestamp() == that.getTimestamp() &&
                getUpdated_at() == that.getUpdated_at() &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTickName(), that.getTickName()) &&
                Objects.equals(getSpecies(), that.getSpecies()) &&
                Objects.equals(getImageUrl(), that.getImageUrl()) &&
                Objects.equals(getGeo_location(), that.getGeo_location()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getUser_id(), that.getUser_id()) &&
                Objects.equals(getActivity_id(), that.getActivity_id()) &&
                Objects.equals(getTick_id(), that.getTick_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTickName(), getSpecies(), getImageUrl(), getGeo_location(), getNum_of_ticks(), getDescription(), getTimestamp(), getUpdated_at(), getLatitude(), getLongitude(), getUser_id(), getActivity_id(), getTick_id());
    }

    public EntityLocation getLocation() {
        return location;
    }

    public void setLocation(EntityLocation location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setIsVerified(boolean isVerified) {
        this.verified = isVerified;
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
                ":" + description +
                ":" + timestamp +
                ":" + latitude +
                ":" + longitude +
                ":" + geo_location +
                ":" + imageUrl +
                ":" + updated_at +
                ":" + user_id +
                ":" + activity_id;
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
        dest.writeString(imageUrl);
        dest.writeString(geo_location);
        dest.writeInt(num_of_ticks);
        dest.writeString(description);
        dest.writeLong(timestamp);
        dest.writeLong(updated_at);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(user_id);
        dest.writeString(activity_id);
        dest.writeString(tick_id);
        dest.writeParcelable(location, flags);
    }
}
