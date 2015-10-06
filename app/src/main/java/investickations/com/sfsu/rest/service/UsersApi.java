package investickations.com.sfsu.rest.service;


import java.util.List;

import investickations.com.sfsu.entities.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface is used to manage URL calls to the REST Api. In this interface you have to specify the type
 * of the request like POST, GET, PUT, etc.. and the uri because in Retrofit you have to set a base url (common url for all requests)
 * and each method in your interface specifies the end of your url with your arguments. Best practice: begin all your endpoint by
 * a '/' like this way you know that all your request begin by a / and your base url have always to finish without /. Some of you will
 * need to add some headers to their requests. You can do it in two ways: with the <tt>@Header</tt> annotation, above your request or in
 * your parameters if you want to use a dynamic header, and programmatically. The second method is explained in the bonus part.
 * The second best practice: create an interface for each type of request (for example one for all user requests like
 * Signing In, Signing Out, getProfile and another for content, etc…). In this article I will show you only Get and Post requests,
 * because they are commonly used.
 * <p/>
 * The endpoints are defined inside of an interface using special retrofit annotations to encode details about the parameters and
 * request method. In addition, the return value is always a parameterized `Call<T>` object such as `Call<User>`.
 * If you do not need any type-specific response, you can specify return value as simply `Call<Response>`.
 * <p/>
 * Created by Pavitra on 10/6/2015.
 */
public interface UsersApi {

    /**
     * This method returns the list of all the entries in resource.
     *
     * @return
     */
    @GET("/users/{user}/tick")
    public Call<List<User>> getUsers();


    /**
     * This method is used to create the specified resource and store in database.
     *
     * @return
     */
    @POST("/users/new")
    Call<User> createUser(@Body User user);

    /**
     * This method displays a specific resource by using ID of that resource.
     *
     * @return
     */
    @GET("/users/{user}")
    public Call<User> showUser(@Path("user") String userId);


    /**
     * edit() method is called when we need to edit a resource.
     *
     * @return
     */
    @GET("/users/{user}")
    public Call<User> editUser(@Path("user") String userId);


    /**
     * destroy() when called, destroys the record from database.
     *
     * @return
     */
    @GET("/users/{user}")
    public Call<User> deleteUser(@Path("user") String userId);

    /*
    @GET("/group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Call<User> createUser(@Body User user);

     */

}