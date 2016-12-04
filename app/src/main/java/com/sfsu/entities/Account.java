package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>
 * <tt>Account</tt> is the main actor of InvesTICKations applications. Account is the real world human who uses this application to
 * carry out {@link Activities} and make {@link Observation}. The Account class contains real world information about each Account
 * who are registered for the application.
 * <p>
 * Created by Pavitra on 5/19/2015.
 */
public class Account implements Parcelable, Entity {

    public static final Parcelable.Creator<Account> CREATOR
            = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };


    private String id;
    private String full_name, email, password, address, city, state;
    private int zipCode;
    private long created_at;

    private Account(Parcel in) {
        // mData = in.readInt();
    }

    public Account() {
    }

    /**
     * Constructor for the Retrofit Response.
     *
     * @param id
     * @param full_name
     * @param email
     * @param password
     * @param address
     * @param city
     * @param state
     * @param zipCode
     * @param created_at
     * @param updated_at
     */
    public Account(String id, String full_name, String email, String password, String address, String city, String state, int zipCode, long created_at) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.created_at = created_at;
    }

    /**
     * Constructor overloading to build a {@link Account} object to send it over to server via Retrofit.
     *
     * @param full_name
     * @param address
     * @param city
     * @param state
     * @param zipCode
     * @param email
     * @param password
     */
    public Account(String full_name, String address, String city, String state, int zipCode, String email, String password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public static Account createUser(String fullName, String address, String city, String state, int zipcode, String email, String
            password) {

        // create and return new user
        return new Account(fullName, address, city, state, zipcode, email, password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public String toString() {
        return id + ", " + full_name + ", " + email + ", " + password + ", " + address + ", " + city + ", " + state + ", " +
                zipCode + ", " + +created_at;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public String getGeneralName() {
        return "Account";
    }

    @Override
    public String getResourceType() {
        return "users";
    }

}
