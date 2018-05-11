package com.example.thiranja.smartnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity {


    Toolbar trashTB;
    DBHelper helper = new DBHelper(this);

    ArrayList<Note> trashlist;
    CustomArrayAdapter customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        // Setting the toolbar

        trashTB = (Toolbar) findViewById(R.id.trash_ab);
        setSupportActionBar(trashTB);

        // Setting the trash list with the activity

        final ListView trashList = (ListView) findViewById(R.id.trashlv);

        //final ArrayList<Note> trashlist = helper.getTrashNoteArray();

        trashlist = helper.getTrashNoteArray();

        customArrayAdapter = new CustomArrayAdapter(this,trashlist);
        customArrayAdapter.notifyDataSetChanged();


        //registering context menu with trashlist
        registerForContextMenu(trashList);
        trashList.setAdapter(customArrayAdapter);

        trashList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idtv = view.findViewById(R.id.rowidtv);
                String idStr = idtv.getText().toString();
                if(!(idStr.equalsIgnoreCase("zzz"))) {
                    DeleteDialog alertDialog = DeleteDialog.newInstance(idStr);
                    alertDialog.show(getFragmentManager(), "fragment_alert");
                }

            }
        });

        /*trashList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView idtv = view.findViewById(R.id.rowidtv);
                String idStr = idtv.getText().toString();
                boolean isRecover = helper.recoverFromTrash(idStr);
                if(isRecover){
                    trashlist.remove(i);
                    Toast.makeText(TrashActivity.this, "Recovered", Toast.LENGTH_SHORT).show();
                    customArrayAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(TrashActivity.this, "Not Recovered", Toast.LENGTH_SHORT).show();
                }
                // if return false normal click also execute so need to
                // retrun ture to only execute long click..
                return true;
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.trashlv){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.trash_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TextView idtv = info.targetView.findViewById(R.id.rowidtv);
        String idStr = idtv.getText().toString();
        switch (item.getItemId()){
            case R.id.trash_cm_delete:
                if(!(idStr.equalsIgnoreCase("zzz"))) {
                    DeleteDialog alertDialog = DeleteDialog.newInstance(idStr);
                    alertDialog.show(getFragmentManager(), "fragment_alert");
                }
                trashlist.remove(info.position);
                customArrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.trash_cm_recover:
                boolean isRecover = helper.recoverFromTrash(idStr);
                if(isRecover){
                    trashlist.remove(info.position);
                    Toast.makeText(TrashActivity.this, "Recovered", Toast.LENGTH_SHORT).show();
                    customArrayAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(TrashActivity.this, "Not Recovered", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
