package com.srdeveloppement.atelier.mymessenger;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;
import java.util.List;

import static com.srdeveloppement.atelier.mymessenger.ChatActivity.Disc;
import static com.srdeveloppement.atelier.mymessenger.ChatActivity.messageQuerry;

/**
 * Created by lhadj on 12/11/2016.
 */

public class LesteningRequests extends Thread {


    EditText area;
    Button send;
    Handler hn ;
    MyAdapter adapter =null;
    RecyclerView recyclerView = null;
    byte[] buffer =null;
    DatagramSocket socketRecieve;
    EmmeteurIP ip ;
    String myIP;
    MessageQuerry msg ;
    WifiManager wm;
    SharedPreferences sharedPreferences;

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public LesteningRequests(MyAdapter adapter, RecyclerView recyclerView, EditText area, Button send, EmmeteurIP ip, WifiManager wm, MessageQuerry msg1, SharedPreferences sharedPreferences) {
        buffer = new byte[6000];
        this.adapter=adapter;
        this.recyclerView = recyclerView;
        this.area= area;
        this.send=send;
        hn = new Handler();
        this.ip =ip;
        this.msg=msg1;
        this.sharedPreferences=sharedPreferences;
        this.wm=wm;
    }

    @Override
    public void run() {


        try {
            socketRecieve = new DatagramSocket(9999);
            DatagramPacket rPacket= new DatagramPacket(buffer,buffer.length);
            while(true){
                socketRecieve.receive(rPacket);
                String packetIP = rPacket.getAddress().toString();
                String myIP = "/"+ Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                if(myIP.equals("/0.0.0.0")){
                    myIP="/192.168.43.1";
                }

                if(!packetIP.equals(myIP)){
                    ip.setAdr(rPacket.getAddress());
                    byte[] messageByte = rPacket.getData();
                    Object mObject;
                    mObject=deserialize(messageByte);
                    msg=(MessageQuerry)mObject;
                    ChatActivity.messageQuerry.setMessage(msg.getMessage());
                    ChatActivity.messageQuerry.setFileName(msg.getFileName());
                    ChatActivity.messageQuerry.setQuerry(msg.getQuerry());

                    switch (msg.getQuerry()){
                        case 1 :
                            Gson gson = new Gson();
                            String response=sharedPreferences.getString("MyConversation" , "");
                            if(!response.equals("")){
                                Disc=gson.fromJson(response, new TypeToken<List<Discution>>()
                                {
                                }.getType());
                            }
                            Disc.add(new Discution(Calendar.getInstance(), msg.getMessage(),true,"",false,messageQuerry.getFileFormat(),new File("")));
                            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                            gson = new Gson();
                            String json = gson.toJson(Disc);
                            prefsEditor.putString("MyConversation", json);
                            prefsEditor.commit();
                            hn.post(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.loadNewData(Disc);
                                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                                    area.setText("");
                                    send.setBackgroundResource(R.drawable.ic_send_off);
                                }
                            });
                            break;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socketRecieve.close();
        }


    }
}