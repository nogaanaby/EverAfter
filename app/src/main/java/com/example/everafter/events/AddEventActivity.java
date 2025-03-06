package com.example.everafter.events;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.EditText;

import com.example.everafter.generic_item.AddItemActivity;
import com.example.everafter.R;

public class AddEventActivity extends AddItemActivity {

    private EditText etEventName, etEventDate;
    private int subjectListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_event);

        // Retrieve subject list ID and user ID from the Intent extras
        subjectListId = getIntent().getIntExtra("SUBJECT_LIST_ID", -1);
        userId = getIntent().getIntExtra("USER_ID", -1);

        loadFields();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void loadFields() {
        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
    }


    @Override
    protected boolean validateFields() {
        // Check that both fields are non-empty.
        return !etEventName.getText().toString().trim().isEmpty() &&
                !etEventDate.getText().toString().trim().isEmpty();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_event;
    }

    @Override
    protected int getButtonId() {
        // The Add button is defined in the layout; ensure this ID matches.
        return R.id.buttonAddEvent;
    }

    @Override
    protected ContentValues populateContentValues() {
        ContentValues values = new ContentValues();
        values.put("event_name", etEventName.getText().toString().trim());
        values.put("event_date", etEventDate.getText().toString().trim());
        values.put("user_id", userId);
        values.put("subject_lists_id", subjectListId);
        return values;
    }

    @Override
    protected String getTableName() {
        return "events";
    }
}
