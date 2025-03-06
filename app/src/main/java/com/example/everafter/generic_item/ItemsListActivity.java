package com.example.everafter.generic_item;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.R;

import java.util.ArrayList;


/**
 * Abstract class ItemsListActivity provides generic functionality to display a list of items
 * from a specified database table. It expects the following Intent extras:
 *
 * - TABLE_NAME: The database table to query.
 * - DISPLAY_COLUMN: The column to display for each item.
 * - PARENT_ID: The parent's ID used for filtering.
 * - PARENT_ID_NAME: The column name that holds the parent ID.
 *
 * This class loads items (each with an 'id' and display text) into a ListView using a custom adapter
 * that shows three icons for each item:
 *    • Add sub-item
 *    • Edit item
 *    • Delete item (with confirmation)
 *
 * Subclasses must implement the abstract action methods.
 */
public abstract class ItemsListActivity extends AppCompatActivity {

    protected DatabaseHelper dbHelper;
    protected ArrayList<Item> items = new ArrayList<>();

    protected ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listViewItems = findViewById(getListViewId());
        loadItems();

        listViewItems.setOnItemClickListener((parent, view, position, id) -> {
            Item clickedItem = items.get(position);
            onListItemClick(clickedItem);
        });

        Button buttonAddSubjectList = findViewById(R.id.buttonAddSubjectList);
        if (buttonAddSubjectList != null) {
            buttonAddSubjectList.setOnClickListener(v -> onAddNewItem());
        }

        super.onCreate(savedInstanceState);

        // Enable the ActionBar "up" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    /**
     * Subclasses must return the ListView resource ID from their layout.
     */
    protected abstract int getListViewId();
    protected abstract void onListItemClick(Item item);
    /**
     * Loads the items from the database.
     * Assumes the table has a primary key column "id".
     */
    protected abstract void loadItems();
    protected abstract void onAddNewItem();
    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // Refresh items on resume
    }

    /**
     * Interface to handle actions on list items.
     */
    public interface ActionListener {
        void onAddSubItem(Item item);
        void onEditItem(Item item);
        void onDeleteItem(Item item);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ends EventsActivity and returns to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Abstract methods that subclasses must implement for handling actions:
    protected void onAddSubItem(Item item){}
    protected abstract void onEditItem(Item item);
    protected abstract void onDeleteItem(Item item);
}
