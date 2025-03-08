package com.example.everafter.events;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    private EditText editEventName, editEventDate;
    private Button buttonSaveEditEvent;
    private DatabaseHelper dbHelper;
    private int eventId;
    private int userId;
    private int subject_list_id;

    // Flag to track if any changes have been made
    private boolean hasChanged = false;

    // Store original values to compare
    private String originalEventName;
    private String originalEventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        dbHelper = new DatabaseHelper(this);

        // Retrieve event and user IDs from the Intent extras
        eventId = getIntent().getIntExtra("ITEM_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);
        subject_list_id = getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        // Initialize the views (make sure these IDs match your layout).
        editEventName = findViewById(R.id.editEventName);
        editEventDate = findViewById(R.id.editEventDate);
        buttonSaveEditEvent = findViewById(R.id.buttonSaveEditEvent);

        // Load the event details from the database
        loadEvent();

        // Attach TextWatchers to detect changes
        attachTextWatchers();

        // Set up the Save button
        buttonSaveEditEvent.setOnClickListener(v -> saveEvent());

        // Enable the ActionBar "up" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void attachTextWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Compare current values with original values to set hasChanged flag
                String currentName = editEventName.getText().toString().trim();
                String currentDate = editEventDate.getText().toString().trim();
                hasChanged = (!currentName.equals(originalEventName) || !currentDate.equals(originalEventDate));
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        };

        editEventName.addTextChangedListener(watcher);
        editEventDate.addTextChangedListener(watcher);
    }

    private void loadEvent() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT event_name, event_date FROM events WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(eventId)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("event_name");
                int dateIndex = cursor.getColumnIndex("event_date");
                if (nameIndex >= 0 && dateIndex >= 0) {
                    originalEventName = cursor.getString(nameIndex);
                    originalEventDate = cursor.getString(dateIndex);
                    editEventName.setText(originalEventName);
                    editEventDate.setText(originalEventDate);
                }
            }
            cursor.close();
        }
        // Initialize flag as false since we just loaded the original data.
        hasChanged = false;
    }

    private void saveEvent() {
        String eventName = editEventName.getText().toString().trim();
        String eventDateStr = editEventDate.getText().toString().trim();

        if (eventName.isEmpty() || eventDateStr.isEmpty()) {
            Toast.makeText(this, "Please enter both event name and date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and format the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date eventDate;
        try {
            eventDate = sdf.parse(eventDateStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }
        String formattedDate = sdf.format(eventDate);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event_name", eventName);
        values.put("event_date", formattedDate);
        values.put("user_id", userId);
        values.put("subject_lists_id", subject_list_id);

        int rowsAffected = db.update("events", values, "id = ?",
                new String[]{String.valueOf(eventId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating event", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // If no changes were made, exit immediately
            if (!hasChanged) {
                finish();
            } else {
                // Otherwise, ask for confirmation
                new AlertDialog.Builder(this)
                        .setTitle("Confirm Exit")
                        .setMessage("You have unsaved changes. Are you sure you want to exit without saving?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
