package com.srdeveloppement.atelier.mymessenger.VOIP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;
import com.srdeveloppement.atelier.mymessenger.FirstStartActivity;
import com.srdeveloppement.atelier.mymessenger.R;
import com.srdeveloppement.atelier.mymessenger.VOIP.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    public static SharedPreferences mSharedPreferences;
    TextView mUsernameTV;
    EditText mCallNumET;
    // private Pubnub mPubNub;
    private String username;
    public ImageButton makeCallBT;

    private Pubnub mPubNub;
    /**
     * TODO: "Login" by subscribing to PubNub channel + Constants.SUFFIX
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Return to Log In screen if no user is logged in.
        if (!this.mSharedPreferences.contains(Constants.USER_NAME)){
            Intent intent = new Intent(this, FirstStartActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        this.username = this.mSharedPreferences.getString(Constants.USER_NAME, "");

        this.mCallNumET  = (EditText) findViewById(R.id.call_num);
        this.mUsernameTV = (TextView) findViewById(R.id.main_username);
        this.makeCallBT = (ImageButton) findViewById(R.id.makeCallBT);
        this.mUsernameTV.setText(this.username);  // Set the username to the username text view

        //TODO: Create and instance of Pubnub and subscribe to standby channel
        // In pubnub subscribe callback, send user to your VideoActivity
        initPubNub();
        makeCallBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void initPubNub() {
        String stdbyChannel = this.username + Constants.STDBY_SUFFIX;
        this.mPubNub = new Pubnub(Constants.PUB_KEY, Constants.SUB_KEY);
        this.mPubNub.setUUID(this.username);
        try {
            this.mPubNub.subscribe(stdbyChannel, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("MA-success", "MESSAGE: " + message.toString());
                    if (!(message instanceof JSONObject)) return; // Ignore if not JSONObject
                    JSONObject jsonMsg = (JSONObject) message;
                    try {
                        if (!jsonMsg.has(Constants.JSON_CALL_USER)) return;
                        String user = jsonMsg.getString(Constants.JSON_CALL_USER);
                        // Consider Accept/Reject call here
                        Intent intent = new Intent(HomeActivity.this, VideoChatActivity.class);
                        intent.putExtra(Constants.USER_NAME, username);
                        intent.putExtra(Constants.JSON_CALL_USER, user);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }
    public void signOut(){
        // TODO: Unsubscribe from all channels with PubNub object ( pn.unsubscribeAll() )
        SharedPreferences.Editor edit = this.mSharedPreferences.edit();
        edit.remove(Constants.USER_NAME);
        edit.apply();
        Intent intent = new Intent(this, FirstStartActivity.class);
        intent.putExtra("oldUsername", this.username);
        startActivity(intent);
    }
    public void makeCall() {
        String callNum = mCallNumET.getText().toString();
        if (callNum.isEmpty() || callNum.equals(this.username)) {
            Toast.makeText(this, "Enter a valid number.", Toast.LENGTH_SHORT).show();
        }
        dispatchCall(callNum);
    }
    public void dispatchCall(final String callNum) {
        final String callNumStdBy = callNum + Constants.STDBY_SUFFIX;
        JSONObject jsonCall = new JSONObject();
        try {
            jsonCall.put(Constants.JSON_CALL_USER, this.username);
            mPubNub.publish(callNumStdBy, jsonCall, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("MA-dCall", "SUCCESS: " + message.toString());
                    Intent intent = new Intent(HomeActivity.this, VideoChatActivity.class);
                    intent.putExtra(Constants.USER_NAME, username);
                    intent.putExtra(Constants.CALL_USER, callNum);
                    //mSharedPreferences.edit().putString("CALL_USER",mCallNumET.getText().toString());
                    //  intent.putExtra(Constants.CALL_USER, callNum);
                    //todo Call_User
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
