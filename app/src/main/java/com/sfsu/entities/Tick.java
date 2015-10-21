package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    public static final Creator<Tick> CREATOR = new Creator<Tick>() {
        @Override
        public Tick createFromParcel(Parcel in) {
            return new Tick(in);
        }

        @Override
        public Tick[] newArray(int size) {
            return new Tick[size];
        }
    };

    private int tick_id;
    @SerializedName("tickName")
    private String tickName;
    @SerializedName("species")
    private String species;
    @SerializedName("color")
    private String color;
    @SerializedName("known_for")
    private String known_for;
    @SerializedName("description")
    private String description;
    @SerializedName("imageUrl")
    private String imageUrl;
    private long created_at, updated_at;

    public Tick() {
    }

    public Tick(String tickName, String description) {
        this.tickName = tickName;
        this.description = description;
    }

    public Tick(int tick_id, String name, String species, String color, String known_for, String description, String imageUrl) {
        this.tick_id = tick_id;
        this.tickName = name;
        this.species = species;
        this.color = color;
        this.known_for = known_for;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    protected Tick(Parcel in) {
        tick_id = in.readInt();
        tickName = in.readString();
        species = in.readString();
        color = in.readString();
        known_for = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        created_at = in.readLong();
        updated_at = in.readLong();
    }

    public static List<Tick> initializeData() {
        List<Tick> ticks = new ArrayList<>();
        ticks.add(new Tick("American Dog tick", "Found on Dogs"));
        ticks.add(new Tick("Spotted Tick", "Red colored with white spots"));
        ticks.add(new Tick("Jungle tick", "Dangerous species"));
        return ticks;
    }

    public int getTick_id() {
        return tick_id;
    }

    public void setTick_id(int tick_id) {
        this.tick_id = tick_id;
    }

    public String getTickName() {
        return tickName;
    }

    public void setTickName(String tickName) {
        this.tickName = tickName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", name='" + tickName + '\'' +
                ", species='" + species + '\'' +
                ", color='" + color + '\'' +
                ", known_for='" + known_for + '\'' +
                ", description='" + description + '\'' +
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
        parcel.writeInt(tick_id);
        parcel.writeString(tickName);
        parcel.writeString(species);
        parcel.writeString(color);
        parcel.writeString(known_for);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeLong(created_at);
        parcel.writeLong(updated_at);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getName() {
        return "Tick";
    }

    @Override
    public String getJSONResourceIdentifier() {
        return "ticks";
    }

    @Override
    public String getResourceType() {
        return AppConfig.TICK_RESOURCE;
    }

}
