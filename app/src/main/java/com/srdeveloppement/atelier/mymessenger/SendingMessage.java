package com.srdeveloppement.atelier.mymessenger;

import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by lhadj on 12/11/2016.
 */

public class SendingMessage extends Thread {


    DatagramSocket socket = null;
    DatagramPacket packet= null;
    Handler hn ;
    MessageQuerry qMessage ;
    EmmeteurIP em ;
    String path ;

    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public SendingMessage(MessageQuerry qMessage,EmmeteurIP em) {
        this.hn = new Handler();
        this.qMessage=qMessage;
        this.em=em;
    }

    public SendingMessage(MessageQuerry qMessage,String pathFile,EmmeteurIP em) {
        this.hn = new Handler();
        this.qMessage=qMessage;
        this.path=pathFile;
        this.em=em;
    }


    @Override
    public void run() {
        try {
            Object s =qMessage ;
            byte[] msgByte= serialize(s);


            switch (qMessage.getQuerry()){
                case 1 :
                    socket = new DatagramSocket();
                    packet = new DatagramPacket(msgByte,msgByte.length, InetAddress.getByName("192.168.43.255"),9999);
                    socket.setBroadcast(true);
                    socket.send(packet);
                    break;
                case 2 :
                    socket = new DatagramSocket();
                    packet = new DatagramPacket(msgByte,msgByte.length, InetAddress.getByName("192.168.43.255"),9999);
                    socket.setBroadcast(true);
                    socket.send(packet);
                    sleep(1000);
                    Socket sock = new Socket(em.getAdr(),1999);
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
                    BufferedOutputStream out = new BufferedOutputStream(sock.getOutputStream());
                    while(true){
                        int line = in.read();
                        if(line<0)
                            break;
                        out.write(line);
                    }
                    ChatActivity.messageQuerry.setQuerry(-1);
                    ChatActivity.messageQuerry.setFileName("");
                    ChatActivity.messageQuerry.setMessage("");
                    in.close();
                    out.close();
                    sock.close();
                    break;
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
