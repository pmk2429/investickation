package investickations.com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * <tt>Activity</tt> refers to the state of the user where the User is performing
 * an action in progress. The example of such activities can be walking, running,
 * dog walking, hiking etc. The Activity class provides all the basic behaviors and
 * methods to define an activity.
 * </p>
 * <p>
 * Activity also defines the Factory design pattern to create activities using
 * creational design pattern by abstracting the logic from user.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class Activity implements Parcelable {

    private int activity_id;
    private String name, imageUrl, location_area;
    private int num_people, num_pets, num_ticks;
    private long timestamp, created_at, update_at;

    public Activity() {
    }

    public Activity(int activity_id, String name, String imageUrl, String location_area, int num_people, int num_pets, int num_ticks, long timestamp, long created_at, long update_at) {
        this.activity_id = activity_id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.location_area = location_area;
        this.num_people = num_people;
        this.num_pets = num_pets;
        this.num_ticks = num_ticks;
        this.timestamp = timestamp;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    static public Activity createActivityFactory(JSONObject jsonObject) throws JSONException {
        Activity activity = new Activity();

        return activity;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation_area() {
        return location_area;
    }

    public void setLocation_area(String location_area) {
        this.location_area = location_area;
    }

    public int getNum_people() {
        return num_people;
    }

    public void setNum_people(int num_people) {
        this.num_people = num_people;
    }

    public int getNum_pets() {
        return num_pets;
    }

    public void setNum_pets(int num_pets) {
        this.num_pets = num_pets;
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

    public long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(long update_at) {
        this.update_at = update_at;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activity_id=" + activity_id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", location_area='" + location_area + '\'' +
                ", num_people=" + num_people +
                ", num_pets=" + num_pets +
                ", num_ticks=" + num_ticks +
                ", timestamp=" + timestamp +
                ", created_at=" + created_at +
                ", update_at=" + update_at +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
