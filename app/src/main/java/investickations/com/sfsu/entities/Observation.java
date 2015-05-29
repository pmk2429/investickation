package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

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
public class Observation {

    private int observation_id;
    private int num_ticks;
    private String tickImageUrl;
    private double latitude, longitude;
    private long timestamp, created_at, updated_at;

    public Observation() {
    }

    public Observation(int observation_id, int num_ticks, String tickImageUrl, double latitude, double longitude, long timestamp, long created_at, long updated_at) {
        this.observation_id = observation_id;
        this.num_ticks = num_ticks;
        this.tickImageUrl = tickImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    static public Observation createObservationFactory(JSONObject jsonObject) throws JSONException {
        Observation observation = new Observation();

        return observation;
    }

    public int getObservation_id() {
        return observation_id;
    }

    public void setObservation_id(int observation_id) {
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
}
