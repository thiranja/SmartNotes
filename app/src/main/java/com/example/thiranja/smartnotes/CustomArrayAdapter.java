package com.example.thiranja.smartnotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Note> {

    private final Context context;
    private final List<Note> notes;


    public CustomArrayAdapter(@NonNull Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView;
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.simple_note_row, parent, false);
        }

        TextView nametv = (TextView) rowView.findViewById(R.id.rownametv);
        TextView idtv = (TextView) rowView.findViewById(R.id.rowidtv);
        TextView datetv = (TextView) rowView.findViewById(R.id.rowdatetv);

        Note curNote = notes.get(position);

        // Identifinng weather the note name is empty//
        if (curNote.getName().isEmpty()){
            nametv.setText("<<NO NAME>>");
        }else {
            nametv.setText(curNote.getName());
        }
        idtv.setText(curNote.getId());
        if(curNote.getDate().isEmpty() == false) {
            datetv.setText(curNote.getDate().substring(0, 6));
        }
        return rowView;
    }
}
