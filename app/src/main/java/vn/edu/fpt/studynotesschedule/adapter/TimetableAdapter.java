package vn.edu.fpt.studynotesschedule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.activity.TimetableActivity;
import vn.edu.fpt.studynotesschedule.helper.CalendarHelper;
import vn.edu.fpt.studynotesschedule.model.Lesson;

public class TimetableAdapter extends ArrayAdapter<String> {
    public TimetableAdapter(Context context, String[] lessons) {
        super(context, android.R.layout.simple_list_item_1, lessons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String lesson = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_cell, parent, false);

        TextView lessonCell = convertView.findViewById(R.id.lessonCell);
        TextView timeCell = convertView.findViewById(R.id.lessonCellTime);
        TextView roomCell = convertView.findViewById(R.id.lessonCellRoom);
        RelativeLayout layout = convertView.findViewById(R.id.relativeLayout);

        if (convertView != null) {
            if (lesson == Lesson.selectedLesson)
                layout.setBackgroundColor(Color.parseColor("#CCE5FF"));
        }

        String startTime = "";
        String endTime = "";
        String room = "";

        for (int i = 0; i < TimetableActivity.lessons.size(); i++) {
            if(CalendarHelper.dayFormatter(CalendarHelper.selectedDate).equals(TimetableActivity.lessons.get(i).getDayOfWeek())
                    && lesson.equals(TimetableActivity.lessons.get(i).getText())) {
                room = TimetableActivity.lessons.get(i).getRoom();
                startTime = CalendarHelper.timeFormatter(TimetableActivity.lessons.get(i).getStartTime());
                int hours = TimetableActivity.lessons.get(i).getDuration()/60;
                int minutes = TimetableActivity.lessons.get(i).getDuration() - 60*hours;
                endTime = CalendarHelper.timeFormatter(TimetableActivity.lessons.get(i).getStartTime().plusHours(hours).plusMinutes(minutes));
            }
        }
        String timeText = startTime + " - " + endTime;
        String roomText = "Room: " + room;

        lessonCell.setText(lesson);
        timeCell.setText(timeText);
        roomCell.setText(roomText);
        return convertView;
    }
}
