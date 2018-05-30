package com.gr.beaconscampus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gr.beaconscampus.data.Student;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by ahmad on 02/05/2018.
 */

public class AttendanceActivityFragment extends Fragment{

    private TextView studentIdTextView;
    private TextView studentNameTextView;
    private TextView statusTextView;

    private String LOG = "AttendanceActivityFragment";

    public AttendanceActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        Student student = new Student("default Student ID", "default Student Name", "Currently not attending any class");

        this.studentNameTextView = (TextView) rootView.findViewById(R.id.nameTextView);

        this.studentIdTextView = (TextView) rootView.findViewById(R.id.idTextView);

        this.statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);

        studentNameTextView.setText(getResources().getString(R.string.name) + " " + student.getStudent_name());

        studentIdTextView.setText(getResources().getString(R.string.student_id) + " " + student.getStudent_id());

        statusTextView.setText(getResources().getString(R.string.status) + " " + student.getStatus());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        Log.d(LOG, "dialog() called");
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
        Log.d(LOG, "dialog() ended");
    }
}
