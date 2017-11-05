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


public class MainActivity extends WearableActivity implements SoapReadTask.ValuesAvailable {

    private static final int NUM_PAGES = 3;
    private static final int SOAP_READ_PERIOD = 500;
    private static final float MAX_LEVEL = 280.0f;

    private TankPageFragment tankPageFragment;

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

        GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        DotsPageIndicator pageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setPager(pager);
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);

        handler = new Handler();
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void updateTankLevels(float level1, float level2, float level3) {
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
                    return new PumpPageFragment();
                case 2:
                    return new HistoryPageFragment();
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
