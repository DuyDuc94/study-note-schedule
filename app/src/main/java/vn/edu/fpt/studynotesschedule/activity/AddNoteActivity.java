package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.helper.*;
import vn.edu.fpt.studynotesschedule.model.*;

public class AddNoteActivity extends AppCompatActivity {
    private User currentUser;
    private String userId;
    private List<String> currentUserData;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText noteName = findViewById(R.id.noteName);
        TextView noteDate = findViewById(R.id.noteDate);
        Button saveEdition = findViewById(R.id.saveEditionButton);
        Button cancelEdition = findViewById(R.id.cancelEditionButton);
        myDB = new DatabaseHelper(AddNoteActivity.this);
        currentUser = new User();
        currentUserData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        currentUserData.addAll(myDB.getCurrentUserData(userId));
        currentUser.setUserData(currentUser, currentUserData);

        noteDate.setText("Data: " + CalendarHelper.selectedDate.getDayOfMonth() + " " +
                CalendarHelper.getMonths(CalendarHelper.selectedDate));

        cancelEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityCancelEdition();
            }
        });

        saveEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNoteName = noteName.getText().toString();
                if (newNoteName.length() > 2) {
                    Note newNote = new Note(newNoteName, CalendarHelper.selectedDate, currentUser);
                    myDB.addNote(newNote);
                    activitySaveEdition();
                } else {
                    Toast.makeText(AddNoteActivity.this, "The note is too short!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void activityCancelEdition () {
        Intent intent = new Intent(this, CalendarMain.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void activitySaveEdition () {
        Intent intent = new Intent(this, CalendarMain.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}