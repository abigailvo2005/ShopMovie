package com.example.shopmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends Activity implements View.OnClickListener {
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

        //change some features of the toolbar
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("User Profile");

        TextView signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setVisibility(View.INVISIBLE);

        //if clicked on the 3 dots icon --> show the signOut button
        findViewById(R.id.signOutdots).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtn.setVisibility(View.VISIBLE);
            }
        });

        //Sign Out actions
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to Log In Screen
                Intent logInI = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(logInI);
            }
        });

        //click anywhere on toolbar to hide the signOut button again
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtn.setVisibility(View.INVISIBLE);
            }
        });

        //get information about user from the Log-In Activity by SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LogInPrefs", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("displayName", "");
        String email = sharedPreferences.getString("email", "");
        String avatarURL = sharedPreferences.getString("avatar", "");
        String userID = sharedPreferences.getString("userID", "");


        //declare necessary Views from xml
        TextView name = findViewById(R.id.username);
        TextView mail = findViewById(R.id.gmail_address);
        ImageView avatar = findViewById(R.id.profile_pic);
        ProfilePictureView facebookView = findViewById(R.id.facebook_profile_pic);

        //display information
        name.setText(displayName);
        mail.setText(email);


        //check if user used google or facebook to log in
        if(!getIntent().getBooleanExtra("isFacebook", true)) {
            //hide the Facebook's Profile Picture View
            facebookView.setVisibility(View.INVISIBLE);

            avatar.setVisibility(View.VISIBLE);
            Picasso.get().load(avatarURL).into(avatar);

        } else {
            //hide the Google's Profile Picture View
            avatar.setVisibility(View.INVISIBLE);

            facebookView.setProfileId(userID);

            facebookView.setVisibility(View.VISIBLE);

        }


        //*********** BOTTOM MENU SECTION **************
        //set onClick events for 2 bottom buttons
        findViewById(R.id.movieSection).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.profileSection).setOnClickListener((View.OnClickListener) this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.movieSection:
                //Redirect to Movie Activity
                Intent mov = new Intent(getApplicationContext(), MovieActivity.class);
                startActivity(mov);
                break;

            case R.id.profileSection:
                //Redirect to Profile Activity
                Intent prof = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(prof);
                break;
        }
    }
}
