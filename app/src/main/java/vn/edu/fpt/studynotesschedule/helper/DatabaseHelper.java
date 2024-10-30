package vn.edu.fpt.studynotesschedule.helper;

import vn.edu.fpt.studynotesschedule.model.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private final static String DATABASE_NAME = "StudyNotesSchedule.db";

    private final static String TABLE_NAME_USER = "Users";
    private final static String TABLE_COLUMN_USER_ID = "Id";
    private final static String TABLE_COLUMN_USER_NAME = "Name";
    private final static String TABLE_COLUMN_USER_SURNAME = "Surname";
    private final static String TABLE_COLUMN_USER_UNIVERSITY = "University";
    private final static String TABLE_COLUMN_USER_USERNAME = "Username";
    private final static String TABLE_COLUMN_USER_PASSWORD = "Password";

    private final static String TABLE_NAME_NOTE = "Notes";
    private final static String TABLE_COLUMN_NOTE_ID = "Id";
    private final static String TABLE_COLUMN_NOTE_TEXT = "Text";
    private final static String TABLE_COLUMN_NOTE_DATE = "LocalDate";
    private final static String TABLE_COLUMN_NOTE_USER_ID = "UserId";

    private final static String TABLE_NAME_LESSON = "Lessons";
    private final static String TABLE_COLUMN_LESSON_ID = "Id";
    private final static String TABLE_COLUMN_LESSON_START = "StartTime";
    private final static String TABLE_COLUMN_LESSON_DAY = "Day";
    private final static String TABLE_COLUMN_LESSON_ROOM = "Room";
    private final static String TABLE_COLUMN_LESSON_TEXT = "Text";
    private final static String TABLE_COLUMN_LESSON_DURATION = "Duration";
    private final static String TABLE_COLUMN_LESSON_USER_ID = "UserId";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryUser = "CREATE TABLE " + TABLE_NAME_USER +
                " (" + TABLE_COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_COLUMN_USER_NAME + " TEXT, " +
                TABLE_COLUMN_USER_SURNAME + " TEXT, " +
                TABLE_COLUMN_USER_UNIVERSITY + " TEXT, " +
                TABLE_COLUMN_USER_USERNAME + " TEXT, " +
                TABLE_COLUMN_USER_PASSWORD + " TEXT);";
        db.execSQL(queryUser);

        String queryNote = "CREATE TABLE " + TABLE_NAME_NOTE +
                " (" + TABLE_COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_COLUMN_NOTE_TEXT + " TEXT, " +
                TABLE_COLUMN_NOTE_DATE + " TEXT, " +
                TABLE_COLUMN_NOTE_USER_ID + " TEXT, " +
                " FOREIGN KEY " + "(" + TABLE_COLUMN_NOTE_USER_ID + ")" + " REFERENCES " + TABLE_NAME_USER + "(" + TABLE_COLUMN_USER_ID + ")" + ");";
        db.execSQL(queryNote);

        String queryLesson = "CREATE TABLE " + TABLE_NAME_LESSON +
                " (" + TABLE_COLUMN_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_COLUMN_LESSON_START + " TEXT, " +
                TABLE_COLUMN_LESSON_DAY + " TEXT, " +
                TABLE_COLUMN_LESSON_ROOM + " TEXT, " +
                TABLE_COLUMN_LESSON_TEXT + " TEXT, " +
                TABLE_COLUMN_LESSON_DURATION + " INTEGER, " +
                TABLE_COLUMN_LESSON_USER_ID + " TEXT, " +
                " FOREIGN KEY " + "(" + TABLE_COLUMN_LESSON_USER_ID + ")" + " REFERENCES " + TABLE_NAME_USER + "(" + TABLE_COLUMN_USER_ID + ")" + ");";
        db.execSQL(queryLesson);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LESSON);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TABLE_COLUMN_USER_NAME, user.getName());
        cv.put(TABLE_COLUMN_USER_SURNAME, user.getSurname());
        cv.put(TABLE_COLUMN_USER_UNIVERSITY, user.getUniversity());
        cv.put(TABLE_COLUMN_USER_USERNAME, user.getUsername());
        cv.put(TABLE_COLUMN_USER_PASSWORD, user.getPassword());

        long result = db.insert(TABLE_NAME_USER, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Error when add User into DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "User added successful!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String date = CalendarHelper.dateFormatter(note.getDate());

        cv.put(TABLE_COLUMN_NOTE_TEXT, note.getText());
        cv.put(TABLE_COLUMN_NOTE_DATE, date);
        cv.put(TABLE_COLUMN_NOTE_USER_ID, note.getNoteOwner().getId());

        long result = db.insert(TABLE_NAME_NOTE, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Error when add Note into DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Note added successful!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addLesson(Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String startTime = lesson.getStartTime().toString();
        String dayOfWeek = lesson.getDayOfWeek();

        cv.put(TABLE_COLUMN_LESSON_START, startTime);
        cv.put(TABLE_COLUMN_LESSON_DAY, dayOfWeek);
        cv.put(TABLE_COLUMN_LESSON_ROOM, lesson.getRoom());
        cv.put(TABLE_COLUMN_LESSON_TEXT, lesson.getText());
        cv.put(TABLE_COLUMN_LESSON_DURATION, lesson.getDuration());
        cv.put(TABLE_COLUMN_LESSON_USER_ID, lesson.getLessonOwner().getId());

        long result = db.insert(TABLE_NAME_LESSON, null, cv);
        if(result == -1) {
            Toast.makeText(context, "Error when add Lesson into DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Lesson added successful!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUserValid(String login, String password) {
        String queryLogin = "SELECT " + TABLE_COLUMN_USER_USERNAME +  " FROM " + TABLE_NAME_USER;
        String queryPassword = "SELECT " + TABLE_COLUMN_USER_PASSWORD + " FROM " + TABLE_NAME_USER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorLogin = db.rawQuery(queryLogin, null);
        Cursor cursorPassword = db.rawQuery(queryPassword, null);

        cursorLogin.moveToFirst();
        cursorPassword.moveToFirst();
        while(!cursorLogin.isAfterLast()) {
            if (cursorLogin.getString(0).equals(login) && cursorPassword.getString(0).equals(password)) {
                return true;
            }
            cursorLogin.moveToNext();
            cursorPassword.moveToNext();
        }

        return false;
    }

    public List<String> getCurrentUserData(String userId) {
        String query = "SELECT * FROM " + TABLE_NAME_USER + " WHERE " + TABLE_COLUMN_USER_ID + " = " + "\"" + userId + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorUser = db.rawQuery(query, null);
        cursorUser.moveToFirst();

        List<String> currentUserData = new ArrayList<>();

        for(int i = 0; i < 6; i++) {
            currentUserData.add(cursorUser.getString(i));
        }
        return currentUserData;
    }

    public String getUserId(String login) {
        String query = "SELECT " + TABLE_COLUMN_USER_ID + " FROM " + TABLE_NAME_USER + " WHERE " + TABLE_COLUMN_USER_USERNAME + " = " + "\"" + login + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public List<Note> getAllNotes(User currentUser) {
        String userId = currentUser.getId();
        String queryNote = "SELECT * FROM " + TABLE_NAME_NOTE + " WHERE " + TABLE_COLUMN_NOTE_USER_ID + " = " + "\"" + userId + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorNote = db.rawQuery(queryNote, null);
        cursorNote.moveToFirst();

        List<Note> notes = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        while (!cursorNote.isAfterLast()) {
            String noteText = cursorNote.getString(1);
            LocalDate noteDate = LocalDate.parse(cursorNote.getString(2), formatter);
            Note note = new Note(noteText, noteDate, currentUser);
            notes.add(note);
            cursorNote.moveToNext();
        }
        return notes;
    }

    public List<Lesson> getAllLessons(User currentUser) {
        String userId = currentUser.getId();
        String queryLesson = "SELECT * FROM " + TABLE_NAME_LESSON + " WHERE " + TABLE_COLUMN_LESSON_USER_ID + " = " + "\"" + userId + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorLesson = db.rawQuery(queryLesson, null);
        cursorLesson.moveToFirst();

        List<Lesson> lessons = new ArrayList<>();

        while (!cursorLesson.isAfterLast()) {
            LocalTime startLesson = LocalTime.parse(cursorLesson.getString(1));
            String dayOfWeek = cursorLesson.getString(2);
            String room = cursorLesson.getString(3);
            String text = cursorLesson.getString(4);
            int duration = Integer.parseInt(cursorLesson.getString(5));

            Lesson lesson = new Lesson(startLesson, dayOfWeek, room, text, duration, currentUser);
            lessons.add(lesson);
            cursorLesson.moveToNext();
        }

        return lessons;
    }

    public List<Note> deleteNote(String text, String day, List<Note> notes, User currentUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_NOTE, "Text=? and LocalDate=?", new String[]{text, day});
        if(result == -1){
            Toast.makeText(context, "Can not find note to delete!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete note successful!", Toast.LENGTH_SHORT).show();

            notes.removeAll(notes);
            notes.addAll(getAllNotes(currentUser));
        }
        return notes;
    }

    public List<Lesson> deleteLesson(String text, String dayOfWeek, List<Lesson> lessons, User currentUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_LESSON, "Text=? and Day=?" , new String[]{text, dayOfWeek});
        if(result == -1){
            Toast.makeText(context, "Can not find lesson to delete!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Delete lesson successful!", Toast.LENGTH_SHORT).show();

            lessons.removeAll(lessons);
            lessons.addAll(getAllLessons(currentUser));
        }
        return lessons;
    }
}