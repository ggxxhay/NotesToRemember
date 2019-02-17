package com.example.thanghq.notestoremember;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    public MainActivity mainActivity;
    public List<Note> noteList;

    public NoteAdapter(MainActivity mainActivity, List<Note> noteList){
        this.mainActivity = mainActivity;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MyHolder myHolder = null;
        // Inflate view.
        if(view==null){
            view = mainActivity.getLayoutInflater().inflate(R.layout.list_item, null);
            myHolder = new MyHolder();
            myHolder.textViewName = view.findViewById(R.id.textViewName);
            myHolder.textViewContent = view.findViewById(R.id.textViewContent);
            myHolder.imageButtonEdit = view.findViewById(R.id.imageButtonEdit);
            myHolder.imageButtonDelete = view.findViewById(R.id.imageButtonDelete);
            view.setTag(myHolder);
        }else{
            myHolder = (MyHolder) view.getTag();
        }
        // Show note.
        Note note = noteList.get(position);
        myHolder.textViewName.setText(note.getName());
        myHolder.textViewContent.setText(note.getContent());
        // Edit clicked.
        myHolder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.editBtnClick(position);
            }
        });
        // Delete clicked.
        myHolder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.deleteBtnClick(position);
            }
        });

        return view;
    }

    public class MyHolder{
        public TextView textViewName;
        public TextView textViewContent;
        public ImageButton imageButtonEdit;
        public ImageButton imageButtonDelete;
    }
}
