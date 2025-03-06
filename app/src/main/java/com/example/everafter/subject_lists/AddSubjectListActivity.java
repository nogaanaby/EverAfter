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
