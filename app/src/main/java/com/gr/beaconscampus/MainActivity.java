package com.gr.beaconscampus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private TabLayout tabLayout;
    private BeaconPagerAdapter pagerAdapter;
    private long mBackPressed;
    private Context context;
    private ViewPager viewPager;
    private String TAG = "MainActivity";

    private String id;

    BeaconConsumer beaconConsumer;

    SharedPreferences sharedpreferences;

    RequestQueue queue;

    private Region globalRegion;

    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Password = "passKey";

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

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

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));

//        beaconManager.setEnableScheduledScanJobs(false);
//        beaconManager.setBackgroundBetweenScanPeriod(0);
//        beaconManager.setBackgroundScanPeriod(1100);

        beaconManager.bind(this);

        beaconConsumer = this;

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        pagerAdapter = new BeaconPagerAdapter(getSupportFragmentManager(), this);


        viewPager.setAdapter(pagerAdapter);


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
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

    public void onScanClicked(View view)
    {
        try {
            beaconManager.startRangingBeaconsInRegion(globalRegion);
        } catch (Exception e) {

        }
    }

    public void onInfoClicked(View view){
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            id = sharedpreferences.getString(Name, "");
        }
        String url = "http://localhost/beacon_backend/api/student_schedule/readTodaySchedule.php?student_id=" + id;

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
                        InformationActivityFragment frg = (InformationActivityFragment) pagerAdapter.getItem(1);
                        if(frg.stk == null) {
                            Log.d(TAG, "onResponse: stk null");
                        } else {
                            Log.d(TAG, "onResponse: stk not null");
                        }
                        for (int i = 0; i < response.length(); i++){

                            try {

                                JSONObject jsonobject = response.getJSONObject(i);

                                TableRow tbrow = new TableRow(frg.getContext());

                                TextView t1v = new TextView(frg.getContext());
                                String courseName = jsonobject.getString("courseName");
                                t1v.setText(courseName);
                                t1v.setTextColor(Color.BLACK);
                                t1v.setGravity(Gravity.CENTER);
                                tbrow.addView(t1v);

                                TextView t2v = new TextView(frg.getContext());
                                String startTime = jsonobject.getString("startTime");
                                t2v.setText(startTime);
                                t2v.setTextColor(Color.BLACK);
                                t2v.setGravity(Gravity.CENTER);
                                tbrow.addView(t2v);

                                TextView t3v = new TextView(frg.getContext());
                                String endTime = jsonobject.getString("endTime");
                                t3v.setText(endTime);
                                t3v.setTextColor(Color.BLACK);
                                t3v.setGravity(Gravity.CENTER);
                                tbrow.addView(t3v);

                                TextView t4v = new TextView(frg.getContext());
                                String roomName = jsonobject.getString("roomName");
                                t3v.setText(roomName);
                                t3v.setTextColor(Color.BLACK);
                                t3v.setGravity(Gravity.CENTER);
                                tbrow.addView(t4v);

                                frg.stk.addView(tbrow);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void request() {
        String url = "http://192.168.43.212/beacon_backend/api/class_has_student/createFromMobile.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject responseJSON = new JSONObject(response);
                            String responseMessage = responseJSON.getString("message");
                            if (responseMessage.equals("fail")) { //If student has no lecture right now
                                Toast.makeText(getBaseContext(), R.string.attend_fail, Toast.LENGTH_SHORT).show();
                            }
                            else { //If student has lecture right now
                                JSONObject dataJSON = responseJSON.getJSONObject("class_detail");
                                String courseName = dataJSON.getString("courseName");
                                String startTime = dataJSON.getString("startTime");
                                String endTime = dataJSON.getString("endTime");

                                AttendanceActivityFragment frg = (AttendanceActivityFragment) pagerAdapter.getItem(0);
                                if(frg.classNameTextView == null) {
                                    Log.d(TAG, "onResponse: null");
                                } else {
                                    Log.d(TAG, "onResponse: not null");
                                }
                                frg.classNameTextView.setText(getResources().getString(R.string.class_name) + " " + courseName);
                                frg.startTimeTextView.setText(getResources().getString(R.string.start_time) + " " + startTime);
                                frg.endTimeTextView.setText(getResources().getString(R.string.end_time) + " " + endTime);
                                frg.statusTextView.setText("Status: Attending class");
                                Toast.makeText(getBaseContext(), R.string.attend_success, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                sharedpreferences = getSharedPreferences(mypreference,
                        Context.MODE_PRIVATE);
                if (sharedpreferences.contains(Name)) {
                    id = sharedpreferences.getString(Name, "");
                }
                params.put("student_id", id);
                params.put("beacon_id", "test");
//                params.put("time", System.currentTimeMillis() + "");

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon: beacons) {
                    Log.d(TAG, "I see a beacon with identifiers: " + beacon.getBluetoothAddress());
                    if (beacon.getBluetoothAddress().toString().equals("FC:47:12:D5:20:EC")) {
                        Log.d(TAG, "gotem");
                        if (viewPager.getCurrentItem() == 0) //First fragment
                        {
                            try {
                                beaconManager.stopRangingBeaconsInRegion(region);
                                Log.d(TAG, "didRangeBeaconsInRegion: stopBeacon");
                                globalRegion = region;
                                request();
                            } catch (Exception e) {
                                
                            }
                        }
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
}
