package com.example.everafter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AddEventActivity extends AppCompatActivity {

    private EditText etEventName, etEventDate;
    private Button buttonAddEvent;
    private DatabaseHelper dbHelper;
    private int userId;         // Logged-in user's id
    private int subjectListId;  // The subject list id passed automatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Retrieve subject list ID and user ID from the Intent extras
        subjectListId = getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        dbHelper = new DatabaseHelper(this);

        // Enable the ActionBar "return" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        buttonAddEvent.setOnClickListener(view -> {
            String eventName = etEventName.getText().toString().trim();
            String eventDate = etEventDate.getText().toString().trim();

            if (eventName.isEmpty() || eventDate.isEmpty()) {
                Toast.makeText(AddEventActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("event_name", eventName);
            values.put("event_date", eventDate);
            values.put("user_id", userId);
            // Use the subjectListId passed from the previous activity
            values.put("subject_lists_id", subjectListId);

            long newRowId = db.insert("events", null, values);
            if (newRowId != -1) {
                Toast.makeText(AddEventActivity.this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to previous activity
            } else {
                Toast.makeText(AddEventActivity.this, "Error adding event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ends EventsActivity and returns to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
