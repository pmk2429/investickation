package com.sfsu.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */

/**
 * <p>
 * <tt>User</tt> class defines all the attributes of the User entity.
 * This construct makes the main user of the application. The class
 * provides all the getters and setters as well as all tostring() method
 * </p>
 * <p/>
 * The User class also provides the Factory pattern to create the user on demand.
 * <p/>
 */
public class User implements Parcelable, Entity {

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    // TODO: define how to post Image using Bitmap and Retrofit
    private long user_id;
    private String zipcode;
    private String username, email, password, address, city, state;
    private long created_at, updated_at;


    private User(Parcel in) {
//        mData = in.readInt();
    }


    public User() {
    }

    public User(int user_id, String username, String email, String password, String address, String city, String zipcode, String
            state) {
        this.user_id = user_id;
        this.zipcode = zipcode;
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    // TODO: http://www.javaworld.com/article/2074938/core-java/too-many-parameters-in-java-methods-part-3-builder-pattern.html
    public static User createUser(String username, String email, String password, String zipcode, String address) {

        // TODO: Create Logic for user_id.
        // TODO: Parse the Address String and get City, Zip and State
        int user_id = 0;
        String city = "", state = "";

        // create and return new customer
        return new User(user_id, username, email, password, address, city, zipcode, state);
    }

    /**
     * createUserFactory() is the factory design pattern to create Users without revealing
     * the implementation logic to the users.
     */
    @Override
    public Entity createEntityFactory(JSONObject jsonObject) {
        User user = new User();

        return user;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return "User{" +
                "user_id=" + user_id +
                ", zipcode=" + zipcode +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
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

    @Override
    public String getName() {
        return "User";
    }

    @Override
    public String getJSONResourceIdentifier() {
        return "users";
    }
}
