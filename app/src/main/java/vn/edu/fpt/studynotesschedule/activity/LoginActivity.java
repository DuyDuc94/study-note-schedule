package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.helper.*;


public class LoginActivity extends AppCompatActivity {
   EditText usernameInput, passwordInput;
   Button loginButton, backButton;
   DatabaseHelper myDB;
   String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.login);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        backButton = findViewById(R.id.backButton);

        myDB = new DatabaseHelper(LoginActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (myDB.isUserValid(username, password)) {
                    LoginActivity.this.userId = myDB.getUserId(usernameInput.getText().toString());
                    openMainPage();
                } else {
                    Toast.makeText(LoginActivity.this, "Username or Password incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStart();
            }
        });
    }

    public void openMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openStart() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}