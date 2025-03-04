package com.example.everafter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsernameOrEmail, etPassword;
    private Button buttonSignIn;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etUsernameOrEmail = findViewById(R.id.etUsernameOrEmail);
        etPassword = findViewById(R.id.etPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        dbHelper = new DatabaseHelper(this);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameOrEmail = etUsernameOrEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String query = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
                Cursor cursor = db.rawQuery(query, new String[]{usernameOrEmail, usernameOrEmail, password});
                int columnIndex = cursor.getColumnIndex("id");
                if (cursor.moveToFirst() && columnIndex != -1) {
                    int userId = cursor.getInt(columnIndex);
                    Toast.makeText(SignInActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                    cursor.close();
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
