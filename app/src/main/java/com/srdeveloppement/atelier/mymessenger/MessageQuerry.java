package com.srdeveloppement.atelier.mymessenger;

import java.io.Serializable;

/**
 * Created by lhadj on 12/12/2016.
 */

public class MessageQuerry implements Serializable {
    private String message ;
    private int querry ;
    private String fileName;
    private String fileFormat;

    public MessageQuerry(String message, int querry, String fileName, String fileFormat) {
        this.message = message;
        this.querry = querry;
        this.fileName = fileName;
        this.fileFormat = fileFormat;
    }




    public String getMessage() {
        return message;
    }

    public int getQuerry() {
        return querry;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileFormat() {
        return fileFormat;
    }




    public void setMessage(String message) {
        this.message = message;
    }

    public void setQuerry(int querry) {
        this.querry = querry;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}
