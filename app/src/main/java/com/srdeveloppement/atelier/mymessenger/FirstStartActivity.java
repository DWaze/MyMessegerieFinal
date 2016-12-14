package com.srdeveloppement.atelier.mymessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.srdeveloppement.atelier.mymessenger.VOIP.HomeActivity;
import com.srdeveloppement.atelier.mymessenger.VOIP.util.Constants;

public class FirstStartActivity extends AppCompatActivity {
    EditText mUsername;
    Button ppchat,vidchat;
    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;
    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGEW = 101;
    private static final int PERMISSIONS_REQUEST_RECORD = 102;
    private static final int PERMISSIONS_REQUEST_EXTERNAL_CAM = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED||
        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.MODIFY_AUDIO_SETTINGS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_EXTERNAL_STORAGEW);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_EXTERNAL_CAM);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.MODIFY_AUDIO_SETTINGS},
                    PERMISSIONS_REQUEST_EXTERNAL_CAM);
        }

        vidchat=(Button)findViewById(R.id.vidchat);
        ppchat=(Button)findViewById(R.id.ppchat);
        mUsername=(EditText)findViewById(R.id.username);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String lastUsername = extras.getString("oldUsername", "");
            mUsername.setText(lastUsername);
        }
        vidchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinChat();
            }
        });

        ppchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(myIntent);
            }
        });
    }
    private boolean validUsername(String username) {
        if (username.length() == 0) {
            mUsername.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            mUsername.setError("Username too long.");
            return false;
        }
        return true;
    }
    public void joinChat(){
        String username = mUsername.getText().toString();
        if (!validUsername(username))
            return;

        SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.USER_NAME, username);
        edit.apply();

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
