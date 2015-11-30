package com.sfsu.network.rest.apiclient;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class RetrofitApiClient {
    public static final String BASE_API_URL = "https://investickations.com:3000";

    /**
     * Returns the ApiClient for the corresponding ApiClient.
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getApi(RetrofitApiClient.ApiTypes type) {
        try {
            return (T) type.getApiType().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Enum of ApiTypes
     */
    public enum ApiTypes {
        USER_API(UserApiClient.class), ACTIVITY_API(ActivityApiClient.class), OBSERVATION_API(ObservationApiClient.class),
        TICK_API(TickApiClient.class), LOCATION_API(LocationApiClient.class);

        // apiClass enum variable
        private final Class<? extends RetrofitApiClient> apiClass;

        /**
         * Constructor to initialize this Enum value to apiClass
         *
         * @param apiClass
         */
        ApiTypes(Class<? extends RetrofitApiClient> apiClass) {
            this.apiClass = apiClass;
        }

        /**
         * Returns the ApiType class.
         *
         * @return
         */
        Class<? extends RetrofitApiClient> getApiType() {
            return this.apiClass;
        }
    }




}

