package com.example.everafter.subject_lists;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.everafter.DatabaseHelper;
import com.example.everafter.R;
import com.example.everafter.generic_item.AddItemActivity;

public class AddSubjectListActivity extends AddItemActivity {

    private EditText etListName, etDescription;
    private int userId;
    private int subjectListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_list);

        dbHelper = new DatabaseHelper(this);
        subjectListId= getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        loadFields();
        super.onCreate(savedInstanceState);

//        buttonAdd.setOnClickListener(view -> {
//            String listName = etListName.getText().toString().trim();
//            String description = etDescription.getText().toString().trim();
//
//            if (listName.isEmpty()) {
//                Toast.makeText(AddSubjectListActivity.this, "Please enter a list name", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put("list_name", listName);
//            values.put("description", description);
//            values.put("user_id", userId);
//
//            long newRowId = db.insert("subject_lists", null, values);
//            if (newRowId != -1) {
//                Toast.makeText(AddSubjectListActivity.this, "Subject list added successfully!", Toast.LENGTH_SHORT).show();
//                finish(); // Return to HomeActivity
//            } else {
//                Toast.makeText(AddSubjectListActivity.this, "Error adding subject list", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_list;
    }

    @Override
    protected int getButtonId() {
        return R.id.buttonAdd;
    }

    @Override
    protected void loadFields() {
        etListName = findViewById(R.id.etListName);
        etDescription = findViewById(R.id.etDescription);
    }

    @Override
    protected boolean validateFields() {
        String listName = etListName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        return !(listName.isEmpty() || description.isEmpty());
    }

    @Override
    protected ContentValues populateContentValues() {
        ContentValues values = new ContentValues();
        values.put("list_name", etListName.getText().toString().trim());
        values.put("description", etDescription.getText().toString().trim());
        values.put("user_id", userId);
        return values;
    }

    @Override
    protected String getTableName() {
        return "subject_lists";
    }


}
