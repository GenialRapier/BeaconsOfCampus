package com.gr.beaconscampus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmad on 15/09/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static String LOG_TAG = LoginActivity.class.getSimpleName();

    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Password = "passKey";

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        Log.v(LOG_TAG, "Starting normal");
        mUsernameView = (EditText) findViewById(R.id.username_login_edittext);
        mPasswordView = (EditText) findViewById(R.id.password_login_edittext);

        if (sharedpreferences.contains(Name)) {
            mUsernameView.setText(sharedpreferences.getString(Name, ""));
        }
        if (sharedpreferences.contains(Password)) {
            mPasswordView.setText(sharedpreferences.getString(Password, ""));
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.buttonLogin);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    public void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username
        if (!TextUtils.isEmpty(username) && !isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            login();
        }
    }

    private void login() {
        //Getting values from edit texts
        final String username = mUsernameView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();

        showProgress(true);

        try {
            String datas = "{'username': username,'password': password}";
            data = new JSONObject(datas);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final String url = "http://192.168.4.170:3000/api/currClass/1/test";
        postData(url, data);
    }

    private boolean isUsernameValid(String username) {
        return username.length() > 1;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }

    private void startMainActivity() {
        Log.v(LOG_TAG, "Starting MainActivity");
        this.finish();
    }

    public void postData(String url,JSONObject data){
        RequestQueue requstQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            String n = mUsernameView.getText().toString();
                            String p = mPasswordView.getText().toString();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Name, n);
                            editor.putString(Password, p);
                            editor.commit();
                            LoginActivity.this.startMainActivity();
                            showProgress(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String n = mUsernameView.getText().toString(); // Debug
                        String p = mPasswordView.getText().toString(); // Debug
                        SharedPreferences.Editor editor = sharedpreferences.edit(); // Debug
                        editor.putString(Name, n); // Debug
                        editor.putString(Password, p); // Debug
                        editor.commit(); // Debug
                        LoginActivity.this.startMainActivity(); // Debug purpose
                        showProgress(false);
                    }
                }
        );
        requstQueue.add(jsonobj);

    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
