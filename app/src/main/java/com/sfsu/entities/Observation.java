package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>Observation</tt> defines tick related data created by the {@link User}. The {@link Observation} specifies the process of
 * capturing the tick and posting the data on the central server. Each observation contains tick image and specifying the
 * details(if known) and then posting the data on the server.
 * </p>
 * Observation holds reference to the {@link Tick} and {@link EntityLocation} for a each Observation captured by the User.
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

    private String id;
    private String geoLocation;
    private String tickName;
    private String imageUrl;
    private int num_ticks;
    private long timestamp, created_at, updated_at;

    @SerializedName("location")
    private EntityLocation locationObj;

    @SerializedName("tick")
    private Tick tickObj;

    private String requestToken, user_id;

    // REQUIRED : Default Constructor
    public Observation() {
    }

    /**
     * IMP -  Constructor Overloading to create the Observation object while retrieving the data from the Server via Retrofit.
     *
     * @param observation_id - Observation ID of the recorded Observation.
     * @param tickObj        - The Tick Object retrieved from the JSON data
     * @param locationObj    - EntityLocation object of the Observation.
     * @param imageUrl       - ImageUrl of the Image on the Server.
     * @param geoLocation    - The area - feature name of the Location
     * @param num_ticks      - Total number of Ticks during this Observation.
     * @param timestamp      - The Timestamp during which the Observation was recorded.
     * @param created_at     - Timestamp when the Observation entry was recorded on the Server
     * @param updated_at     - Timestamp when the Observation entry was updated on the Server
     */
    public Observation(String id, Tick tickObj, EntityLocation locationObj, String imageUrl, String geoLocation,
                       int num_ticks, long timestamp, long created_at, long updated_at) {
        this.id = id;
        this.geoLocation = geoLocation;
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tickObj = tickObj;
        this.locationObj = locationObj;
        this.imageUrl = imageUrl;
    }

    /**
     * IMP : Constructor overloading to create the Observation object for sending it over to Server via Retrofit.
     * TODO: Add UserId to the Constructor.
     *
     * @param imageUrl    - Image of Tick captured by the User for this Observation
     * @param tickName    -  Name of the Tick captured by the User
     * @param num_ticks   - Total number of Ticks found in this  Observation
     * @param timestamp   - Current Timestamp of the Observation.
     * @param locationObj - Location captured by the Android Device for the Observation
     */
    public Observation(String imageUrl, String tickName, int num_ticks, long timestamp, EntityLocation locationObj, String
            requestToken, String user_id) {
        this.imageUrl = imageUrl;
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.locationObj = locationObj;
        this.tickName = tickName;
        this.requestToken = requestToken;
        this.user_id = user_id;
    }

    // constructor for demo purposes
    public Observation(String tickName, String geoLocation, long timestamp) {
        this.geoLocation = geoLocation;
        this.timestamp = timestamp;
    }

    protected Observation(Parcel in) {
        id = in.readString();
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

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
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

    public EntityLocation getLocationObj() {
        return locationObj;
    }

    public void setLocationObj(EntityLocation locationObj) {
        this.locationObj = locationObj;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
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
    public String getGeneralName() {
        return "Observations";
    }

    @Override
    public String getResourceType() {
        return "observations";
    }
}
