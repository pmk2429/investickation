package investickations.com.sfsu.investickation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import investickations.com.sfsu.investickation.fragments.Dashboard;


public class MainActivity extends BaseActivity implements Dashboard.IDashboardCallback {

    // resource identifier for each unique resource. specifying demo
    private static String USER_RESOURCE = "users";
    ImageButton btnActivityAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if Fragment container is present,
        if (findViewById(R.id.mainActivity_fragmentContainer) != null) {


            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            Dashboard dashboardFragment = new Dashboard();

            // if activity was started with special instructions from an Intent, then pass Intent's extras
            // to fragments as arguments
            dashboardFragment.setArguments(getIntent().getExtras());

            // add the Fragment to 'mainActivity_fragmentContainer' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.mainActivity_fragmentContainer, dashboardFragment).commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onDashboardInteraction(Uri uri) {

    }

    @Override
    public void onActivityButtonClicked() {
        Intent activityIntent = new Intent(MainActivity.this, UserActMainActivity.class);
        startActivity(activityIntent);
        finish();
    }

    @Override
    public void onObservationButtonClicked() {
        Intent observationIntent = new Intent(MainActivity.this, ObservationMainActivity.class);
        startActivity(observationIntent);
        finish();
    }
}

