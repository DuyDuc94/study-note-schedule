package vn.edu.fpt.studynotesschedule.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.activity.CalendarMain;
import vn.edu.fpt.studynotesschedule.helper.CalendarHelper;
import vn.edu.fpt.studynotesschedule.view.CalendarViewHolder;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener listener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener listener) {
        this.days = days;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // single cell
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.13);

        return new CalendarViewHolder(view, listener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder calendarViewHolder, int position) {
        // mark selected date
        final LocalDate date = days.get(position);

        if(date == null)
            calendarViewHolder.dayOfMonth.setText("");
        else {
            calendarViewHolder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(CalendarHelper.selectedDate))
                calendarViewHolder.parentView.setBackgroundColor(Color.parseColor("#CCE5FF"));
            // current date
            else if(date.equals(LocalDate.now()))
                calendarViewHolder.parentView.setBackgroundColor(Color.parseColor("#A0A0A0"));
            // if there is a note on a given day
            else {
                for (int i = 0; i < 42; i++) {
                    for (int j = 0; j < CalendarMain.currentUserNoteList.size(); j++) {
                        LocalDate noteDate = CalendarMain.currentUserNoteList.get(j).getDate();
                        if (date.equals(noteDate)) {
                            calendarViewHolder.parentView.setBackgroundColor(Color.parseColor("#FFCC99"));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
