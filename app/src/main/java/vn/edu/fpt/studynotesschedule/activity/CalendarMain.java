package vn.edu.fpt.studynotesschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.adapter.*;
import vn.edu.fpt.studynotesschedule.helper.*;
import vn.edu.fpt.studynotesschedule.model.*;

public class CalendarMain extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView currentMonth, previousMonth, nextMonth;
    private RecyclerView recyclerView; // cửa sổ có tất cả các ngày
    private ListView lvNotesList;
    private Button goToCurrentMonth;
    private String userId;
    private List<String> currentUserData;
    private static User currentUser;
    public static DatabaseHelper myDB;
    public static List<Note> currentUserNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        recyclerView = findViewById(R.id.recyclerView);
        currentMonth = findViewById(R.id.currentMonth);
        previousMonth = findViewById(R.id.previousMonthButton);
        nextMonth = findViewById(R.id.nextMonthButton);
        goToCurrentMonth = findViewById(R.id.goToCurrentMonth);
        lvNotesList = findViewById(R.id.lvNotesList);
        Button deleteNote = findViewById(R.id.deleteNote);
        Button addNote = findViewById(R.id.addNoteButton);
        Button backButton = findViewById(R.id.calendarToMainPage);

        myDB = new DatabaseHelper(CalendarMain.this);
        currentUser = new User();
        currentUserData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("userId");
        currentUserData.addAll(myDB.getCurrentUserData(userId));
        currentUser.setUserData(currentUser, currentUserData);

        currentUserNoteList = myDB.getAllNotes(currentUser);

        CalendarHelper.selectedDate = LocalDate.now();
        Note.selectedNote = "";

        setMonthView();

        // selected note (to be deleted)
        lvNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Note.selectedNote = String.valueOf(adapterView.getItemAtPosition(position));
                setMonthView();
            }
        });

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNote();
            }
        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Note.selectedNote.equals("")) {
                    for(int i = 0; i < currentUserNoteList.size(); i++) {
                        if (Note.selectedNote.equals(currentUserNoteList.get(i).getText()) &&
                                CalendarHelper.dateFormatter(CalendarHelper.selectedDate).equals(CalendarHelper.dateFormatter(currentUserNoteList.get(i).getDate()))) {
                            currentUserNoteList = myDB.deleteNote(Note.selectedNote, CalendarHelper.dateFormatter(currentUserNoteList.get(i).getDate()), currentUserNoteList, currentUser);
                            setMonthView();
                            Note.selectedNote = "";
                        }
                    }
                }
            }
        });

        goToCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarHelper.selectedDate = LocalDate.now();
                setMonthView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainPage();
            }
        });
    }

    public void setMonthView() {
        currentMonth.setText(CalendarHelper.getMonths(CalendarHelper.selectedDate));
        previousMonth.setText(CalendarHelper.getMonths(CalendarHelper.selectedDate.minusMonths(1)));
        nextMonth.setText(CalendarHelper.getMonths(CalendarHelper.selectedDate.plusMonths(1)));

        ArrayList<LocalDate> daysOfMonth = CalendarHelper.fillCalendar(CalendarHelper.selectedDate);

        fillListView();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysOfMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(calendarAdapter);

        // go back to current date
        if (CalendarHelper.selectedDate.getMonth().equals(LocalDate.now().getMonth()) &&
                CalendarHelper.selectedDate.getYear() == LocalDate.now().getYear()) {
            // invisible when on the current month
            goToCurrentMonth.setVisibility(View.INVISIBLE);
        } else {
            goToCurrentMonth.setVisibility(View.VISIBLE);
        }
    }

    // ----------------------------------------
    public void previousMonth(View view) {
        CalendarHelper.selectedDate = CalendarHelper.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonth(View view) {
        CalendarHelper.selectedDate = CalendarHelper.selectedDate.plusMonths(1);
        setMonthView();
    }
    // ----------------------------------------

    // selected date - assignment
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            CalendarHelper.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillListView();
    }

    // change notes to String and fill listView
    private void fillListView() {
        List<Note> dailyNotes = new ArrayList<>();

        for (int i = 0; i < currentUserNoteList.size(); i++) {
            if (CalendarHelper.selectedDate.equals(currentUserNoteList.get(i).getDate())) {
                dailyNotes.add(currentUserNoteList.get(i));
            }
        }

        // change to String
        String [] notesToString = new String[dailyNotes.size()];

        int i = 0;
        for (Note note : dailyNotes) {
            notesToString[i] = note.getText();
            i += 1;
        }

        // setting the note adapter
        NoteAdapter adapter = new NoteAdapter(this, notesToString);
        lvNotesList.setAdapter(adapter);
    }

    public void openAddNote() {
        Intent intent = new Intent(this, AddNoteActivity.class);
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
}