package com.example.everafter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listViewSubjectLists;
    private Button buttonAddList, buttonAddEvent;
    private DatabaseHelper dbHelper;
    private int userId; // ID of the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewSubjectLists = findViewById(R.id.listViewSubjectLists);
        buttonAddList = findViewById(R.id.buttonAddList);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        dbHelper = new DatabaseHelper(this);

        // Retrieve the user id passed from SignInActivity
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        loadSubjectLists();

        // Open AddListActivity when Add New Subject List is clicked
        buttonAddList.setOnClickListener(view -> {
            Intent addListIntent = new Intent(HomeActivity.this, AddListActivity.class);
            addListIntent.putExtra("USER_ID", userId);
            startActivity(addListIntent);
        });

        // Open AddEventActivity when Add New Event is clicked
        buttonAddEvent.setOnClickListener(view -> {
            Intent addEventIntent = new Intent(HomeActivity.this, AddEventActivity.class);
            addEventIntent.putExtra("USER_ID", userId);
            startActivity(addEventIntent);
        });
    }

    private void loadSubjectLists() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> listNames = new ArrayList<>();

        String query = "SELECT list_name FROM subject_lists WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex("list_name");
            if (columnIndex == -1) {
                android.util.Log.e("HomeActivity", "Column 'list_name' not found in the query result.");
            } else {
                while (cursor.moveToNext()) {
                    String listName = cursor.getString(columnIndex);
                    listNames.add(listName);
                }
            }
            cursor.close();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);
        listViewSubjectLists.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubjectLists(); // refresh data when returning to HomeActivity
    }
}
