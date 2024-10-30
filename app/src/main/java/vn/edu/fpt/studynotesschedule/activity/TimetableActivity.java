package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.adapter.TimetableAdapter;
import vn.edu.fpt.studynotesschedule.helper.*;
import vn.edu.fpt.studynotesschedule.model.*;

public class TimetableActivity extends AppCompatActivity {
    private User currentUser;
    private String userId;
    private List<String> currentUserData;
    private DatabaseHelper myDB;
    private TextView currentDay, previousDay, nextDay;
    ListView lvLessons;
    public static List <Lesson> lessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        currentDay = findViewById(R.id.currentDay);
        previousDay = findViewById(R.id.previousDayButton);
        nextDay = findViewById(R.id.nextDayButton);
        Button addLesson = findViewById(R.id.addLessonButton);
        Button deleteLesson = findViewById(R.id.deleteLessonButton);
        Button backButton = findViewById(R.id.timetableToMainPage);
        lvLessons = findViewById(R.id.lessonsListView);

        myDB = new DatabaseHelper(TimetableActivity.this);
        currentUser = new User();
        currentUserData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        currentUserData.addAll(myDB.getCurrentUserData(userId));
        currentUser.setUserData(currentUser, currentUserData);

        lessons = myDB.getAllLessons(currentUser);
        System.out.println(">>> lessons: " + lessons);

        CalendarHelper.selectedDate = LocalDate.now();
        Lesson.selectedLesson = "";

        setDayView();

        lvLessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Lesson.selectedLesson = String.valueOf(adapterView.getItemAtPosition(position));
                setDayView();
            }
        });

        addLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddLesson();
            }
        });

        deleteLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Lesson.selectedLesson.equals("")) {
                    for (int i = 0; i < lessons.size(); i++) {
                        if (Lesson.selectedLesson.equals(lessons.get(i).getText()) && CalendarHelper.dayFormatter(CalendarHelper.selectedDate).equals(lessons.get(i).getDayOfWeek())) {
                            lessons = myDB.deleteLesson(Lesson.selectedLesson, lessons.get(i).getDayOfWeek(), lessons, currentUser);
                            setDayView();
                            Lesson.selectedLesson = "";
                        }
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainPage();
            }
        });
    }

    private void setDayView () {
        currentDay.setText(CalendarHelper.getDays(CalendarHelper.selectedDate));
        previousDay.setText(CalendarHelper.getDays(CalendarHelper.selectedDate.minusDays(1)));
        nextDay.setText(CalendarHelper.getDays(CalendarHelper.selectedDate.plusDays(1)));
        fillLessonListView();
    }

    public void previousDay (View view) {
        CalendarHelper.selectedDate = CalendarHelper.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDay (View view) {
        CalendarHelper.selectedDate = CalendarHelper.selectedDate.plusDays(1);
        setDayView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillLessonListView();
    }

    private void fillLessonListView() {
        List <Lesson> dailyLessons = new ArrayList<>();

        for (int i = 0; i < lessons.size(); i++) {
            if (CalendarHelper.dayFormatter(CalendarHelper.selectedDate).equals(lessons.get(i).getDayOfWeek())) {
                dailyLessons.add(lessons.get(i));
            }
        }

        sort(dailyLessons);

        String [] lessonsToString = new String[dailyLessons.size()];

        int i = 0;
        for (Lesson lesson : dailyLessons) {
            lessonsToString[i] = lesson.getText();
            i += 1;
        }

        // adapter
        TimetableAdapter adapter = new TimetableAdapter(this, lessonsToString);
        lvLessons.setAdapter(adapter);
    }
    
    public void openAddLesson() {
        Intent intent = new Intent(this, AddLessonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void sort(List<Lesson> lessons) {
        for (int i = 0; i < lessons.size() - 1; i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (lessons.get(j).getStartTime().getHour() < lessons.get(i).getStartTime().getHour()) {
                    Collections.swap(lessons, lessons.indexOf(lessons.get(j)), lessons.indexOf(lessons.get(i)));
                } else if (lessons.get(j).getStartTime().getHour() == lessons.get(i).getStartTime().getHour()) {
                    if (lessons.get(j).getStartTime().getMinute() < lessons.get(i).getStartTime().getMinute()) {
                        Collections.swap(lessons, lessons.indexOf(lessons.get(j)), lessons.indexOf(lessons.get(i)));
                    }
                }
            }
        }
    }
}
