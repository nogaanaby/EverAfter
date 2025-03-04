package com.example.everafter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    private ListView listViewEvents;
    private DatabaseHelper dbHelper;
    private int subjectListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Enable the ActionBar "up" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listViewEvents = findViewById(R.id.listViewEvents);
        dbHelper = new DatabaseHelper(this);

        // Retrieve the subject list ID passed from HomeActivity
        subjectListId = getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        Log.d("EventsActivity", "Subject List ID: " + subjectListId);

        loadEvents();
    }

    private void loadEvents() {
        ArrayList<String> eventDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT event_name, event_date FROM events WHERE subject_lists_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(subjectListId)});
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex("event_name");
            int dateIndex = cursor.getColumnIndex("event_date");
            if (nameIndex < 0 || dateIndex < 0) {
                Log.e("EventsActivity", "One or more columns not found in events query");
            } else {
                while (cursor.moveToNext()) {
                    String eventName = cursor.getString(nameIndex);
                    String eventDate = cursor.getString(dateIndex);
                    eventDetails.add(eventName + " (" + eventDate + ")");
                    Log.d("EventsActivity", "Loaded event: " + eventName + " on " + eventDate);
                }
            }
            cursor.close();
        }
        // Check if no events were found and add a placeholder message
        if (eventDetails.isEmpty()) {
            Log.d("EventsActivity", "No events found for subject_list_id: " + subjectListId);
            eventDetails.add("No events found");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventDetails);
        listViewEvents.setAdapter(adapter);
    }

    // Handle the up (back) arrow in the ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ends EventsActivity and returns to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
