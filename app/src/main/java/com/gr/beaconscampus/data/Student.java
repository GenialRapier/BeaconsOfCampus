package com.gr.beaconscampus.data;

/**
 * Created by ahmad on 17/05/2018.
 */

public class Student {
    private String student_id;
    private String student_name;
    private String status;


    public Student(String id, String name) {
        this.student_id = id;
        this.student_name = name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getStudent_name() {
        return student_name;
    }
}
