package com.example.shopmovies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieActivity extends Activity implements View.OnClickListener {
    GridView gridView;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<String> imageURLs = new ArrayList<>();


    String movieName, moviePrice, movieImageURL;
    private static String JSON_URL = "https://api.androidhive.info/json/movies_2017.json";
    ArrayList<HashMap<String, String>> movieList;


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


        //*********** BOTTOM MENU SECTION ************** s
        //set onClick events for 2 bottom buttons
        findViewById(R.id.movieSection).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.profileSection).setOnClickListener((View.OnClickListener) this);


        RetrieveData getData = new RetrieveData();
        getData.execute();

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

                //pass the user's login option to the ProfileActivity
                prof.putExtra("isFacebook", getIntent().getBooleanExtra("isFacebook", true));

                startActivity(prof);
                break;
        }
    }

    public class RetrieveData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String cur = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader inReader = new InputStreamReader(in);

                    int data = inReader.read();
                    while (data != -1) {

                        cur += (char) data;
                        data = inReader.read();

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
            }

            return cur;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    movieName = jsonObject.getString("title");
                    moviePrice = (String) jsonObject.get("price");
                    movieImageURL = jsonObject.getString("image");


                    names.add(movieName);
                    prices.add(moviePrice);
                    imageURLs.add(movieImageURL);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //set adapter and display movies on screen
            gridView = findViewById(R.id.theGrid);
            MovieAdapter adapter = new MovieAdapter(names, prices, imageURLs, MovieActivity.this);
            gridView.setAdapter(adapter);
        }
    }


}

