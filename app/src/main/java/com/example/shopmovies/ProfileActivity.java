package com.example.shopmovies;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;

public class ProfileActivity extends Activity {
    @Override
    protected void onPause() {
        super.onPause();

        //adding animations between Activity's transitions
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //delete the default toolbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //inflate the layout-view for Profile Activity
        setContentView(R.layout.profile_layout);
    }
}
