package com.facebookapi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebookapi.application.AppController2;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    LoginButton lb;
    TextView tv;
    CallbackManager callbackManager;
    Button post;
    String x;
    private static final String PAGE_ID = "288637851555189";

    private static final String getURL = "https://api.myjson.com/bins/8wgo1";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lb = (LoginButton) findViewById(R.id.button_fb);
        tv = (TextView) findViewById(R.id.text);
        post = (Button) findViewById(R.id.post);
        requestQueue=Volley.newRequestQueue(this);

        callbackManager = CallbackManager.Factory.create();
        lb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tv.setText("login successful\n" + loginResult.getAccessToken().getUserId() + "\n" + loginResult.getAccessToken().getToken());
                x = loginResult.getAccessToken().getToken();

            }


            @Override
            public void onCancel() {
                tv.setText("login fail");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String text = "yomen";
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/accounts",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Bundle params = new Bundle();
                                params.putString("message", text);

                                JSONObject JsonObject = response.getJSONObject();
                                try {
                                    JSONArray data = JsonObject.getJSONArray("data");
                                    JSONObject objectdata = data.getJSONObject(0);
                                    String accessToken = x;
                                    Log.d("AccessToken: ", accessToken);
                                    AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                                            AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                                            AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                                            AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());
                                    new GraphRequest(accessToken_obj, "/" + PAGE_ID + "/feed", params, HttpMethod.POST,
                                            new GraphRequest.Callback() {
                                                public void onCompleted(GraphResponse response) {
                                                    Toast.makeText(MainActivity.this, "called", Toast.LENGTH_SHORT).show();
                                                    if (response.getError() == null)

                                                        Toast.makeText(MainActivity.this, "Shared on facebook page.", Toast.LENGTH_LONG).show();
                                                    else {
                                                        Toast.makeText(MainActivity.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                                                        Log.d(response.getError().getErrorMessage(), "Error Message :");
                                                    }
                                                }
                                            }).executeAsync();
                                } catch (JSONException j) {
                                    Log.d(j.getMessage(), "Error Message");
                                   /* Toast.makeText(MainActivity.this, j.getMessage(), Toast.LENGTH_SHORT).show();*/
                                    Toast.makeText(MainActivity.this, "you should join group before posting", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                ).executeAsync();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    public void wtf(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://graph.facebook.com/" + PAGE_ID + "/members?access_token=" + x));
        startActivity(intent);
    }

    public void jason(View view) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                "https://graph.facebook.com/" + PAGE_ID + "/members?access_token=" + x,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Toast.makeText(MainActivity.this,jsonObject.getString("id"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"fuck bitches",Toast.LENGTH_LONG).show();
            }
        }
        );requestQueue.add(jsonObjectRequest);


    }

}
