package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.helper.*;
import vn.edu.fpt.studynotesschedule.model.*;

public class AddLessonActivity extends AppCompatActivity {
    private User currentUser;
    private String userId;
    private List<String> currentUserData;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);

        Button cancel = findViewById(R.id.cancelLesson);
        Button save = findViewById(R.id.saveLesson);
        TextView dayOfWeek = findViewById(R.id.dayOfWeek);
        EditText lessonName = findViewById(R.id.lessonName);
        EditText hours = findViewById(R.id.hours);
        EditText minutes = findViewById(R.id.minutes);
        EditText duration = findViewById(R.id.duration);
        EditText room = findViewById(R.id.room);
        myDB = new DatabaseHelper(AddLessonActivity.this);
        currentUser = new User();
        currentUserData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        currentUserData.addAll(myDB.getCurrentUserData(userId));
        currentUser.setUserData(currentUser, currentUserData);

        dayOfWeek.setText(CalendarHelper.getDays(CalendarHelper.selectedDate));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSaving();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newLessonName = lessonName.getText().toString();
                int hour = Integer.parseInt(hours.getText().toString());
                int mins = Integer.parseInt(minutes.getText().toString());
                String hoursAndMinutes;
                if(hour == 0 && mins == 0) {
                    hoursAndMinutes = "00:00";
                }
                else if (hour == 0 && mins >= 10) {
                    hoursAndMinutes = "00:" + mins;
                }
                else if (hour == 0 && (mins != 0 && mins < 10)) {
                    hoursAndMinutes = "00:0" + mins;
                }
                else if (hour < 10 && mins == 0) {
                    hoursAndMinutes = "0" + hour + ":00";
                }
                else if (hour < 10 && (mins != 0 && mins < 10)) {
                    hoursAndMinutes = "0" + hour + ":0" + mins;
                }
                else if (hour < 10 && mins >= 10) {
                   hoursAndMinutes = "0" + hour + ":" + mins;
                }
                else if (hour >= 10 && mins == 0) {
                    hoursAndMinutes = hour + ":00";
                }
                else if (hour >= 10 && (mins != 0 && mins < 10)) {
                    hoursAndMinutes = hour + ":0" + mins;
                } else {
                    hoursAndMinutes = hour + ":" + mins;
                }

                int newLessonDuration = Integer.parseInt(duration.getText().toString());
                String newLessonRoom = room.getText().toString();

                if (newLessonName.length() > 1 && hour >= 0 && hour <= 23 && mins >= 0 && mins <= 59
                && newLessonRoom.length() > 0 && newLessonDuration >= 20 && newLessonDuration <= 200) {

                    LocalTime startTime = LocalTime.parse(hoursAndMinutes, DateTimeFormatter.ISO_LOCAL_TIME);

                    List<Lesson> lessons = new ArrayList<>();
                    lessons = myDB.getAllLessons(currentUser);
                    List<Lesson> dailyLessons = new ArrayList<>();
                    for (int i = 0; i < lessons.size(); i++) {
                        if (CalendarHelper.dayFormatter(CalendarHelper.selectedDate).equals(lessons.get(i).getDayOfWeek())) {
                            dailyLessons.add(lessons.get(i));
                        }
                    }

                    boolean isValid = false;
                    for (int i = 0; i < dailyLessons.size(); i++) {
                        int hours = dailyLessons.get(i).getDuration()/60;
                        int minutes = dailyLessons.get(i).getDuration() - 60*hours;
                        LocalTime endTime = dailyLessons.get(i).getStartTime().plusHours(hours).plusMinutes(minutes);

                        if (startTime.getHour() > dailyLessons.get(i).getStartTime().getHour()) {
                            if (startTime.getHour() < endTime.getHour()) {
                                isValid = true;
                            } else if (startTime.getHour() == endTime.getHour() && startTime.getMinute() < endTime.getMinute()) {
                                isValid = true;
                            }
                        }

                        if (startTime.getHour() == dailyLessons.get(i).getStartTime().getHour()) {
                            if (startTime.getMinute() > dailyLessons.get(i).getStartTime().getMinute()) {
                                if (startTime.getHour() < endTime.getHour()) {
                                    isValid = true;
                                } else if (startTime.getHour() == endTime.getHour() && startTime.getMinute() < endTime.getMinute()) {
                                    isValid = true;
                                }
                            }
                        }

                        int hoursNewLesson = newLessonDuration/60;
                        int minutesNewLesson = newLessonDuration - 60*hoursNewLesson;
                        LocalTime endTimeNewLesson = startTime.plusHours(hoursNewLesson).plusMinutes(minutesNewLesson);

                        if (endTimeNewLesson.getHour() > dailyLessons.get(i).getStartTime().getHour()) {
                            if (endTimeNewLesson.getHour() < endTime.getHour()) {
                                isValid = true;
                            } else if (endTimeNewLesson.getHour() == endTime.getHour() && endTimeNewLesson.getMinute() < endTime.getMinute()) {
                                isValid = true;
                            }
                        }

                        if (endTimeNewLesson.getHour() == dailyLessons.get(i).getStartTime().getHour()) {
                            if (endTimeNewLesson.getMinute() > dailyLessons.get(i).getStartTime().getMinute()) {
                                if (endTimeNewLesson.getHour() < endTime.getHour()) {
                                    isValid = true;
                                } else if (endTimeNewLesson.getHour() == endTime.getHour() && endTimeNewLesson.getMinute() <= endTime.getMinute()) {
                                    isValid = true;
                                }
                            }
                        }

                        if (startTime.getHour() < dailyLessons.get(i).getStartTime().getHour() && endTimeNewLesson.getHour() > endTime.getHour()) {
                            isValid = true;
                        }

                        if (startTime.getHour() == dailyLessons.get(i).getStartTime().getHour() && endTimeNewLesson.getHour() > endTime.getHour()) {
                            if (startTime.getMinute() < dailyLessons.get(i).getStartTime().getMinute()) {
                                isValid = true;
                            }
                        }

                        if (startTime.getHour() < dailyLessons.get(i).getStartTime().getHour() && endTimeNewLesson.getHour() == endTime.getHour()) {
                            if (endTimeNewLesson.getMinute() > endTime.getMinute()) {
                                isValid = true;
                            }
                        }

                        if (startTime.getHour() == dailyLessons.get(i).getStartTime().getHour() && endTimeNewLesson.getHour() == endTime.getHour()) {
                            if (startTime.getMinute() < dailyLessons.get(i).getStartTime().getMinute() && endTimeNewLesson.getMinute() > endTime.getMinute()) {
                                isValid = true;
                            }
                        }

                    }

                    if (!isValid) {
                        Lesson newLesson = new Lesson(startTime, CalendarHelper.dayFormatter(CalendarHelper.selectedDate),
                                newLessonRoom, newLessonName, newLessonDuration, currentUser);
                        myDB.addLesson(newLesson);
                        saveLesson();
                    } else {
                        Toast.makeText(AddLessonActivity.this, "New lesson existed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddLessonActivity.this, "The entered data is incorrect or too short", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    public void cancelSaving() {
        Intent intent = new Intent(this, TimetableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void saveLesson() {
        Intent intent = new Intent(this, TimetableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}