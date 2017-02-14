package com.facebookapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;

public class MainActivity extends AppCompatActivity {
    LoginButton lb;
    TextView tv;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lb = (LoginButton) findViewById(R.id.button_fb);
        tv = (TextView) findViewById(R.id.text);
        callbackManager = CallbackManager.Factory.create();
        lb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tv.setText("login successful\n" + loginResult.getAccessToken().getApplicationId() + "\n" +
                        loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                tv.setText("login fail");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void gogo(View view) {
        Bundle params = new Bundle();
        params.putString("message", "This is a test message");
/* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{1357616140975878}/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();
    }
}
