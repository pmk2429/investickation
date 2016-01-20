package com.sfsu.entities.response;

import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.sfsu.entities.Tick;

/**
 * Wrapper model class for the {@link Observation} response
 */
public class ObservationResponse {
    private final Observation observation;
    private final Activities activity;
    private final Tick tick;

    public ObservationResponse(Observation observation, Activities activity, Tick tick) {
        this.observation = observation;
        this.activity = activity;
        this.tick = tick;
    }

    public Observation getObservation() {
        return observation;
    }

    public Activities getActivity() {
        return activity;
    }

    public Tick getTick() {
        return tick;
    }
}
