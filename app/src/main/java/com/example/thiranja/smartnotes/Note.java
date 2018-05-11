package com.example.thiranja.smartnotes;

import java.util.Date;

/**
 * Created by thiranja on 3/21/18.
 */

public class Note {
    String id;
    String name;
    String note;
    int pass;
    String date;

    void setId(String id){
        this.id = id;
    }
    String getId(){
        return this.id;
    }
    void setName(String name){
        this.name = name;
    }
    String getName(){
        return this.name;
    }
    void setNote(String note){
        this.note = note;
    }
    String getNote(){
        return this.note;
    }
    void setPass(int pass){
        this.pass = pass;
    }
    int getPass(){
        return this.pass;
    }
    void setDate(){
        String dateArray[] = new Date().toString().split(" ");
        String dateStr = dateArray[2] +" "+ dateArray[1] +" "+ dateArray[5];
        this.date = dateStr;
    }
    void setDate(String date){
        this.date = date;
    }
    String getDate(){
        return this.date;
    }


}
