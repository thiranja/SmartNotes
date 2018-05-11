package com.example.thiranja.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DBHelper helper = new DBHelper(this);


    Toolbar homeTB;

    Spinner sortSpinner;

    ArrayList<Note> notelist;

    CustomArrayAdapter customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting up the buttens

        Button addbtn = findViewById(R.id.addbtn);

        // setting new note activity to addbtn

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setting the action intent of add new activity
                Intent add = new Intent("android.intent.action.ADDNOTE");
                startActivity(add);
            }
        });

        // Setting the list view with the java Class

        final ListView noteList = findViewById(R.id.notelv);


        // Testing with custom Array Apadter


        notelist = helper.getNoteArray();

        customArrayAdapter = new CustomArrayAdapter(this,notelist);
        customArrayAdapter.notifyDataSetChanged();

        noteList.setAdapter(customArrayAdapter);

        // Registering the listview for a context menu
        registerForContextMenu(noteList);

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView idtv = view.findViewById(R.id.rowidtv);
                String idStr = idtv.getText().toString();
                if(!(idStr.equalsIgnoreCase("zzz"))) {
                    Intent update = new Intent("android.intent.action.UPDATE");
                    update.putExtra("id", idStr);
                    startActivity(update);
                }
            }
        });

        /*noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idtv = view.findViewById(R.id.rowidtv);
                String idStr = idtv.getText().toString();
                if(!(idStr.equalsIgnoreCase("zzz"))) {
                    DeleteDialog alertDialog = DeleteDialog.newInstance(idStr);
                    alertDialog.show(getFragmentManager(), "fragment_alert");
                }
                // if return false normal click also execute so need to
                // retrun ture to only execute long click..
                return true;
            }
        });*/

        homeTB = (Toolbar) findViewById(R.id.home_ab);
        setSupportActionBar(homeTB);

        // Setting the Navigation bar on create


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, homeTB, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // setting spinner for sort with the home activity
        sortSpinner = (Spinner)findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);

        // Setting the spinner layout design
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                NoteSorter sorter = new NoteSorter();
                if(i == 1) {
                    ArrayList<Note> newNotes = helper.getNoteArrayMD();
                    notelist.clear();
                    notelist.addAll(newNotes);
                    sorter.sortByModifiedDate(notelist);
                }else if(i == 2){
                    // Here no database operations have to be perform only need to sort
                    sorter.sortByAlphabatically(notelist);
                }else{
                    ArrayList<Note> newNotes = helper.getNoteArray();
                    notelist.clear();
                    notelist.addAll(newNotes);
                    // No sort is needed here
                }
                customArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Loading defalt value of settings for activity

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        AdView homeAdview = (AdView) findViewById(R.id.home_banner_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        homeAdview.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Code comes here

        // Getting the user choice
        int itemSel = item.getItemId();

        if (itemSel == R.id.home_ab_add){
            Intent add = new Intent(this, AddNoteActivity.class);
            startActivity(add);
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isPressTwice = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(isPressTwice == true) {
                // These lines are required to deal with back stac
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // Finish the activity and exit from the application
                finish();
                System.exit(0);
            }

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            isPressTwice = true;

            Thread pressTwice = new Thread(){
                public void run(){
                    try{
                        sleep(2000);
                        isPressTwice = false;
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            };
            pressTwice.start();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            // want to do nothing
            //Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_trash) {
            Intent trashIntent = new Intent(this,TrashActivity.class);
            startActivity(trashIntent);

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_rate) {

        }else if(id == R.id.nav_like){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.notelv){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.home_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.home_cm_delete:
                TextView idtv = info.targetView.findViewById(R.id.rowidtv);
                String idStr = idtv.getText().toString();
                if (!(idStr.equalsIgnoreCase("zzz"))) {
                    DeleteDialog alertDialog = DeleteDialog.newInstance(idStr);
                    alertDialog.show(getFragmentManager(), "fragment_alert");
                    /*synchronized (alertDialog) {
                        try {
                            alertDialog.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        notelist.remove(info.position);
                        customArrayAdapter.notifyDataSetChanged();
                    }*/

                return true;
            }
            default:
                return super.onContextItemSelected(item);

        }
    }


}
