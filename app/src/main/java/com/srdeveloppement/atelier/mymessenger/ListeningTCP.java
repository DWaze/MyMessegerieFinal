package com.srdeveloppement.atelier.mymessenger;

import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

import static com.srdeveloppement.atelier.mymessenger.ChatActivity.messageQuerry;

/**
 * Created by lhadj on 12/12/2016.
 */

public class ListeningTCP extends Thread {
    Handler hn ;
    MyAdapter adapter;
    RecyclerView recyclerView;
    EditText area;
    Button send;
    MessageQuerry msg ;

    public ListeningTCP(Handler hn, MyAdapter adapter, RecyclerView recyclerView, EditText area, Button send, MessageQuerry msg) {
        this.hn = hn;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.area = area;
        this.send = send;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            while (true){
                ServerSocket socket = new ServerSocket(1999);
                Socket s = socket.accept();
                BufferedInputStream in = new BufferedInputStream(s.getInputStream());
                File Source = Environment.getExternalStorageDirectory();
                String Path = Source.getAbsolutePath()+"/MyMessenger/"+msg.getFileName();
                File file = new File(Path);
                while (file.isDirectory()){
                    Path = Source.getAbsolutePath()+"/MyMessenger/"+msg.getFileName();
                    file = new File(Path);
                }
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                while (true){
                    int line = in.read();
                    if(line<0){
                        break;
                    }
                    out.write(line);
                }
                String format = msg.getFileName().split("\\.",2)[1];;
                if(format.equals("png")||format.equals("jpg"))
                ChatActivity.Disc.add(new Discution(Calendar.getInstance(), msg.getFileName()+msg.getFileFormat(),true,"",false,file.getAbsolutePath(),file));
                hn.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadNewData(ChatActivity.Disc);
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        area.setText("");
                        send.setBackgroundResource(R.drawable.ic_send_off);
                    }
                });
                messageQuerry.setQuerry(-1);
                messageQuerry.setFileName("");
                messageQuerry.setMessage("");
                in.close();
                out.close();
                socket.close();
                s.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
