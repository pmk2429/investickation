package com.sfsu.network.handler;

import android.content.Context;

import com.sfsu.entities.Account;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.LoginEvent;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.LoginService;
import com.sfsu.network.rest.service.UserApiService;
import com.sfsu.session.LoginResponse;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * <p>
 * RequestHandler to handle network requests for {@link Account}. It Subscribes to the Account events published by the all Account related
 * operations through out the application and makes successive network calls depending on the type of request made such as get,
 * add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class UserRequestHandler extends ApiRequestHandler {

    private final String TAG = "~!@#$UserReqHdlr";
    private UserApiService mApiService;
    private ErrorResponse mErrorResponse;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public UserRequestHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(UserApiService.class);
    }

    /**
     * Subscribes to the event when {@link Account} is Loaded. Receives the Account object and make network calls to
     * Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserEvent(UserEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Account> userCall = null;
        // delegating call to specific method.
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                userCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(userCall);
                break;
            case ADD:
                userCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(userCall);
                break;
            case TOTAL_LOCATIONS_COUNT:
                break;
        }
    }

    /**
     * Helper method for dealing with CRUD operations for {@link Account} entity.
     *
     * @param userCall
     */
    private void makeCRUDCall(Call<Account> userCall) {
        userCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccess()) {
                    mBus.post(new UserEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new UserEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(UserEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

                if (t != null && t.getMessage() != null) {
                    mBus.post(new UserEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(UserEvent.FAILED);
                }
            }
        });
    }

    /**
     * Subscribes to the Account Login event and then posts the {@link com.sfsu.session.LoginResponse} back to the Bus.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserLoginEvent(LoginEvent.OnLoadingInitialized onLoadingInitialized) {
        LoginService loginApiService = RetrofitApiClient.createService(LoginService.class);
        // make login call using LoginServiceApi
        Call<LoginResponse> userLoginCall = loginApiService.login(onLoadingInitialized.email, onLoadingInitialized.password);
        userLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccess()) {
                    mBus.post(new LoginEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new LoginEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(LoginEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new LoginEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(LoginEvent.FAILED);
                }
            }
        });
    }
}
