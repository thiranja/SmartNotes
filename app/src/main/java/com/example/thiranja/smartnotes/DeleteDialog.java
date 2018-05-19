package com.example.thiranja.smartnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.thiranja.smartnotes.HomeActivity.homeCustomArrayAdapter;
import static com.example.thiranja.smartnotes.HomeActivity.notelist;
import static com.example.thiranja.smartnotes.TrashActivity.trashCustomArrayAdapter;
import static com.example.thiranja.smartnotes.TrashActivity.trashlist;

public class DeleteDialog extends DialogFragment {

    boolean trashed;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }




    public DeleteDialog() {

        // Empty constructor required for DialogFragment

    }

    public static DeleteDialog newInstance(String title) {

        DeleteDialog frag = new DeleteDialog();

        Bundle args = new Bundle();

        args.putString("id", title);

        frag.setArguments(args);

        return frag;

    }


    @Override
    public synchronized void onStop() {
        super.onStop();
        notify();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getActivity().getClass().equals(TrashActivity.class)){
            trashed = true;
        }else{
            trashed = false;
        }
        final String id = getArguments().getString("id");
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        final DBHelper helper = new DBHelper(getActivity());

        builder.setTitle("Delete Note?");
        builder.setMessage("Do you want to Delete this note?");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean isDelete;
                if (trashed) {
                    isDelete = helper.deleteNote(id);
                    if(isDelete){
                        Toast.makeText(getActivity(), "Note Deleted", Toast.LENGTH_SHORT).show();
                        trashlist.clear();
                        trashlist.addAll(helper.getTrashNoteArray());
                        trashCustomArrayAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    }
                    //Intent trash = new Intent("android.intent.action.TRASH");
                    //trash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //startActivity(trash);

                }else{
                    isDelete = helper.sendToTrash(id);
                    if(isDelete){
                        Toast.makeText(getActivity(), "Note Deleted", Toast.LENGTH_SHORT).show();
                        notelist.clear();
                        notelist.addAll(helper.getNoteArray());
                        homeCustomArrayAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                    }
                    //Intent home = new Intent("android.intent.action.HOME");
                    //home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //startActivity(home);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();

    }
}
