package com.gr.beaconscampus;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ahmad on 02/05/2018.
 */

public class BeaconPagerAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private Context mContext;

    public BeaconPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    private Fragment[] fragments = new Fragment[PAGE_COUNT];

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragments[position] = new AttendanceActivityFragment();
                return fragments[position];
            case 1:
                fragments[position] = new InformationActivityFragment();
                return fragments[position];
            default:
                fragments[0] = new AttendanceActivityFragment();
                return fragments[0];
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.title_activity_attendance);
        }
        else {
            return mContext.getString(R.string.title_activity_information);
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public Fragment getMyFragment(int position) {
        try {
            return fragments[position];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
