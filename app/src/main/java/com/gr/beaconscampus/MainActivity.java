package com.gr.beaconscampus;

import android.content.Context;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private TabLayout tabLayout;
    private BeaconPagerAdapter pagerAdapter;
    private long mBackPressed;
    private Context context;
    private ViewPager viewPager;
    private String TAG = "MainActivity";

    private BeaconManager beaconManager;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

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
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect: ");
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion: ");
                for (Beacon beacon: beacons) {
                    Log.d(TAG, "I see a beacon with identifiers: "+beacon.getId1()+" "+beacon.getId2()+" "+beacon.getId3());
                    if (beacon.getId1().toString().equals("e2c56db5-dffb-48d2-b060-d0f5a71096e0")) {
                        Log.d(TAG, "gotem");
                        if(viewPager.getCurrentItem() == 0) //First fragment
                        {
                            request();
                        }
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    public void request() {
        Log.d(TAG, "request: ");
        String url = "http://my-json-feed";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d(TAG, "nothing");
                    }
                });
    }
    
}
