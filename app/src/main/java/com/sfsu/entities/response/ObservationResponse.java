package com.sfsu.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.sfsu.entities.Activities;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.Observation;
import com.sfsu.entities.Tick;

/**
 * Wrapper model class for the {@link Observation} response
 */
public class ObservationResponse implements Parcelable {
    public static final Creator<ObservationResponse> CREATOR = new Creator<ObservationResponse>() {
        @Override
        public ObservationResponse createFromParcel(Parcel in) {
            return new ObservationResponse(in);
        }

        @Override
        public ObservationResponse[] newArray(int size) {
            return new ObservationResponse[size];
        }
    };
    private final Observation observation;
    private final Activities activity;
    private final Tick tick;
    private final EntityLocation location;

    public ObservationResponse(Observation observation, Activities activity, Tick tick, EntityLocation location) {
        this.observation = observation;
        this.activity = activity;
        this.tick = tick;
        this.location = location;
    }

    protected ObservationResponse(Parcel in) {
        observation = in.readParcelable(Observation.class.getClassLoader());
        activity = in.readParcelable(Activities.class.getClassLoader());
        tick = in.readParcelable(Tick.class.getClassLoader());
        location = in.readParcelable(EntityLocation.class.getClassLoader());
    }

    public EntityLocation getLocation() {
        return location;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(observation, flags);
        dest.writeParcelable(activity, flags);
        dest.writeParcelable(tick, flags);
        dest.writeParcelable(location, flags);
    }
}
