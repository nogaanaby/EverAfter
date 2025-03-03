package com.example.everafter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDate;
    private Spinner spinnerSubjectLists;
    private Button buttonAddEvent;
    private DatabaseHelper dbHelper;
    private int userId;  // Logged-in user's id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        spinnerSubjectLists = findViewById(R.id.spinnerSubjectLists);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        dbHelper = new DatabaseHelper(this);

        // Retrieve user id passed from HomeActivity
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Load subject lists for the current user and populate the spinner
        ArrayList<SubjectList> subjectLists = loadSubjectListsForUser();
        ArrayAdapter<SubjectList> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectLists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjectLists.setAdapter(adapter);

        buttonAddEvent.setOnClickListener(view -> {
            String eventName = etEventName.getText().toString().trim();
            String eventDate = etEventDate.getText().toString().trim();

            if (eventName.isEmpty() || eventDate.isEmpty()) {
                Toast.makeText(AddEventActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected subject list from the spinner
            SubjectList selectedSubjectList = (SubjectList) spinnerSubjectLists.getSelectedItem();
            int subjectListId = selectedSubjectList != null ? selectedSubjectList.getId() : -1;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("event_name", eventName);
            values.put("event_date", eventDate);
            values.put("user_id", userId);
            values.put("subject_lists_id", subjectListId);

            long newRowId = db.insert("events", null, values);
            if (newRowId != -1) {
                Toast.makeText(AddEventActivity.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to HomeActivity
            } else {
                Toast.makeText(AddEventActivity.this, "Error adding event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Loads the subject lists for the logged-in user from the database.
     */
    private ArrayList<SubjectList> loadSubjectListsForUser() {
        ArrayList<SubjectList> subjectLists = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id, list_name FROM subject_lists WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex("id");
            int columnIndexListName = cursor.getColumnIndex("list_name");
            if (columnIndex == -1 || columnIndexListName == -1) {
                android.util.Log.e("AddEventActivity", "Column 'list_name' not found in the query result.");
            } else {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(columnIndex);
                    String listName = cursor.getString(columnIndexListName);
                    subjectLists.add(new SubjectList(id, listName));
                }
            }
            cursor.close();
        }
        return subjectLists;
    }

    /**
     * Model class representing a subject list.
     */
    public static class SubjectList {
        private int id;
        private String name;

        public SubjectList(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        @Override
        public String toString() {
            return name;
        }
    }
}
