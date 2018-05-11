package com.example.thiranja.smartnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by thiranja on 3/21/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Mapping the database with names needed for addresing the database

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "note.db";
    private static final String TABLE_NAME = "notes";
    private static final String C_ID = "id";
    private static final String C_NAME = "name";
    private static final String C_NOTE = "note";
    private static final String C_PASS = "pass";
    private static final String C_CDATE = "cdate";
    private static final String C_MDATE = "mdate";

    // Creating a database object

    private SQLiteDatabase db;

    // Writing the quries

    private static final String TABLE_CREATE = "CREATE TABLE notes(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "note TEXT NOT NULL," +
            "pass INT NOT NULL," +
            "cdate TEXT NOT NULL," +
            "mdate TEXT NOT NULL)";

    // Writing the constructor for the DBHelper

    DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS notes";
        db.execSQL(query);
        this.onCreate(db);
    }

    public void insertNote(Note note){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NAME, note.getName());
        values.put(C_NOTE, note.getNote());
        values.put(C_PASS, note.getPass());
        values.put(C_CDATE,note.getDate());
        values.put(C_MDATE,note.getDate());

        db.insert(TABLE_NAME, null,values);
        db.close();
    }

    public String getName(String id){
        db = this.getReadableDatabase();
        String query = "SELECT "+C_ID+","+C_NAME + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        String name = "Invalid name";
        String dbid;
        do{
            dbid = cursor.getString(0);
            if (dbid.equalsIgnoreCase(id)){
                name = cursor.getString(1);
                break;
            }
        }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return name;
    }

    public String getNote(String id){
        db = this.getReadableDatabase();
        String query = "SELECT "+C_ID+","+C_NOTE + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        String note = "Invalid Note";
        String dbid;
        do{
            dbid = cursor.getString(0);
            if (dbid.equalsIgnoreCase(id)){
                note = cursor.getString(1);
                break;
            }
        }while (cursor.moveToNext());
        cursor.close();
        db.close();
        return note;
    }

    public boolean updateNote(Note note){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(C_NAME,note.getName());
        values.put(C_NOTE,note.getNote());
        values.put(C_MDATE,note.getDate());

        boolean isUpdated = db.update(TABLE_NAME,values,C_ID + "=" + note.getId(), null) > 0;
        db.close();

        return isUpdated;

    }

    public boolean deleteNote(String id){
        db = this.getWritableDatabase();
        boolean isDeleted = db.delete(TABLE_NAME,C_ID + "=" + id,null) > 0;
        db.close();
        return isDeleted;
    }

    public boolean sendToTrash(String id){
        db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(C_PASS, 1);
        boolean isUpdated = db.update(TABLE_NAME,values,C_ID + "=" + id, null) > 0;
        db.close();
        return isUpdated;
    }

    public boolean recoverFromTrash(String id){
        db  = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(C_PASS, 0);
        boolean isUpdated = db.update(TABLE_NAME,values,C_ID + "=" + id, null) > 0;
        db.close();
        return isUpdated;
    }

    public ArrayList<Note> getNoteArray(){
        db = this.getReadableDatabase();

        String query = "SELECT " + C_ID + "," +C_NAME + "," + C_NOTE + "," + C_PASS + "," + C_CDATE + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);

        boolean isMoved = cursor.moveToLast();
        ArrayList<Note> notes = new ArrayList<>();
        if(isMoved) {
            do {
                if (cursor.getInt(3) == 0) {
                    Note note = new Note();

                    note.setId(cursor.getString(0));
                    note.setName(cursor.getString(1));
                    note.setNote(cursor.getString(2));
                    note.setPass(cursor.getInt(3));
                    note.setDate(cursor.getString(4));

                    notes.add(note);
                }

            } while (cursor.moveToPrevious());

            cursor.close();
            db.close();
        }else{
            Note guideNote = new Note();
            guideNote.setDate();
            guideNote.setId("zzz");
            guideNote.setName("<-- Empty -->");
            guideNote.setNote("Nothing want to set here");
            guideNote.setPass(0);
            notes.add(guideNote);
        }
        return notes;
    }

    public ArrayList<Note> getTrashNoteArray(){
        db = this.getReadableDatabase();

        String query = "SELECT " + C_ID + "," +C_NAME + "," + C_NOTE + "," + C_PASS + "," + C_CDATE + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);

        boolean isMoved = cursor.moveToLast();
        ArrayList<Note> notes = new ArrayList<>();
        if(isMoved) {
            do {
                if (cursor.getInt(3) == 1) {
                    Note note = new Note();

                    note.setId(cursor.getString(0));
                    note.setName(cursor.getString(1));
                    note.setNote(cursor.getString(2));
                    note.setPass(cursor.getInt(3));
                    note.setDate(cursor.getString(4));

                    notes.add(note);
                }

            } while (cursor.moveToPrevious());

            cursor.close();
            db.close();
        }else{
            Note guideNote = new Note();
            guideNote.setDate();
            guideNote.setId("zzz");
            guideNote.setName("<-- Empty -->");
            guideNote.setNote("Nothing want to set here");
            guideNote.setPass(0);
            notes.add(guideNote);
        }
        return notes;
    }



    public ArrayList<Note> getNoteArrayMD(){
        db = this.getReadableDatabase();

        String query = "SELECT " + C_ID + "," +C_NAME + "," + C_NOTE + "," + C_PASS + "," + C_MDATE + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        boolean isMoved = cursor.moveToLast();

        ArrayList<Note> notes = new ArrayList<>();
        if(isMoved) {
            do {
                Note note = new Note();

                note.setId(cursor.getString(0));
                note.setName(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setPass(cursor.getInt(3));
                note.setDate(cursor.getString(4));

                notes.add(note);

            } while (cursor.moveToPrevious());

            cursor.close();
            db.close();
        }else{
            Note guideNote = new Note();
            guideNote.setDate();
            guideNote.setId("zzz");
            guideNote.setName("<-- Empty -->");
            guideNote.setNote("Nothig want to set here");
            guideNote.setPass(0);
            notes.add(guideNote);
        }
        return notes;
    }

    public String getLastNoteId(){
        db = this.getReadableDatabase();
        String query = "SELECT "+C_ID + " FROM " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        boolean isMoved = cursor.moveToLast();
        String id = "error";
        if(isMoved){
            id = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return id;
    }

}
