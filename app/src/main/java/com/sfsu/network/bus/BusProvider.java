package com.sfsu.network.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Singleton Bus provider that creates and returns Bus instance. This class enforces singleton design pattern in order to re
 * use the same bus instance throughout the entire application.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class BusProvider {
    private static volatile Bus instance;

    private BusProvider() {
    }

    public static Bus bus() {
        Bus localInstance = instance;
        if (localInstance == null) {
            synchronized (Bus.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Bus(ThreadEnforcer.ANY);
                }
            }
        }
        return localInstance;
    }
}
