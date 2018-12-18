package ro.sapientia.ms.seo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.model.ScheduleData;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {
    private List<ScheduleData> mScheduleData;
    private ListItemClickListener mOnClickListener;

    ScheduleListAdapter(List<ScheduleData> scheduleData, ListItemClickListener onClickListener) {
        mScheduleData = scheduleData;
        mOnClickListener = onClickListener;
    }

    //TODO : Make a sign for the user is the operation is started.

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ScheduleViewHolder holder, int position) {
        holder.name.setText(mScheduleData.get(position).getName());

        //setting the date format
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

        holder.startDate.setText(dateFormat.format(mScheduleData.get(position).getOperationDate().getStartDate()));
        holder.endDate.setText(dateFormat.format(mScheduleData.get(position).getOperationDate().getFinishDate()));

        //TODO : Change operation time to status
        holder.operationTime.setText("30 minutes");
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_schedule_list_row, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mScheduleData.size();
    }

    interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView startDate;
        private TextView endDate;
        private TextView operationTime;

        ScheduleViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.schedule_list_name_value_text_view);
            startDate = v.findViewById(R.id.schedule_list_start_value_text_view);
            endDate = v.findViewById(R.id.schedule_list_end_value_text_view);
            operationTime = v.findViewById(R.id.schedule_list_operation_time_value_text_view);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
