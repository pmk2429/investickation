package com.sfsu.network.handler;

import android.util.Log;

import com.sfsu.entities.User;
import com.sfsu.network.events.LoginEvent;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.login.LoginResponse;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.LoginService;
import com.sfsu.network.rest.service.UserApiService;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * <p>
 * RequestHandler to handle network requests for {@link User}. It Subscribes to the User events published by the all User related
 * operations through out the application and makes successive network calls depending on the type of request made such as get,
 * add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class UserRequestHandler extends ApiRequestHandler {

    private final String LOGTAG = "~!@#$UserReqHdlr :";
    private UserApiService mApiService;
    private Bus mBus;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public UserRequestHandler(Bus bus) {
//        super(bus);
        mBus = bus;
        mApiService = RetrofitApiClient.createService(UserApiService.class);
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.User} is Loaded. Receives the User object and make network calls to
     * Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserEvent(UserEvent.OnLoadingInitialized onLoadingInitialized) {
        Log.i(LOGTAG, "inside onInitializeUserEvent");
        Call<User> userCall = null;

        Log.i(LOGTAG, "Created user:" + onLoadingInitialized.getRequest().toString());

        // delegating call to specific method.
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET_METHOD:
                userCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(userCall);
                break;
            case ADD_METHOD:
                Log.i(LOGTAG, "yayyyy inside the add user method");
                userCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(userCall);
                break;
        }
    }

    /**
     * Helper method for dealing with CRUD operations for {@link User} entity.
     *
     * @param userCall
     */
    private void makeCRUDCall(Call<User> userCall) {
        Log.i(LOGTAG, "making CRUD call");
        // makes the Calls to network.
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                Log.i(LOGTAG, "inside onResponse");
                if (response.isSuccess()) {
                    Log.i(LOGTAG, "Response Success");
                    mBus.post(new UserEvent.OnLoaded(response.body()));
                } else {
                    Log.i(LOGTAG, "Response Failure");
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new UserEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(UserEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(LOGTAG, "inside onFailure");
                if (t != null && t.getMessage() != null) {
                    mBus.post(new UserEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(UserEvent.FAILED);
                }
            }
        });
    }

    /**
     * Subscribes to the User Login event and then posts the {@link com.sfsu.network.login.LoginResponse} back to the Bus.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onIntitalizeUserLoginEvent(LoginEvent.OnLoadingInitialized onLoadingInitialized) {
        LoginService loginApiService = RetrofitApiClient.createService(LoginService.class);
        // make login call using LoginServiceApi
        Call<LoginResponse> userLoginCall = loginApiService.login(onLoadingInitialized.email, onLoadingInitialized.password);
        userLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                if (response.isSuccess()) {
                    mBus.post(new LoginEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new LoginEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(LoginEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new LoginEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(LoginEvent.FAILED);
                }
            }
        });
    }
}
