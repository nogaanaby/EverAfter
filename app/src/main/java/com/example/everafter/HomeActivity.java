package com.example.everafter;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listViewSubjectLists;
    private Button buttonAddList;
    private DatabaseHelper dbHelper;
    private int userId; // ID of the logged-in user

    // We'll store subject list IDs in parallel to the names
    private ArrayList<Integer> subjectListIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewSubjectLists = findViewById(R.id.listViewSubjectLists);
        buttonAddList = findViewById(R.id.buttonAddList);
        dbHelper = new DatabaseHelper(this);

        // Retrieve the user id passed from SignInActivity
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        Log.d("HomeActivity", "User ID received: " + userId);

        loadSubjectLists();

        // When a subject list is clicked, open EventsActivity for that list.
        listViewSubjectLists.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            int subjectListId = subjectListIds.get(position);
            Intent eventsIntent = new Intent(HomeActivity.this, EventsActivity.class);
            eventsIntent.putExtra("SUBJECT_LIST_ID", subjectListId);
            startActivity(eventsIntent);
        });

        // Open AddListActivity when "Add New Subject List" is clicked
        buttonAddList.setOnClickListener(v -> {
            Intent addListIntent = new Intent(HomeActivity.this, AddListActivity.class);
            addListIntent.putExtra("USER_ID", userId);
            startActivity(addListIntent);
        });
    }

    private void loadSubjectLists() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> listNames = new ArrayList<>();
        subjectListIds.clear();

        // Retrieve both id and list_name for subject lists of the user.
        String query = "SELECT id, list_name FROM subject_lists WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("list_name");
            if (idIndex < 0 || nameIndex < 0) {
                Log.e("HomeActivity", "One or more columns not found in subject_lists query");
            } else {
                while (cursor.moveToNext()) {
                    int listId = cursor.getInt(idIndex);
                    String listName = cursor.getString(nameIndex);
                    listNames.add(listName);
                    subjectListIds.add(listId);
                    Log.d("HomeActivity", "Loaded subject list: ID=" + listId + ", Name=" + listName);
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
        loadSubjectLists(); // refresh subject lists when returning to HomeActivity
    }
}
