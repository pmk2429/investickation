package com.sfsu.network.handler;

/**
 * <p>
 * Parent class for all RequestHandlers in the application. It contains the api method identifiers and a constructor to handle
 * the Retrofit network calls for each of {@link com.sfsu.entities.Entity} present in the InvesTICKations project.
 * </p>
 * Created by Pavitra on 11/27/2015.
 */
public class ApiRequestHandler {
    // Service method
    protected final String GET_METHOD = "get";
    protected final String GET_ALL_METHOD = "getAll";
    protected final String ADD_METHOD = "add";
    protected final String UPDATE_METHOD = "update";
    protected final String DELETE_METHOD = "delete";
    

//    private Bus mBus;
//
//    /**
//     * Constructor overloading to initialize the Bus to be used for this Request Handling.
//     *
//     * @param bus
//     */
//    public ApiRequestHandler(Bus bus) {
//        mBus = bus;
//    }


}
