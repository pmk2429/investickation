package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sfsu.investickation.fragments.SplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "~!@#$SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashScreen splashScreenFragment = new SplashScreen();
        getSupportFragmentManager().beginTransaction().add(R.id.splashscreen_fragment_container, splashScreenFragment).commit();

        Thread splashTimer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    Log.i(TAG, "run: ");
                } finally {
                    Intent dashboardIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    dashboardIntent.putExtra(HomeActivity.KEY_SIGNIN_SUCCESS, 1);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    startActivity(dashboardIntent);
                }
                finish();
            }
        };
        splashTimer.start();
    }

}
