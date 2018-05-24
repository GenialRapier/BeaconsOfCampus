package com.gr.beaconscampus;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private BeaconPagerAdapter pagerAdapter;
    private long mBackPressed;

    private int[] tabIcons = {
            R.drawable.ic_assignment_turned_in_black_24dp,
            R.drawable.ic_info_outline_black_24dp
    };

    private BeaconPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        pagerAdapter = new BeaconPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), R.string.prompt_quit, Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
}
