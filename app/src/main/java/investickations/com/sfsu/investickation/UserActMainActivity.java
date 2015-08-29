package investickations.com.sfsu.investickation;

// else show the ActivityList Fragment in the 'activity_fragment_container'

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import investickations.com.sfsu.investickation.fragments.ActivityList;
import investickations.com.sfsu.investickation.fragments.ActivityNew;
import investickations.com.sfsu.investickation.fragments.ActivityRunning;


public class UserActMainActivity extends BaseActivity implements ActivityList.IActivityCallBacks, ActivityNew.IActivityNewCallBack, View.OnClickListener {


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

            if (getIntent().getIntExtra("ActivityNew", 0) == 1) {
                ActivityNew activityNewFragment = new ActivityNew();
                FragmentTransaction activityNew = getSupportFragmentManager().beginTransaction();
                activityNew.replace(R.id.activity_fragment_container, activityNewFragment);
                activityNew.addToBackStack(null);
                activityNew.commit();
            }

            ActivityList activityListFragment = new ActivityList();

            // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
            //activityListFragment.setArguments(getIntent().getExtras());

            // add Fragment to 'activity_fragment_container'
            getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container, activityListFragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(UserActMainActivity.this, MainActivity.class);
        startActivity(homeIntent);
        finish();
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

    /**
     * when the user clicks on the Play Button in ActivityNew Fragment, open the
     * ActivityRunning Fragment.
     */
    @Override
    public void onPlayButtonclick() {
        ActivityRunning mActivityRunningFragment = new ActivityRunning();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_container, mActivityRunningFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
