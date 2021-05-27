package com.example.shopmovies;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;

//LOG IN ACTIVITY
public class MainActivity extends AppCompatActivity {
    public GoogleSignInClient ggClient;
    public GoogleSignInAccount account;
    public CallbackManager callbackManager;
    public LoginButton facebookLogInBtn;
    public static final int SIGN_IN = 1;
    AccessToken accessToken;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String USERNAME_KEY = "displayName";
    String EMAIL_KEY = "email";
    String AVATAR_KEY = "avatar";
    String USERID_KEY = "userID";

    @Override
    protected void onStart() {
        super.onStart();

        //always log out of existed account when starting this Activity
        ggClient.signOut();
        LoginManager.getInstance().logOut();


    }

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
        getSupportActionBar().hide();

        //initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());

        //inflate the layout-view for Main Activity
        setContentView(R.layout.activity_main);

        //create a sharedPreferences with name
        sharedPreferences = getSharedPreferences("LogInPrefs", getApplicationContext().MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //*************  GOOGLE SIGN IN  ***************
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        ggClient = GoogleSignIn.getClient(this, gso);


        //When click on the Google sign in button --> show sign in dialog
        SignInButton ggSignInBtn = findViewById(R.id.gg_sign_in_btn);
        ggSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //*************  FACEBOOK SIGN IN  ***************

        //create a callback manager deal with information after a successful log-in
        callbackManager = CallbackManager.Factory.create();

        //declare facebook log-in button + set read permission
        facebookLogInBtn = findViewById(R.id.fb_sign_in_button);
        facebookLogInBtn.setReadPermissions(Arrays.asList("public_profile", "email"));

        facebookLogInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();

                //declare the user's profile to get his/her avatar + display name
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String displayName = profile.getName();

                    //add user's ID into sharePref
                    editor.putString(USERID_KEY, profile.getId());


                    //add name into the database
                    editor.putString(USERNAME_KEY, displayName);

                }

                //use facebook's GraphRequest to retrieve extra data - in this case: email
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        //add email to the database
                        String email = object.optString("email");
                        editor.putString(EMAIL_KEY, email);
                        editor.commit();
                    }
                });

                Bundle param = new Bundle();
                param.putString("fields", "email");
                request.setParameters(param);
                request.executeAsync();


                //redirect user to MovieActivity
                toMovieShop();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                Log.e("tag", error.toString());
            }
        });


    }


    public void signIn() {
        Intent ggInt = ggClient.getSignInIntent();
        startActivityForResult(ggInt, SIGN_IN);
    }


    public void toMovieShop() {
        Intent i = new Intent(MainActivity.this, MovieActivity.class);
        i.putExtra("isFacebook", isFacebook()); //pass user's ID to set facebook profile picture
        startActivity(i);
    }

    //function to check which Log In Service did User use (ProfileActivity to call)
    public boolean isFacebook() {
        accessToken = AccessToken.getCurrentAccessToken();
        //return TRUE if user logged in by Facebook
        //return FALSE if user logged in by Google
        return accessToken != null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Google Intent
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else { //result returned from Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            //declare the user's profile to get his/her avatar + display name + email
            account = completedTask.getResult(ApiException.class);

            String email = account.getEmail();
            String displayName = account.getDisplayName();
            String avatar = account.getPhotoUrl().toString();

            //add User's information into sharedPreferences
            editor.putString(USERNAME_KEY, displayName);
            editor.putString(EMAIL_KEY, email);
            editor.putString(AVATAR_KEY, avatar);
            editor.commit();

            //redirect to User's Profile screen
            toMovieShop();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            System.out.println("signInResult:failed code=" + e.getStatusCode());
        }
    }
}