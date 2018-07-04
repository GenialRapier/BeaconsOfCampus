package com.gr.beaconscampus;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.Manifest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gr.beaconscampus.data.Student;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmad on 02/05/2018.
 */

public class AttendanceActivityFragment extends Fragment implements LocationListener{

    private TextView studentIdTextView;
    private TextView studentNameTextView;
    public TextView classNameTextView;
    public TextView startTimeTextView;
    public TextView endTimeTextView;
    private TextView statusTextView;

    private Context context;

    private String classString;
    private String startTime;
    private String endTime;

    LocationManager locationManager;
    String provider;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private String TAG = "AttendanceActivityFragment";

    public AttendanceActivityFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context == null) {
            Log.d(TAG, "onAttach: null");
        } else {
            this.context = context;
            Log.d(TAG, "onAttach: not null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        Student student = new Student("default Student ID", "default Student Name", "Currently not attending any class");

        this.studentNameTextView = (TextView) rootView.findViewById(R.id.nameTextView);

        this.studentIdTextView = (TextView) rootView.findViewById(R.id.idTextView);

        this.statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);

        this.classNameTextView = (TextView) rootView.findViewById(R.id.classTextView);

        this.startTimeTextView = (TextView) rootView.findViewById(R.id.startTextView);

        this.endTimeTextView = (TextView) rootView.findViewById(R.id.endTextView);

        Log.d(TAG, "onCreateView: called");

        studentNameTextView.setText(getResources().getString(R.string.name) + " " + student.getStudent_name());

        studentIdTextView.setText(getResources().getString(R.string.student_id) + " " + student.getStudent_id());

        statusTextView.setText(getResources().getString(R.string.status) + " " + student.getStatus());

        statusCheck();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        checkLocationPermission();

        //dialog();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //dialog();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void dialog() {
        Log.d(TAG, "dialog() called");
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.getActivity());
        }
        builder.setTitle("Confirmation")
                .setMessage("Is it really you?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with yes
                        statusTextView.setText(getResources().getString(R.string.status) + " " + "Currently attending " + "default " + "class");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        Log.d(TAG, "dialog() ended");
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("LOCATION")
                        .setMessage("LOCATION")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void request(Context contexte) {
        Log.d(TAG, "request: ");
        String url = "http://192.168.30.106:3000/api/currClass/1/test";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG,"Response: " + response.toString());
                        try {
                            classString = response.getString("name");
                        } catch(JSONException e) {}
                        try {
                            startTime = response.getString("start_time");
                        } catch (JSONException e) {}
                        try {
                            endTime = response.getString("end_time");
                        } catch (JSONException e) {}
                              classNameTextView.setText(getResources().getString(R.string.class_name) + " " + classString);
                              startTimeTextView.setText(getResources().getString(R.string.start_time) + " " + startTime);
                              endTimeTextView.setText(getResources().getString(R.string.end_time) + " " + endTime);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d(TAG, "nothing");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(contexte);
        requestQueue.add(jsonObjectRequest);
    }

}
