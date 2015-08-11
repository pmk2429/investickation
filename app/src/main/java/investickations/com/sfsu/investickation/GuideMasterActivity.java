package investickations.com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import investickations.com.sfsu.investickation.fragments.GuideDetail;
import investickations.com.sfsu.investickation.fragments.GuideIndex;


public class GuideMasterActivity extends BaseActivity implements GuideIndex.IGuideIndexListener {

    private static String TICK_RESOURCE = "ticks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidmaster);

        
        // if Fragment container is present,
        if (findViewById(R.id.guide_fragment_container) != null) {


            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            GuideIndex guideIndexFragment = new GuideIndex();

            // if activity was started with special instructions from an Intent, then pass Intent's extras
            // to fragments as arguments
            guideIndexFragment.setArguments(getIntent().getExtras());

            // add the Fragment to 'guide_fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.guide_fragment_container, guideIndexFragment).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide_master, menu);
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

    // callback interface to listen to onClick event in GuideIndex Fragment
    @Override
    public void onGuideItemClick() {
        GuideDetail guideDetailFragment = new GuideDetail();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.guide_fragment_container, guideDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
