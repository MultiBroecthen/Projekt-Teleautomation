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


public class MainActivity extends WearableActivity {

    private static final int NUM_COLUMNS = 3;
    private static final int PERIOD = 500;
    private static final float MAX_LEVEL = 280.0f;

    private GridViewPager mPager;
    private DotsPageIndicator mPageIndicator;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private TankPageFragment tankPageFragment;

    private AsyncSoapValueRequestTask soapValueRequestTask;
    private Handler handler;
    private Runnable runnable;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        mPager = (GridViewPager) findViewById(R.id.pager);
        mPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        mPageIndicator.setPager(mPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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
                    soapValueRequestTask = new AsyncSoapValueRequestTask(context);
                    soapValueRequestTask.execute();
                }
                catch (Exception e) {
                    Log.e("ASYNC_TASK", e.toString());
                }
                finally {
                    handler.postDelayed(this, PERIOD);
                }
            }
        };
        handler.postDelayed(runnable, PERIOD);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    public void setTankLevels(float level1, float level2, float level3) {
        float tankHeight = (float) getResources().getDimension(R.dimen.tank_height);
        level1 = level1 / MAX_LEVEL * tankHeight;
        level2 = level2 / MAX_LEVEL * tankHeight;
        level3 = level3 / MAX_LEVEL * tankHeight;
        tankPageFragment.setTankLevels(level1, level2, level3);
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
            return NUM_COLUMNS;
        }
    }
}
