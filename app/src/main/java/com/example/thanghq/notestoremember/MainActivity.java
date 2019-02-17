package com.example.thanghq.notestoremember;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private String DB_NAME = "NotesDatabase";
    final private int dbVersion = 1;

    final private int REQUEST_CODE_ADD = 100;
    final private int REQUEST_CODE_EDIT = 101;

    private ListView listView;
    private List<Note> noteList;
    private List<Note> sortedNoteList;
    private boolean isSorted = false;
    private NoteAdapter noteAdapter;
    private MyDBHelper myDBHelper = null;

    SharedPreferences sharedPreferences;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        showNotes();
        Toast.makeText(this, "On create", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save sorting status.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GlobalVariables.isSorted_key, isSorted);
        editor.commit(); // This leads to a bug before.
        Toast.makeText(this, "On Stop: "+isSorted, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Return the last sorting status.
        sharedPreferences = getSharedPreferences(GlobalVariables.sharedPreferencesName, MODE_PRIVATE);
        isSorted = sharedPreferences.getBoolean(GlobalVariables.isSorted_key, false);
        if(isSorted){
            Collections.sort(noteList);
        }
        Toast.makeText(this, "On resume: "+isSorted, Toast.LENGTH_LONG).show();
    }

    void showNotes() {
        if (myDBHelper == null) {
            myDBHelper = new MyDBHelper(this, DB_NAME, dbVersion);
        }
        SQLiteDatabase database = myDBHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Note", null);
        noteList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            Note note = new Note(id, name, content);
            noteList.add(note);
        }

        noteAdapter = new NoteAdapter(this, noteList);
        listView.setAdapter(noteAdapter);

    }

    // action add clicked, add new note.
    public void addBtnClick(View view) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    // button edit clicked.
    public void editBtnClick(int position) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra("note", noteList.get(position));
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    // button delete clicked.
    public void deleteBtnClick(final int position) {
        // Confirmation.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm_delete_title);
        builder.setMessage(R.string.confirm_delete_message);
        builder.setNegativeButton(R.string.confirm_delete_negative, null);
        builder.setPositiveButton(R.string.confirm_delete_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase database = myDBHelper.getWritableDatabase();
                database.delete("Note", "id=?", new String[]{String.valueOf(noteList.get(position).getId())});
                noteList.remove(position);
                noteAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    // Result after access add/edit activity.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == NoteDetailsActivity.RESULT_CODE) {
            Note note = (Note) data.getSerializableExtra("note");
            addOrEditNote(note);
        }
    }

    // add or edit row in database.
    void addOrEditNote(Note note) {
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("name", note.getName());
        data.put("content", note.getContent());
        if (note.getId() == 0) {
            database.insert("Note", null, data);
        } else {
            String whereClause = "id=?";
            String[] whereArgs = new String[]{String.valueOf(note.getId())};
            database.update("Note", data, whereClause, whereArgs);
        }
        showNotes();
    }

    // sort the list of notes.
    private void sortNoteList(MenuItem sortItem) {
        // if the list is sorted by name, reset it to the original order.
        if (isSorted) {
            Collections.sort(noteList, new NoteIDComparator());
            sortItem.getIcon().setAlpha(128);
            sortItem.setTitle(R.string.action_menu_sort);
        } else {
            Collections.sort(noteList);
            sortItem.getIcon().setAlpha(255);
            sortItem.setTitle(R.string.action_menu_sort_reverse);
        }
        isSorted = !isSorted;
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.actions_bar, menu);
        if (!isSorted) {
            menu.getItem(1).getIcon().setAlpha(128);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addBtnClick(null);
                return true;
            case R.id.action_sort:
                sortNoteList(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
