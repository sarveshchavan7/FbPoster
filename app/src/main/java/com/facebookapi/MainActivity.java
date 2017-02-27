package com.facebookapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
    LoginButton lb;

    CallbackManager callbackManager;
    Button post;
    String x;
    private static final String PAGE_ID = "288637851555189";
    String idOfUser;

    private static final String getURL = "https://api.myjson.com/bins/8wgo1";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lb = (LoginButton) findViewById(R.id.button_fb);
        requestQueue = Volley.newRequestQueue(this);

        callbackManager = CallbackManager.Factory.create();
        lb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                x = loginResult.getAccessToken().getToken();
                idOfUser = loginResult.getAccessToken().getUserId();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, SuggestFb.class);
        intent.putExtra("token", x);
        intent.putExtra("userid", idOfUser);
        startActivity(intent);

    }

   /* public void wtf(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://graph.facebook.com/" + PAGE_ID + "/members?access_token=" + x));
        startActivity(intent);
    }*/
}
