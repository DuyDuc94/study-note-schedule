package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.helper.*;
import vn.edu.fpt.studynotesschedule.model.*;

public class RegisterActivity extends AppCompatActivity {
    EditText nameInput, surnameInput, universityInput, loginInput, passwordInput;
    Button registerButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.name);
        surnameInput = findViewById(R.id.surname);
        universityInput = findViewById(R.id.fieldOfStudies);
        loginInput = findViewById(R.id.login_register);
        passwordInput = findViewById(R.id.password_register);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButtonRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                DatabaseHelper myDB = new DatabaseHelper(RegisterActivity.this);
                myDB.addUser(new User(nameInput.getText().toString(),
                        surnameInput.getText().toString(),
                        universityInput.getText().toString(),
                        loginInput.getText().toString(),
                        passwordInput.getText().toString()));
                openStart();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStart();
            }
        });

    }

    public void openStart() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}