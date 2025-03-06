package com.example.everafter.subject_lists;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.events.EventsActivity;
import com.example.everafter.generic_item.Item;
import com.example.everafter.generic_item.ItemsListActivity;
import com.example.everafter.generic_item.ItemsListAdapter;
import com.example.everafter.R;
import com.example.everafter.events.AddEventActivity;

import java.util.ArrayList;

public class SubjectsListActivity extends ItemsListActivity {

    protected DatabaseHelper dbHelper;
    protected int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        userId = getIntent().getIntExtra("USER_ID", -1);
        userId=1;
        dbHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_subjects_list);
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onListItemClick(Item item) {
        Intent eventsIntent = new Intent(this, EventsActivity.class);
        eventsIntent.putExtra("SUBJECT_LIST_ID", item.getId());
        eventsIntent.putExtra("USER_ID", userId);

        startActivity(eventsIntent);
    }
    @Override
    protected int getListViewId() {
        // The ListView in the layout (activity_subject_list.xml) should have this ID.
        return R.id.listViewSubjectLists;
    }

    @Override
    protected void onAddNewItem(){
        Intent intent = new Intent(this, AddSubjectListActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Called when the "Add Sub-Item" icon is clicked on a subject list item.
     * For subject lists, we interpret this as adding an event to the selected subject list.
     */
    @Override
    protected void onAddSubItem(Item item) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the "Edit" icon is clicked on a subject list item.
     * Launches an activity to edit the subject list.
     */
    @Override
    protected void onEditItem(Item item) {
//        Intent intent = new Intent(this, EditSubjectListActivity.class);
//        intent.putExtra("ITEM_ID", item.getId());
//        startActivity(intent);
    }

    /**
     * Called when the "Delete" icon is clicked on a subject list item.
     * Deletes the subject list from the database and reloads the list.
     */
    @Override
    protected void onDeleteItem(Item item) {
        int deleted = dbHelper.getWritableDatabase().delete("subject_lists", "id = ?", new String[]{String.valueOf(item.getId())});
        if (deleted > 0) {
            Toast.makeText(this, "Subject list deleted", Toast.LENGTH_SHORT).show();
            loadItems();
        } else {
            Toast.makeText(this, "Error deleting subject list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getButtonId() {
        return R.id.buttonAddSubjectList;
    }

    @Override
    protected void loadItems() {
        items.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT id, list_name FROM subject_lists WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("list_name");
            if (idIndex < 0 || nameIndex < 0) {
                Log.e("SubjectsListActivity", "One or more columns not found in subject_lists query");
            } else {
                while (cursor.moveToNext()) {
                    int listId = cursor.getInt(idIndex);
                    String listName = cursor.getString(nameIndex);
                    items.add(new Item(listId, listName));
                    Log.d("SubjectsListActivity", "Loaded subject list: ID=" + listId + ", Name=" + listName);
                }
            }
            cursor.close();
        }
        // Create a simple adapter to display the list names.
        ArrayList<String> listNames = new ArrayList<>();
        for (Item item : items) {
            listNames.add(item.getDisplayText());
        }
        ItemsListAdapter adapter = new ItemsListAdapter(this, items, new ItemsListActivity.ActionListener() {
            @Override
            public void onAddSubItem(Item item) {
                SubjectsListActivity.this.onAddSubItem(item);
            }
            @Override
            public void onEditItem(Item item) {
                SubjectsListActivity.this.onEditItem(item);
            }
            @Override
            public void onDeleteItem(Item item) {
                SubjectsListActivity.this.onDeleteItem(item);
            }
        });
        listViewItems.setAdapter(adapter);
    }

}
