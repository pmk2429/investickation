package com.sfsu.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>Tick </tt> <a href="https://en.wikipedia.org/wiki/Tick">(as described in Wiki)</a> are the real world entities
 * that are small arachnids in the order Parasitiformes.
 * </p>
 * The Tick class contains characteristics that a specific Tick exhibit.
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

    private String id;
    @SerializedName("common_name")
    private String tickName;
    private String scientific_name;
    private String species;
    private String known_for;
    private String description;
    @SerializedName("image_url")
    private String imageUrl;
    private Bitmap image;
    private String found_near_habitat;
    private long created_at1, updated_at1;

    public Tick() {
    }

    public Tick(String tickName, String species) {
        this.tickName = tickName;
        this.species = species;
    }

    /**
     * Constructor overloading for Tick response.
     *
     * @param id
     * @param tickName
     * @param species
     * @param known_for
     * @param description
     * @param image
     * @param created_at
     * @param updated_at
     */
    public Tick(String id, String commonName, String scientific_name, String species, String known_for, String description,
                Bitmap image, String found_near_habitat, long created_at, long updated_at) {
        this.id = id;
        this.tickName = commonName;
        this.species = species;
        this.known_for = known_for;
        this.scientific_name = scientific_name;
        this.description = description;
        this.image = image;
        this.created_at1 = created_at;
        this.updated_at1 = updated_at;
        this.found_near_habitat = found_near_habitat;
    }

    /**
     * Constructor overloading for creating {@link Tick} instances using.
     *
     * @param name
     * @param species
     * @param known_for
     * @param description
     * @param image
     */
    public Tick(String name, String species, String known_for, String description, Bitmap image) {
        this.tickName = name;
        this.species = species;
        this.known_for = known_for;
        this.description = description;
        this.image = image;
    }

    protected Tick(Parcel in) {
        id = in.readString();
        tickName = in.readString();
        species = in.readString();
        known_for = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        created_at1 = in.readLong();
        updated_at1 = in.readLong();
    }

    /**
     * Static method for creating {@link Tick} instance.
     *
     * @param name
     * @param species
     * @param known_for
     * @param description
     * @param image
     * @return
     */
    public static Tick createTick(String name, String species, String known_for, String description, Bitmap image) {
        return new Tick(name, species, known_for, description, image);
    }

    public static List<Tick> initializeData() {
        List<Tick> ticks = new ArrayList<>();
        ticks.add(new Tick("American Dog tick", "Found on Dogs"));
        ticks.add(new Tick("Spotted Tick", "Red colored with white spots"));
        ticks.add(new Tick("Jungle tick", "Dangerous species"));
        return ticks;
    }

    public String getFound_near_habitat() {
        return found_near_habitat;
    }

    public void setFound_near_habitat(String found_near_habitat) {
        this.found_near_habitat = found_near_habitat;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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
        return created_at1;
    }

    public void setCreated_at(long created_at) {
        this.created_at1 = created_at;
    }

    public long getUpdated_at() {
        return updated_at1;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at1 = updated_at;
    }

    @Override
    public String toString() {
        return id + " : " + tickName + " : " + species + " : " + known_for + " : " + description + " : " + imageUrl +
                " : " + created_at1 + " : " + updated_at1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(tickName);
        parcel.writeString(species);
        parcel.writeString(known_for);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeLong(created_at1);
        parcel.writeLong(updated_at1);
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getGeneralName() {
        return "Tick";
    }

    @Override
    public String getResourceType() {
        return "ticks";
    }

}
