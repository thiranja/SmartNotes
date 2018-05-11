package com.example.thiranja.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    //Creating the database helper object

    DBHelper help = new DBHelper(this);

    Toolbar addTb;

    EditText nameet;
    EditText noteet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // mapping the layout with code

        Button savebtn = (Button)findViewById(R.id.savebtn);
        nameet = (EditText)findViewById(R.id.nameet);
        noteet = (EditText)findViewById(R.id.noteet);

        addTb = (Toolbar) findViewById(R.id.add_ab);
        setSupportActionBar(addTb);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Creating the note object

                String namestr = nameet.getText().toString();
                String notestr = noteet.getText().toString();

                if (notestr.isEmpty()) {
                    noteet.setHint("Note can not be empty");
                } else {
                    //Calling the database insert function with note object

                    Note note = new Note();
                    note.setName(namestr);
                    note.setNote(notestr);
                    note.setPass(0);
                    note.setDate();
                    help.insertNote(note);
                    Intent home = new Intent("android.intent.action.HOME");
                    startActivity(home);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Code comes here for actions perform

        int itemSelected = item.getItemId();

        if (itemSelected == R.id.add_ad_save){

            //Creating the note object

            String namestr = nameet.getText().toString();
            String notestr = noteet.getText().toString();

            if (notestr.isEmpty()) {
                noteet.setHint("Note can not be empty");
            } else {
                //Calling the database insert function with note object

                Note note = new Note();
                note.setName(namestr);
                note.setNote(notestr);
                note.setPass(0);
                note.setDate();
                help.insertNote(note);
                Intent home = new Intent("android.intent.action.HOME");
                startActivity(home);
            }

        }else if(itemSelected == R.id.add_ab_discard){

            nameet.setText("");
            noteet.setText("");
            Toast.makeText(this, "Continue Unsaved", Toast.LENGTH_SHORT).show();
            Intent home = new Intent("android.intent.action.HOME");
            startActivity(home);

        }else if(itemSelected == R.id.add_ab_send){
            // Code for sending note comes here
        }else if(itemSelected == R.id.add_ab_reminder){
            // Code for setting reminders comes here
        }
        return super.onOptionsItemSelected(item);
    }


    // Adding a variable to check weather tried to save

    private boolean savedOnce = false;

    @Override
    public void onBackPressed() {
        String namestr = nameet.getText().toString();
        String notestr = noteet.getText().toString();

        if (notestr.isEmpty()){
            noteet.setHint("Note Can not be Empty");
            if (savedOnce == true){
                Toast.makeText(this, "Continue Unsaved", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
            }
            savedOnce = true;
        }else{
            Note note = new Note();
            note.setName(namestr);
            note.setNote(notestr);
            note.setPass(0);
            // Setting date as a String
            note.setDate();

            help.insertNote(note);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            String noteId = help.getLastNoteId();
            Intent update = new Intent("android.intent.action.UPDATE");
            update.putExtra("id",noteId);
            update.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(update);
        }
    }
    // End of the on back pressed method


}
