package investickations.com.sfsu.investickation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import investickations.com.sfsu.investickation.fragments.Login;

public class HomeActivity extends BaseActivity implements Login.ILoginCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);


        // if Fragment container is present
        if (findViewById(R.id.home_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // else show the ActivityList Fragment in the 'activity_fragment_container'
            Login loginFragment = new Login();

            // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
            loginFragment.setArguments(getIntent().getExtras());

            // add Fragment to 'activity_fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, loginFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    public void onFragmentInteraction() {
        
    }
}
