package investickations.com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link SplashScreen} Fragment that contains the ViewPager for the first time user to get started.
 * This screen will provide the Gist of 'using the application' using multiple screens. SplashScreen holds
 * multiple fragments showcsing the features of the application.
 */
public class SplashScreen extends Fragment {

    private ISplashScreenCallBacks mListener;


    public SplashScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ISplashScreenCallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ISplashScreenCallBacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface ISplashScreenCallBacks {
        public void onSplashScreenItemListener();
    }

}
