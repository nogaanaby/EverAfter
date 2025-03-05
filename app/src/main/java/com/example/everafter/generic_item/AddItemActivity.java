package com.example.everafter.generic_item;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.everafter.DatabaseHelper;

public abstract class AddItemActivity extends AppCompatActivity {

    protected Button buttonAddItem;
    protected DatabaseHelper dbHelper;
    protected int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId()); // Allow each subclass to define its layout.
        buttonAddItem = findViewById(getButtonId());
        dbHelper = new DatabaseHelper(this);

        // Enable the ActionBar "return" button.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // When the Add button is clicked, call our final template method.
        if (buttonAddItem != null) {
            buttonAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddItem(); // Template method: common workflow.
                }
            });
        }

        loadFields(); // Let subclasses wire up their fields.
    }

    // Subclasses must supply their layout, button, and field wiring.
    protected abstract int getLayoutId();
    protected abstract int getButtonId();
    protected abstract void loadFields();

    // These abstract helper methods allow subclasses to plug in their specifics.
    protected abstract boolean validateFields();
    protected abstract ContentValues populateContentValues();
    protected abstract String getTableName();

    /**
     * Template method that handles the common workflow of adding an item.
     * This method should not be overridden.
     */
    public final void onAddItem() {
        // Validate fields using subclass logic.
        if (!validateFields()) {
            Toast.makeText(AddItemActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the ContentValues from the subclass.
        ContentValues values = populateContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(getTableName(), null, values);

        // Provide common feedback.
        if (newRowId != -1) {
            Toast.makeText(AddItemActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Return to previous activity.
        } else {
            Toast.makeText(AddItemActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to exit without saving?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
