package com.srdeveloppement.atelier.mymessenger;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity {


    public static InetAddress EmeteurIP;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 100;
    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGEW = 101;
    private LinearLayoutManager mLayoutManager;
    EditText area;
    LinearLayout LL;
    EmmeteurIP em= new EmmeteurIP(null);
    Button send;
    public static String emojiValue;
    String SenderMsg;
    Intent intent;
    Gson gson;
    TextView senderTv;
    TextView reciverTv;
    String response;
    String json;
    public static MessageQuerry messageQuerry;
    Button sendFileBTN;
    ToggleButton closeOpen;
    File fileToSend;
    SharedPreferences sharedPref;
    ImageView em1, em2, em3, em4, em5, em6, em7;
    LinearLayout emojiPanel;
    public static ArrayList<Discution> Disc ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void setEmeteurIP(InetAddress emeteurIP) {
        EmeteurIP = emeteurIP;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            fileToSend = new File(filePath);
            messageQuerry.setFileName(fileToSend.getName());
            messageQuerry.setFileFormat(fileToSend.getParent());
            messageQuerry.setQuerry(2);
            messageQuerry.setMessage(fileToSend.getName());
            // Do anything with fileToSend
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.chat);
        emojiPanel = (LinearLayout) findViewById(R.id.emojiPanel);
        LL = (LinearLayout) findViewById(R.id.LL);
        em2 = (ImageView) findViewById(R.id.em2);
        em3 = (ImageView) findViewById(R.id.em3);
        em4 = (ImageView) findViewById(R.id.em4);
        em5 = (ImageView) findViewById(R.id.em5);
        em6 = (ImageView) findViewById(R.id.em6);
        em7 = (ImageView) findViewById(R.id.em7);
        messageQuerry = new MessageQuerry("",-1,"","");
        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"MyMessenger");
        directory.mkdirs();
        closeOpen = (ToggleButton)findViewById(R.id.closeOpen);
        closeOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    LL.setBackgroundColor(0x80000000);
                    emojiPanel.setVisibility(View.VISIBLE);
                } else {

                    LL.setBackgroundColor(0x00000000);
                    emojiPanel.setVisibility(View.GONE);

                }
            }
        });






        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_EXTERNAL_STORAGEW);
        }


        send = (Button) findViewById(R.id.send);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        area = (EditText) findViewById(R.id.my_edit_text);

        Disc = new ArrayList<Discution>();
        gson = new Gson();
        response=sharedPref.getString("MyConversation" , "");
        if(!response.equals("")){
            Disc=gson.fromJson(response, new TypeToken<List<Discution>>()
            {
            }.getType());
        }

        mAdapter = new MyAdapter(Disc);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!mAdapter.getmDiscution().get(position).getPath_File().equals("")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/MyMessenger/");
                    intent.setDataAndType(uri, "*/*");
                    startActivity(Intent.createChooser(intent, "Open folder"));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));



        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        LesteningRequests requests =new LesteningRequests(mAdapter,mRecyclerView,area,send,em,wm,messageQuerry,sharedPref);
        requests.start();
        ListeningTCP ft = new ListeningTCP(new Handler(),mAdapter,mRecyclerView,area,send,messageQuerry);
        ft.start();
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
        area.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("key", "onTextChanged start :" + start + "  end :" + count);
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                send.setBackgroundResource(R.drawable.ic_send_off);
                Log.d("key", "beforeTextChanged start :" + start + "  after :" + after);
            }

            public void afterTextChanged(Editable s) {
                if (area.getText().toString().trim().equals("")) {
                    send.setBackgroundResource(R.drawable.ic_send_off);
                } else {
                    send.setBackgroundResource(R.drawable.ic_send);

                    send.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (isEmpty(area)) {

                            } else {
                                if(messageQuerry.getQuerry()==2){
                                    SenderMsg = area.getText().toString();
                                    Disc = new ArrayList<Discution>();
                                    gson = new Gson();
                                    response=sharedPref.getString("MyConversation" , "");
                                    if(!response.equals("")){
                                        Disc=gson.fromJson(response, new TypeToken<List<Discution>>()
                                        {
                                        }.getType());
                                    }

                                    String format = messageQuerry.getFileName().split("\\.",2)[1];
                                    if(format.equals("png") || format.equals("jpg"))
                                    Disc.add(new Discution(Calendar.getInstance(), "", false, messageQuerry.getFileName(), true,messageQuerry.getFileFormat(),fileToSend));
                                    SharedPreferences.Editor prefsEditor = sharedPref.edit();
                                    gson = new Gson();
                                    json = gson.toJson(Disc);
                                    prefsEditor.putString("MyConversation", json);
                                    prefsEditor.commit();
                                    mAdapter.loadNewData(Disc);
                                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                    String message = area.getText().toString();
                                    messageQuerry.setMessage(messageQuerry.getMessage()+""+message);
                                    area.setText("");
                                    send.setBackgroundResource(R.drawable.ic_send_off);
                                    SendingMessage msg = new SendingMessage(messageQuerry,fileToSend.getAbsolutePath(),em);
                                    msg.start();
                                }else{
                                    messageQuerry.setMessage(area.getText().toString());
                                    messageQuerry.setFileFormat("");
                                    messageQuerry.setFileName("");
                                    messageQuerry.setQuerry(1);
                                    SenderMsg = area.getText().toString();
                                    Disc = new ArrayList<Discution>();
                                    gson = new Gson();
                                    response=sharedPref.getString("MyConversation" , "");
                                    if(!response.equals("")){
                                        Disc=gson.fromJson(response, new TypeToken<List<Discution>>()
                                        {
                                        }.getType());
                                    }
                                    Disc.add(new Discution(Calendar.getInstance(), "", false, SenderMsg, true,messageQuerry.getFileFormat(),new File("")));
                                    SharedPreferences.Editor prefsEditor = sharedPref.edit();
                                    gson = new Gson();
                                    json = gson.toJson(Disc);
                                    prefsEditor.putString("MyConversation", json);
                                    prefsEditor.commit();
                                    mAdapter.loadNewData(Disc);
                                    mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                                    area.setText("");
                                    send.setBackgroundResource(R.drawable.ic_send_off);
                                    SendingMessage msg = new SendingMessage(messageQuerry,"",em);
                                    msg.start();
                                }
                            }
                        }
                    });
                }
                Log.d("key", "afterTextChange last char" + s);
            }

        });


        intent = new Intent(ChatActivity.this, MyAdapter.class);


        Disc = new ArrayList<Discution>();

        senderTv = (TextView) findViewById(R.id.senderText);
        reciverTv = (TextView) findViewById(R.id.reciverText);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void addEmoji(Drawable drawable) {
        // drawable.setBounds(0, 0, 63, 63);

        int selectionCursor = area.getSelectionStart();
        area.getText().insert(selectionCursor, emojiValue);
        selectionCursor = area.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(area.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - emojiValue.length(), selectionCursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        area.setText(builder);
        area.setSelection(selectionCursor);
    }


    boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void setEmoji(View view) {
        int imId = view.getId();
        switch (imId) {
            case R.id.em2:
                emojiValue = ":tongue:";
                addEmoji(em2.getDrawable());
                break;
            case R.id.em3:
                emojiValue = ":glasses:";
                addEmoji(em3.getDrawable());
                break;
            case R.id.em4:
                emojiValue = ":sad:";
                addEmoji(em4.getDrawable());
                break;
            case R.id.em5:
                emojiValue = ":angry:";
                addEmoji(em5.getDrawable());
                break;
            case R.id.em6:
                emojiValue = ":laugh:";
                addEmoji(em6.getDrawable());
                break;
            case R.id.em7:
                emojiValue = ":happy:";
                addEmoji(em7.getDrawable());
                break;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Disc = new ArrayList<Discution>();
            mAdapter.loadNewData(Disc);
            SharedPreferences.Editor prefsEditor = sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(Disc);
            prefsEditor.putString("MyConversation", json);
            prefsEditor.commit();
            return true;
        }
        if (id == R.id.FileButton) {
            new MaterialFilePicker()
                    .withActivity(ChatActivity.this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.*")) // Filtering files and directories by file name using regexp
                    .withFilterDirectories(true) // Set directories filterable (false by default)
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Chat Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
