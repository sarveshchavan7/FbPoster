package com.facebookapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SuggestFb extends Activity {
    Button joinGroup, postOnFb;
    TextView topText, notice, notice2,textQuestion1;
    private static final String PAGE_ID = "288637851555189";
    RequestQueue requestQueue;
    EditText textQuestion;
    String y;
    String xx;
    ImageView imageViewQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestfb);
        joinGroup = (Button) findViewById(R.id.joinGroup);
        postOnFb = (Button) findViewById(R.id.postOnFb);
        topText = (TextView) findViewById(R.id.topText);
        notice = (TextView) findViewById(R.id.notice);
        notice2 = (TextView) findViewById(R.id.notice2);
        requestQueue = Volley.newRequestQueue(this);
        textQuestion = (EditText) findViewById(R.id.textQuestion);
        imageViewQuestion=(ImageView)findViewById(R.id.imageViewQuestion);
        textQuestion1=(TextView)findViewById(R.id.textQuestion1);

        Bundle bundle = getIntent().getExtras();
        String x = bundle.getString("token");
        xx = x;
        Bundle bundle2 = getIntent().getExtras();
        String idOfUser = bundle2.getString("userid");
        y = idOfUser;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://graph.facebook.com/" + PAGE_ID + "/members?access_token=" + x,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (y.equals(jsonObject.get("id"))) {
                                    /*Toast.makeText(SuggestFb.this,"match found"+jsonObject.getString("id"),Toast.LENGTH_LONG).show();*/
                                    textQuestion.setVisibility(View.VISIBLE);
                                    postOnFb.setVisibility(View.VISIBLE);
                                    notice2.setVisibility(View.VISIBLE);
                                    joinGroup.setVisibility(View.GONE);
                                    notice.setText("Great!! your member of facebook group start posting");
                                    topText.setVisibility(View.GONE);
                                    imageViewQuestion.setVisibility(View.VISIBLE);
                                    textQuestion1.setVisibility(View.VISIBLE);
                                    break;
                                } else {
                                    textQuestion.setVisibility(View.GONE);
                                    postOnFb.setVisibility(View.GONE);
                                    notice2.setVisibility(View.GONE);
                                    joinGroup.setVisibility(View.VISIBLE);
                                    notice.setText("Note: Yoy can suggest questions after admin accepts your joining request. Thanks for your patient");
                                    topText.setVisibility(View.VISIBLE);
                                    imageViewQuestion.setVisibility(View.GONE);
                                    textQuestion1.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SuggestFb.this, "Please check your network connection and try again.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/charetaker/"));
                startActivity(intent);
            }
        });
        postOnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isOnline()){
                   String q="#Q";
                   final String text =q+" "+textQuestion.getText().toString();
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


                                  if(text.length()<=114 && text.length()>=21){
                                      try {
                                          JSONArray data = JsonObject.getJSONArray("data");
                                          JSONObject objectdata = data.getJSONObject(0);
                                          String accessToken = xx;
                                          Log.d("AccessToken: ", accessToken);
                                          AccessToken accessToken_obj = new AccessToken(accessToken, AccessToken.getCurrentAccessToken().getApplicationId(),
                                                  AccessToken.getCurrentAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(),
                                                  AccessToken.getCurrentAccessToken().getDeclinedPermissions(), AccessToken.getCurrentAccessToken().getSource(),
                                                  AccessToken.getCurrentAccessToken().getExpires(), AccessToken.getCurrentAccessToken().getLastRefresh());
                                          new GraphRequest(accessToken_obj, "/" + PAGE_ID + "/feed", params, HttpMethod.POST,
                                                  new GraphRequest.Callback() {
                                                      public void onCompleted(GraphResponse response) {
                                                          if (response.getError() == null)
                                                              Toast.makeText(SuggestFb.this, "Question has been successfully posted in group.", Toast.LENGTH_LONG).show();
                                                          else {
                                                              Toast.makeText(SuggestFb.this, response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                                                              Log.d(response.getError().getErrorMessage(), "Error Message :");
                                                          }
                                                      }
                                                  }).executeAsync();
                                      } catch (JSONException j) {
                                          Log.d(j.getMessage(), "Error Message");
                                          Toast.makeText(SuggestFb.this, "Some error ocurred while posting.", Toast.LENGTH_LONG).show();
                                      }

                                  }else{
                                      Toast.makeText(SuggestFb.this,"Question is too short or long.",Toast.LENGTH_LONG).show();
                                  }

                               }

                           }
                   ).executeAsync();

               }else{
                    Toast.makeText(SuggestFb.this,"Check your internet connection and try agaian.",Toast.LENGTH_LONG).show();
               }
            }
        });


}
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
