package com.example.everafter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ListView listViewSubjectLists;
    private Button buttonAddList;
    private DatabaseHelper dbHelper;
    private int userId; // ID of the logged-in user

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve the user id passed from SignInActivity
        Intent intent = getIntent();
        this.userId = intent.getIntExtra("USER_ID", -1);

        // Launch the generic ItemsListActivity for subject lists
        Intent listIntent = new Intent(HomeActivity.this, SubjectsListActivity.class);
        listIntent.putExtra("USER_ID", userId);
        startActivity(listIntent);

//        // Optionally, finish HomeActivity if you want ItemsListActivity to replace it:
//        finish();
    }
}
