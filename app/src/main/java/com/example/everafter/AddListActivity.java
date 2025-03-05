package com.example.everafter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class AddListActivity extends AppCompatActivity {

    private EditText etListName, etDescription;
    private Button buttonAdd;
    private DatabaseHelper dbHelper;
    private int userId;
    private int subjectListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        etListName = findViewById(R.id.etListName);
        etDescription = findViewById(R.id.etDescription);
        buttonAdd = findViewById(R.id.buttonAdd);
        dbHelper = new DatabaseHelper(this);
        subjectListId= getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Get the user id from the intent extras
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Enable the ActionBar "return" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        buttonAdd.setOnClickListener(view -> {
            String listName = etListName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (listName.isEmpty()) {
                Toast.makeText(AddListActivity.this, "Please enter a list name", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("list_name", listName);
            values.put("description", description);
            values.put("user_id", userId);

            long newRowId = db.insert("subject_lists", null, values);
            if (newRowId != -1) {
                Toast.makeText(AddListActivity.this, "Subject list added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to HomeActivity
            } else {
                Toast.makeText(AddListActivity.this, "Error adding subject list", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to exit without saving?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
