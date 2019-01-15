package ro.sapientia.ms.seo.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.model.SmartOutlet;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {
    private List<SmartOutlet> mScheduleData;
    private ListItemClickListener mOnClickListener;
    private List<SmartOutlet> adapterData;

    ScheduleListAdapter(List<SmartOutlet> scheduleData, ListItemClickListener onClickListener) {
        mScheduleData = scheduleData;
        mOnClickListener = onClickListener;
        adapterData = new ArrayList<>();
        processDataForTodaySchedule();
    }

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ScheduleViewHolder holder, int position) {
        holder.name.setText(adapterData.get(position).getName());
        holder.startDate.setText(adapterData.get(position).getToday().getStartingHour() + ":00");
        String stringHolder = "";
        stringHolder += adapterData.get(position).getToday().getOperationDuration();
        holder.operationTime.setText(stringHolder + " minutes");
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_schedule_list_row, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    interface ListItemClickListener {
        void onListItemClick(int clickedPosition);
    }

    private void processDataForTodaySchedule() {
        for (int i=0; i<mScheduleData.size(); i++) {
            if (mScheduleData.get(i).getToday().isThisDaySet()) {
                adapterData.add(mScheduleData.get(i));
            }
        }
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView startDate;
        private TextView operationTime;

        ScheduleViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.schedule_list_name_value_text_view);
            startDate = v.findViewById(R.id.schedule_list_start_value_text_view);
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
