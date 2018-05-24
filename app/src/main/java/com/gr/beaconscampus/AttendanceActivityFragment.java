package com.gr.beaconscampus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gr.beaconscampus.data.Student;

/**
 * Created by ahmad on 02/05/2018.
 */

public class AttendanceActivityFragment extends Fragment{

    private TextView studentIdTextView;
    private TextView studentNameTextView;
    private TextView statusTextView;

    public AttendanceActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        Student student = new Student("default Student ID", "default Student Name");

        this.studentNameTextView = (TextView) rootView.findViewById(R.id.nameTextView);

        this.studentIdTextView = (TextView) rootView.findViewById(R.id.idTextView);

        this.statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);

        studentNameTextView.setText(getResources().getString(R.string.name) + " " + student.getStudent_name());

        studentIdTextView.setText(getResources().getString(R.string.student_id) + " " +student.getStudent_id());

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
}
