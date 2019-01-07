package com.gr.beaconscampus;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by ahmad on 02/05/2018.
 */

public class InformationActivityFragment extends Fragment {

    private Context context;

    public TableLayout stk;

    private String TAG = "InformationActivityFragment";

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

        final Activity activity = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        stk = (TableLayout) rootView.findViewById(R.id.table_main);

        TableRow tbrow0 = new TableRow(this.context);

        TextView tv0 = new TextView(this.context);
        tv0.setText(" Course ");
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this.context);
        tv1.setText(" Start Time ");
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this.context);
        tv2.setText(" End Time ");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this.context);
        tv2.setText(" Room Name ");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);

        stk.addView(tbrow0);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
