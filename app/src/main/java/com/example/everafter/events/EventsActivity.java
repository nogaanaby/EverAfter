package com.example.everafter.events;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.R;
import com.example.everafter.generic_item.Item;
import com.example.everafter.generic_item.ItemsListActivity;
import com.example.everafter.generic_item.ItemsListAdapter;
import com.example.everafter.subject_lists.SubjectsListActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventsActivity extends ItemsListActivity {

    protected DatabaseHelper dbHelper;
    protected int userId;
    private int subjectListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_events);
        userId = getIntent().getIntExtra("USER_ID", -1);
        dbHelper = new DatabaseHelper(this);
        subjectListId = getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        super.onCreate(savedInstanceState);

        loadItems();

    }

    @Override
    protected int getListViewId() {
        return R.id.listViewEvents;
    }

    @Override
    protected void onListItemClick(Item item) {

    }

    @Override
    protected void loadItems() {
        items.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id, event_name, event_date FROM events WHERE subject_lists_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(subjectListId)});
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("event_name");
            int dateIndex = cursor.getColumnIndex("event_date");
            if (idIndex < 0 || nameIndex < 0 || dateIndex < 0) {
                Log.e("EventsActivity", "One or more columns not found in events query");
            } else {
                while (cursor.moveToNext()) {
                    int eventId = cursor.getInt(idIndex);
                    String eventName = cursor.getString(nameIndex);

                    //get the date from the database it storing as text and we need to cast it to date
                    String dateString = cursor.getString(dateIndex);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        Date eventDate = sdf.parse(dateString);
                        String formattedDate = sdf.format(eventDate);
                        eventName = eventName + " " + formattedDate + "";
                    } catch (ParseException e) {
                        e.printStackTrace();
                        eventName = eventName + " (unknown date)";
                    }


                    items.add(new Item(eventId, eventName));
                    Log.d("EventsActivity", "Loaded event: ID=" + eventId + ", Name=" + eventName);
                }

                if (items.isEmpty()) {
                    Toast.makeText(EventsActivity.this, "No events found.", Toast.LENGTH_SHORT).show();
                    listViewItems.setAdapter(null); // Or set an empty adapter if needed.
                    return;
                }

                ItemsListAdapter adapter = new ItemsListAdapter(this, items, new ItemsListActivity.ActionListener() {
                    @Override
                    public void onAddSubItem(Item item) {EventsActivity.this.onAddSubItem(item);}
                    @Override
                    public void onEditItem(Item item) {
                        EventsActivity.this.onEditItem(item);
                    }
                    @Override
                    public void onDeleteItem(Item item) {
                        EventsActivity.this.onDeleteItem(item);
                    }
                });
                listViewItems.setAdapter(adapter);
            }
            cursor.close();
        }

    }

    @Override
    protected void onAddNewItem() {
        Intent addEventIntent = new Intent(EventsActivity.this, AddEventActivity.class);
        // Pass the subject list ID so the new event is linked to this list
        addEventIntent.putExtra("SUBJECT_LIST_ID", subjectListId);
        startActivity(addEventIntent);
    }

    @Override
    protected void onEditItem(Item item) {

    }

    @Override
    protected void onDeleteItem(Item item) {
        int deleted = dbHelper.getWritableDatabase().delete("events", "id = ?", new String[]{String.valueOf(item.getId())});
        if (deleted > 0) {
            Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
            loadItems();
        } else {
            Toast.makeText(this, "Error deleting Event", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onAddSubItem(Item item) {
    }


    @Override
    protected int getButtonId() {
        return R.id.buttonAddEvent;
    }

}
