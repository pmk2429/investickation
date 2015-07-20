package investickations.com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import investickations.com.sfsu.investickation.fragments.AddObservation;
import investickations.com.sfsu.investickation.fragments.GuideIndex;
import investickations.com.sfsu.investickation.fragments.ObservationDetail;
import investickations.com.sfsu.investickation.fragments.RemoteObservations;

public class ObservationMainActivity extends BaseActivity implements RemoteObservations.IObservationsInteractionListener, View.OnClickListener {

    private static String OBSERVATION_RESOURCE = "observations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_observation_main);
        super.onCreate(savedInstanceState);

        // if Fragment container is present,
        if (findViewById(R.id.observation_fragment_container) != null) {


            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            RemoteObservations remoteObservations = new RemoteObservations();

            // if activity was started with special instructions from an Intent, then pass Intent's extras
            // to fragments as arguments
            remoteObservations.setArguments(getIntent().getExtras());

            // add the Fragment to 'guide_fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.observation_fragment_container, remoteObservations).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observation_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onObservationInteractionListener() {
        ObservationDetail observationDetailFragment = new ObservationDetail();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, observationDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        // if user clicked the Add Button, replace with AddObservation Fragment
        AddObservation addObservationFragment = new AddObservation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, addObservationFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
