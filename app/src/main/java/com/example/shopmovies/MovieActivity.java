package com.example.shopmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MovieActivity extends Activity implements View.OnClickListener {
    GridView gridView;
    String[] names = {"movie", "movies", "mov", "hello", "movie", "movies", "mov", "hello"};
    String[] prices = {"$999", "$320", "$330", "$440", "$999", "$320", "$330", "$440"};
    int[] images = {R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper};

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

        //inflate the layout-view for Main Activity
        setContentView(R.layout.movie_layout);

        //change some features of the toolbar
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Movies");

        TextView signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setVisibility(View.INVISIBLE);

        //if clicked on the 3 dots icon --> show the signOut button
        findViewById(R.id.signOutdots).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutBtn.setVisibility(View.VISIBLE);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to Log In Screen
                Intent logInI = new Intent(MovieActivity.this, MainActivity.class);
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

        //set adapter and display movies on screen
        gridView = findViewById(R.id.theGrid);
        MovieAdapter adapter = new MovieAdapter(names, prices, images, getApplicationContext());
        gridView.setAdapter(adapter);


        //*********** BOTTOM MENU SECTION ************** s
        //set onClick events for 2 bottom buttons
        findViewById(R.id.movieSection).setOnClickListener((View.OnClickListener)this);
        findViewById(R.id.profileSection).setOnClickListener((View.OnClickListener)this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.movieSection:
                //Redirect to Movie Activity
                Intent mov = new Intent(getApplicationContext(), MovieActivity.class);
                startActivity(mov);
                break;

            case R.id.profileSection:
                //Redirect to Profile Activity
                Intent prof = new Intent(getApplicationContext(), ProfileActivity.class);

                //pass the user's login option to the ProfileActivity
                prof.putExtra("isFacebook", getIntent().getBooleanExtra("isFacebook", true));

                startActivity(prof);
                break;
        }
    }
}

