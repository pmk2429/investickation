package investickations.com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import investickations.com.sfsu.investickation.fragments.ActivityList;
import investickations.com.sfsu.investickation.fragments.ActivityNew;


public class UserActMainActivity extends BaseActivity implements ActivityList.IActivityCallBacks, View.OnClickListener {

    private static String ACTIVITY_RESOURCE = "activities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);


        // if Fragment container is present
        if (findViewById(R.id.activity_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // else show the ActivityList Fragment in the 'activity_fragment_container'
            ActivityList activityListFragment = new ActivityList();

            // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
            //activityListFragment.setArguments(getIntent().getExtras());

            // add Fragment to 'activity_fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container, activityListFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_activity_main, menu);
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

    /**
     * method to listen to the onClick of the Item in Activity List Interface
     */
    @Override
    public void onItemClickListener() {
        
    }

    @Override
    public void onActivityAddListener() {
        // if user clicked the Add Button, replace with AddObservation Fragment
        ActivityNew addActivityFragment = new ActivityNew();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_container, addActivityFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {


    }
}
