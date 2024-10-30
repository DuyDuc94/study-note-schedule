package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.helper.DatabaseHelper;
import vn.edu.fpt.studynotesschedule.model.Lesson;
import vn.edu.fpt.studynotesschedule.model.User;

public class MainActivity extends AppCompatActivity {
    String userId;
    Button calendarButton, timetableButton;
    private List<String> currentUserData;
    private User currentUser;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarButton = findViewById(R.id.calendarButton);
        timetableButton = findViewById(R.id.timetableButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        TextView nextLesson = findViewById(R.id.nextLesson);
        TextView currentUser = findViewById(R.id.currentUserMainPage);

        myDB = new DatabaseHelper(MainActivity.this);
        this.currentUser = new User();
        currentUserData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        currentUserData.addAll(myDB.getCurrentUserData(userId));
        this.currentUser.setUserData(this.currentUser, currentUserData);

        String nextLessonString = nextLesson();
        nextLesson.setText(nextLessonString);
        currentUser.setText(this.currentUser.getName() + " " + this.currentUser.getSurname());

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });

        timetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimetable();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStart();
            }
        });
    }

    public void openTimetable () {
        Intent intent = new Intent(this, TimetableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openCalendar() {
        Intent intent = new Intent(this, CalendarMain.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openStart() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public String nextLesson() {
        String nextLesson = "";
        LocalTime now = LocalTime.now();
        int currentDay = LocalDate.now().getDayOfWeek().getValue();
        int timeDifference = 1000000;

        List<Lesson> lessons = myDB.getAllLessons(currentUser);

        String lessonDay = "";
        for (int i = 0; i < lessons.size(); i++) {
            String lessonDayString = lessons.get(i).getDayOfWeek();
            int lessonDayInt = 0;
            switch (lessonDayString) {
                case "Mon":
                    lessonDayInt = 1;
                    break;
                case "Tue":
                    lessonDayInt = 2;
                    break;
                case "Wed":
                    lessonDayInt = 3;
                    break;
                case "Thu":
                    lessonDayInt = 4;
                    break;
                case "Fri":
                    lessonDayInt = 5;
                    break;
                case "Sat":
                    lessonDayInt = 6;
                    break;
                case "Sun":
                    lessonDayInt = 7;
                    break;
                default:
                    break;
            }

            if (lessonDayInt > currentDay) {
                lessonDayInt = lessonDayInt - currentDay;
                lessonDay = lessons.get(i).getDayOfWeek();
            } else if (currentDay == lessonDayInt){
                if (now.getHour() > lessons.get(i).getStartTime().getHour()) {
                    lessonDayInt = 7;
                    lessonDay = lessons.get(i).getDayOfWeek();
                } else if (now.getHour() < lessons.get(i).getStartTime().getHour()) {
                    lessonDayInt = 0;
                    lessonDay = "today";
                }
            } else if (currentDay == lessonDayInt && now.getHour() == lessons.get(i).getStartTime().getHour()) {
                if (lessons.get(i).getStartTime().getMinute() > now.getMinute()) {
                    lessonDayInt = 7;
                    lessonDay = "today";
                } else {
                    lessonDayInt = 0;
                    lessonDay = lessons.get(i).getDayOfWeek();
                }
            }
            else {
                lessonDayInt = 7 - Math.abs(lessonDayInt - currentDay);
                lessonDay = lessons.get(i).getDayOfWeek();
            }

            int currentTimeDifference = ((lessons.get(i).getStartTime().getHour() - now.getHour()) * 60) + (lessons.get(i).getStartTime().getMinute() - now.getMinute()) + (lessonDayInt * 24 * 60);

            if (currentTimeDifference < timeDifference && currentTimeDifference > 0) {
                timeDifference = currentTimeDifference;
                nextLesson = lessons.get(i).getText() + " (" + lessonDay + " " + lessons.get(i).getStartTime() + ") " +
                        lessons.get(i).getRoom();
            } else {
                currentTimeDifference = Math.abs(currentTimeDifference);
                if (currentTimeDifference < timeDifference) {
                    timeDifference = currentTimeDifference;
                    nextLesson = lessons.get(i).getText() + " (" + lessonDay + " " + lessons.get(i).getStartTime() + ") " +
                            lessons.get(i).getRoom();
                }
            }
        }

        String next = "";

        if (!nextLesson.equals(""))
            next = "Next lesson: " + nextLesson;

        return next;
    }
}