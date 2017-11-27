package de.ok94.atkleinversuchsanlage;

import android.content.Context;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.Fragment;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.LinearLayout;


public class MainActivity extends WearableActivity implements SoapReadTask.ValuesAvailable {

    private static final int NUM_PAGES = 2;
    private static final int SOAP_READ_PERIOD = 1000;
    private static final float MAX_LEVEL = 280.0f;

    private TankPageFragment tankPageFragment;
    private PumpPageFragment pumpPageFragment;

    private LinearLayout noConnectionOverlay;

    private SoapReadTask soapReadTask;
    private Handler handler;
    private Runnable runnable;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        // set up the pages of the main activity
        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        DotsPageIndicator pageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setPager(pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);

        noConnectionOverlay = (LinearLayout) findViewById(R.id.noConnectionOverlay);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        context = this;

        // start a Runnable that executes the SoapReadTask periodically
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    soapReadTask = new SoapReadTask((SoapReadTask.ValuesAvailable) context);
                    soapReadTask.execute();
                }
                catch (Exception e) {
                    Log.e("ASYNC_TASK", e.toString());
                }
                finally {
                    handler.postDelayed(this, SOAP_READ_PERIOD);
                }
            }
        };
        handler.postDelayed(runnable, SOAP_READ_PERIOD);
    }

    @Override
    protected void onPause() {
        // stop the Runnable
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void updateTankLevels(float level1, float level2, float level3) {
        // scale the tank levels with the height of the tanks in the activity
        float tankHeight = getResources().getDimension(R.dimen.tank_height);
        level1 = level1 / MAX_LEVEL * tankHeight;
        level2 = level2 / MAX_LEVEL * tankHeight;
        level3 = level3 / MAX_LEVEL * tankHeight;

        if (tankPageFragment.isAdded()) {
            tankPageFragment.setTankLevels(level1, level2, level3);
        }
    }

    @Override
    public void updateCapacitiveSensorStates(boolean ll1, boolean ll2, boolean ll3, boolean lh1, boolean lh2, boolean lh3) {
        if (tankPageFragment.isAdded()) {
            tankPageFragment.setCapacitiveSensorStates(ll1, ll2, ll3, lh1, lh2, lh3);
        }
    }

    @Override
    public void updatePumpingState(boolean pumping, int tankA, int tankB) {
        if (pumpPageFragment.isAdded()) {
            pumpPageFragment.setPumpingState(pumping, tankA, tankB);
        }
    }

    @Override
    public void setNoConnectionOverlayVisibility(int visibility) {
        noConnectionOverlay.setVisibility(visibility);
    }

    private class ScreenSlidePagerAdapter extends FragmentGridPagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getFragment(int row, int col) {
            switch (col) {
                case 0:
                    tankPageFragment = new TankPageFragment();
                    return tankPageFragment;
                case 1:
                    pumpPageFragment = new PumpPageFragment();
                    return pumpPageFragment;
            }

            return null;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int row) {
            return NUM_PAGES;
        }
    }
}
