package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * <tt>Location </tt> contains the latitude and longitude of the user whenever the user
 * is on the ongoing {@link Activity}. By making use of Location, we can develop a trajectory
 * of each activity and the possible existence of the ticks in the proximity of activity.
 * This way {@link Location} helps to detect the presence of ticks and {@link User}'s path.
 * </p>
 * <p>
 * Location can also be built using the Factory design pattern.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class Location {

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

    static public Location createLocationFactory(JSONObject jsonObject) throws JSONException {
        Location location = new Location();

        return location;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getFk_userid() {
        return fk_userid;
    }

    public void setFk_userid(int fk_userid) {
        this.fk_userid = fk_userid;
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
        return "Location{" +
                "location_id=" + location_id +
                ", fk_userid=" + fk_userid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                '}';
    }
}
