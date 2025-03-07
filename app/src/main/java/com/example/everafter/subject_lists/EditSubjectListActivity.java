package com.example.everafter.subject_lists;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.R;

public class EditSubjectListActivity extends AppCompatActivity {

    private EditText eltListName, eltDescription;
    private Button buttonSaveEditList;
    private DatabaseHelper dbHelper;
    private int subjectListId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject_list);

        // Initialize the DatabaseHelper.
        dbHelper = new DatabaseHelper(this);

        // Retrieve subject list ID and user ID passed via the Intent.
        subjectListId = getIntent().getIntExtra("ITEM_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize the views.
        eltListName = findViewById(R.id.eltListName);
        eltDescription = findViewById(R.id.eltDescription);
        buttonSaveEditList = findViewById(R.id.buttonSaveEditList);

        // Load the existing subject list details.
        loadSubjectList();

        // Set up the Save button to update the subject list.
        buttonSaveEditList.setOnClickListener(v -> saveSubjectList());

        // Enable the ActionBar "up" button.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadSubjectList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT list_name, description FROM subject_lists WHERE id = ? AND user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(subjectListId),
                String.valueOf(userId)
        });
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("list_name");
                int descIndex = cursor.getColumnIndex("description");
                if (nameIndex >= 0 && descIndex >= 0) {
                    String listName = cursor.getString(nameIndex);
                    String description = cursor.getString(descIndex);
                    eltListName.setText(listName);
                    eltDescription.setText(description);
                }
            }
            cursor.close();
        }
    }

    private void saveSubjectList() {
        String listName = eltListName.getText().toString().trim();
        String description = eltDescription.getText().toString().trim();

        if (listName.isEmpty()) {
            Toast.makeText(this, "Please enter a list name", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("list_name", listName);
        values.put("description", description);
        values.put("user_id", userId);

        int rowsAffected = db.update("subject_lists", values, "id = ? AND user_id = ?",
                new String[]{String.valueOf(subjectListId), String.valueOf(userId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Subject list updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to the previous screen.
        } else {
            Toast.makeText(this, "Error updating subject list", Toast.LENGTH_SHORT).show();
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
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
