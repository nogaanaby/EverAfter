package com.example.everafter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {
//
//    private LinearLayout fieldsContainer;
//    private Button buttonAddItem;
//    private DatabaseHelper dbHelper;
//    private String tableName;
//    private ArrayList<String> fieldNames;
//    private ArrayList<String> fieldTypes;
//    private ArrayList<EditText> editTextList = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_item);
//
//        fieldsContainer = findViewById(R.id.fieldsContainer);
//        buttonAddItem = findViewById(R.id.buttonAddItem);
//        dbHelper = new DatabaseHelper(this);
//
//        // Retrieve configuration from the Intent extras
//        tableName = getIntent().getStringExtra("TABLE_NAME");
//        fieldNames = getIntent().getStringArrayListExtra("FIELD_NAMES");
//        fieldTypes = getIntent().getStringArrayListExtra("FIELD_TYPES");
//
//        // Dynamically create an input field for each field name
//        if (fieldNames != null && fieldTypes != null && fieldNames.size() == fieldTypes.size()) {
//            for (int i = 0; i < fieldNames.size(); i++) {
//                String field = fieldNames.get(i);
//                String type = fieldTypes.get(i);
//                EditText et = new EditText(this);
//                et.setHint(field);
//
//                // Set input type based on field type
//                if ("int".equalsIgnoreCase(type)) {
//                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
//                } else if ("date".equalsIgnoreCase(type)) {
//                    // For a date, you might later attach a DatePicker dialog.
//                    et.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
//                } else {
//                    et.setInputType(InputType.TYPE_CLASS_TEXT);
//                }
//
//                // Set layout parameters for the EditText
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0, 8, 0, 8);
//                et.setLayoutParams(params);
//                fieldsContainer.addView(et);
//                editTextList.add(et);
//            }
//        }
//
//        buttonAddItem.setOnClickListener(v -> {
//            // Collect values from the dynamically created EditTexts
//            ContentValues values = new ContentValues();
//            if (fieldNames != null && fieldNames.size() == editTextList.size()) {
//                for (int i = 0; i < fieldNames.size(); i++) {
//                    String key = fieldNames.get(i);
//                    String value = editTextList.get(i).getText().toString().trim();
//                    if (value.isEmpty()) {
//                        Toast.makeText(AddItemActivity.this, "Please fill in " + key, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    // If the field is an integer, parse the value
//                    String type = fieldTypes.get(i);
//                    if ("int".equalsIgnoreCase(type)) {
//                        try {
//                            int intValue = Integer.parseInt(value);
//                            values.put(key, intValue);
//                        } catch (NumberFormatException e) {
//                            Toast.makeText(AddItemActivity.this, "Invalid integer for " + key, Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    } else {
//                        // For date and text, store the string value (later you might convert date string to a proper format)
//                        values.put(key, value);
//                    }
//                }
//            }
//
//            // Insert the new item into the table
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            long newRowId = db.insert(tableName, null, values);
//            if (newRowId != -1) {
//                Toast.makeText(AddItemActivity.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(AddItemActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
