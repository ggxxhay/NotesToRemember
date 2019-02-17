package com.example.thanghq.notestoremember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetailsActivity extends AppCompatActivity {

    public static int RESULT_CODE = 200;

    private TextView textView;
    private EditText editTextName;
    private EditText editTextContent;
    private Button buttonAdd;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        textView = findViewById(R.id.textView);
        editTextName = findViewById(R.id.editTextName);
        editTextContent = findViewById(R.id.editTextContent);
        buttonAdd = findViewById(R.id.buttonAddOrEdit);

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            textView.setText(R.string.txtInfoDetails_edit);
            buttonAdd.setText(R.string.btnEditDetails);
            editTextName.setText(note.getName());
            editTextContent.setText(note.getContent());
        }
    }

    public void addOrEditClick(View view) {
        String name = editTextName.getText().toString();
        String content = editTextContent.getText().toString();
        // Check empty.
        if (name.equals("") || content.equals("")) {
            Toast.makeText(this, R.string.empty_input_alert, Toast.LENGTH_LONG).show();
            return;
        }
        // Validate ok.
        if (note == null) {
            note = new Note(name, content);
        } else {
            note.setName(name);
            note.setContent(content);
        }
        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_CODE, intent);
        finish();
    }
}
