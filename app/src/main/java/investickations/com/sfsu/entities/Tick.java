package investickations.com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * <tt>Tick </tt> provides a default behavior of the Tick entity. The Tick class contains
 * all the getters and setters for the Tick interface and provides a toString() method.
 * </p>
 * <p>
 * The Tick class also provides a Factory design pattern implementation called createTickFactory()
 * to create Ticks by abstracting the implementation logic from the user.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */


public class Tick implements Parcelable, Entity {

    private int tick_id;
    private String name, species, color, known_for, descripition, imageUrl;
    private long created_at, updated_at;

    public Tick() {
    }

    public Tick(int tick_id, String name, String species, String color, String known_for, String descripition, String imageUrl) {
        this.tick_id = tick_id;
        this.name = name;
        this.species = species;
        this.color = color;
        this.known_for = known_for;
        this.descripition = descripition;
        this.imageUrl = imageUrl;
    }

    static public Tick createTickFactory(JSONObject jsonObject) throws JSONException {
        Tick tick = new Tick();

        return tick;
    }

    public int getTick_id() {
        return tick_id;
    }

    public void setTick_id(int tick_id) {
        this.tick_id = tick_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKnown_for() {
        return known_for;
    }

    public void setKnown_for(String known_for) {
        this.known_for = known_for;
    }

    public String getDescripition() {
        return descripition;
    }

    public void setDescripition(String descripition) {
        this.descripition = descripition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        return "Tick{" +
                "tick_id=" + tick_id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", color='" + color + '\'' +
                ", known_for='" + known_for + '\'' +
                ", descripition='" + descripition + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
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

    }

    @Override
    public Entity getEntity() {
        return this;
    }
}
