package vn.edu.fpt.studynotesschedule.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import vn.edu.fpt.studynotesschedule.R;
import vn.edu.fpt.studynotesschedule.adapter.CalendarAdapter;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener listener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener listener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = this.itemView.findViewById(R.id.calendarCell);
        this.listener = listener;
        this.days = days;
        this.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }
}
