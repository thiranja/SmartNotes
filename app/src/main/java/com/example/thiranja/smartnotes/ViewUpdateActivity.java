package com.example.thiranja.smartnotes;

//import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ShareActionProvider;
import android.widget.Toast;


public class ViewUpdateActivity extends AppCompatActivity {

    // Making the db helper class object to make queries

    DBHelper helper = new DBHelper(this);

    EditText nameet;
    EditText noteet;

    private String id;

    // Making a toolbar object

    Toolbar viewToolbar;

    // Making a shareActionProvider to send notes

    //private ShareActionProvider shareProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update);

        // Setting the name and the notes edittext with the java class

        nameet = (EditText) findViewById(R.id.viewnameet);
        noteet = (EditText) findViewById(R.id.viewnoteet);


        // Setting the button update with the java class

        Button update = (Button) findViewById(R.id.updatebtn);

        // Receiving the data from the intent

        Bundle bundle = getIntent().getExtras();

        final String idStr = bundle.getString("id");

        this.id = idStr;
        // Getting the note name from the database
        String name = helper.getName(idStr);;
        String note  = helper.getNote(idStr);



        // Setting the Strings to the edittexts
        nameet.setText(name);
        noteet.setText(note);

        // Setting the watcher to edittext after setting exixting text
        nameet.addTextChangedListener(watcher);
        noteet.addTextChangedListener(watcher);

        // Setting update on the click

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                boolean isUpdated;

                note.setId(idStr);
                note.setName(nameet.getText().toString());
                note.setDate();

                if (noteet.getText().toString().isEmpty()){
                    isUpdated = false;
                }else {
                    note.setNote(noteet.getText().toString());
                    isUpdated = helper.updateNote(note);
                }
                if (isUpdated){
                    Toast.makeText(ViewUpdateActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                    // Starting the home activity after the update of the note
                    Intent home = new Intent("android.intent.action.HOME");
                    startActivity(home);
                }else {
                    Toast.makeText(ViewUpdateActivity.this, "Note can not be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

        viewToolbar = (Toolbar) findViewById(R.id.view_ab);
        setSupportActionBar(viewToolbar);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelected = item.getItemId();

        if (itemSelected == R.id.view_ab_delete){
            boolean isDeleted = helper.deleteNote(id);
            if (isDeleted){
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Note did not Deleted", Toast.LENGTH_SHORT).show();
            }
            // Starting the home activity after the update of the note
            Intent home = new Intent("android.intent.action.HOME");
            startActivity(home);

            //Toast.makeText(this, "Delete note presed", Toast.LENGTH_SHORT).show();


        }else if(itemSelected == R.id.view_ab_update){
            Note note = new Note();
            boolean isUpdated;

            note.setId(id);
            note.setName(nameet.getText().toString());
            note.setDate();

            if (noteet.getText().toString().isEmpty()){
                isUpdated = false;
            }else {
                note.setNote(noteet.getText().toString());
                isUpdated = helper.updateNote(note);
            }
            if (isUpdated){
                Toast.makeText(ViewUpdateActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                // Starting the home activity after the update of the note
                Intent home = new Intent("android.intent.action.HOME");
                startActivity(home);
            }else {
                Toast.makeText(ViewUpdateActivity.this, "Note can not be empty", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this, "Update presed", Toast.LENGTH_SHORT).show();

        }else if(itemSelected == R.id.view_ab_send){
            // Code for sending notes comes here
            //Intent shareIntent = new Intent();
            //shareIntent.setAction(Intent.ACTION_SEND);
            //shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello ha");
            //shareIntent.putExtra(Intent.EXTRA_TEXT, "No its too bad");
            //shareIntent.setType("text/plain");

            //shareProvider = (ShareActionProvider) item.getActionProvider();
            //shareProvider.setShareIntent(shareIntent);
            //startActivity(shareIntent);
        }else if(itemSelected == R.id.view_ab_reminder){
            // Code for setting reminders comes here
            ReminderDialog reminderDialog = ReminderDialog.newInstanse(id);
            reminderDialog.show(getFragmentManager(), "fragment_alert");
        }
        return super.onOptionsItemSelected(item);
    }




    // Adding the boolean flag and a watcher to detect changes on edittext
    private boolean isChangesSaved = true;

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            isChangesSaved = false;

        }
    };


    @Override
    public void onBackPressed() {
        if (isChangesSaved == true) {
            Intent home = new Intent("android.intent.action.HOME");
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
        }else {
            Note note = new Note();
            boolean isUpdated;

            note.setId(id);
            note.setName(nameet.getText().toString());
            note.setDate();

            if (noteet.getText().toString().isEmpty()){
                isUpdated = false;
            }else {
                note.setNote(noteet.getText().toString());
                isUpdated = helper.updateNote(note);
            }
            if (isUpdated){
                Toast.makeText(ViewUpdateActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                isChangesSaved = true;

            }else {
                Toast.makeText(ViewUpdateActivity.this, "Note can not be empty", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
