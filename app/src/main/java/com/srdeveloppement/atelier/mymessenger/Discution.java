package com.srdeveloppement.atelier.mymessenger;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sam on 08/03/2016.
 */
public class Discution implements Serializable {
    boolean senderVS;
    private String senderText;
    boolean reciverVS;
    private String reciverText;
    private String path_File;
    Calendar c;
    File ImagePath;
    int hour;
    int minute;


    public void setPath_File(String path_File) {
        this.path_File = path_File;
    }

    public String getPath_File() {

        return path_File;
    }

    public File getImagePath() {
        return ImagePath;
    }

    public Discution(Calendar c, String reciverText, boolean reciverVS, String senderText, boolean senderVS, String path_File, File ImagePath) {
        this.c = c;
        Calendar.getInstance();
        // int hour = c.get(Calendar.HOUR);
        // int minute = c.get(Calendar.MINUTE);
        this.reciverText = reciverText;
        this.reciverVS = reciverVS;
        this.senderVS = senderVS;
        this.senderText = senderText;

        this.path_File=path_File;
        this.ImagePath=ImagePath;
    }

    public Calendar getC() {
        return c;
    }

    public String getReciverText() {
        return reciverText;
    }

    public boolean isReciverVS() {
        return reciverVS;
    }

    public String getSenderText() {
        return senderText;
    }

    public boolean isSenderVS() {
        return senderVS;
    }

    public void setC(Calendar c) {
        this.c = c;
    }

    public void setReciverText(String reciverText) {
        this.reciverText = reciverText;
    }

    public void setReciverVS(boolean reciverVS) {
        this.reciverVS = reciverVS;
    }

    public void setSenderText(String senderText) {
        this.senderText = senderText;
    }

    public void setSenderVS(boolean senderVS) {
        this.senderVS = senderVS;
    }
}
