package ro.sapientia.ms.seo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment implements ScheduleListAdapter.ListItemClickListener{

    private List<ScheduleData> scheduleData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        RecyclerView mRecylerView = view.findViewById(R.id.schedule_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecylerView.setLayoutManager(mLayoutManager);

        scheduleData = new ArrayList<>();
        fillScheduleData();

        RecyclerView.Adapter mAdapter = new ScheduleListAdapter(scheduleData, this);
        mRecylerView.setAdapter(mAdapter);

        //TODO : ADD EMPTY VIEW!

        return view;
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        //TODO : Schedule fragment onClick.
    }

    //function for test data generation
    private void fillScheduleData() {
        ScheduleData schedule1 = new ScheduleData("Kitchen 1", new OperatingDate(new Date(), new Date(), true));
        ScheduleData schedule2 = new ScheduleData("Kitchen 2", new OperatingDate(new Date(), new Date(), true));
        ScheduleData schedule3 = new ScheduleData("Bedroom 1", new OperatingDate(new Date(), new Date(), false));
        ScheduleData schedule4 = new ScheduleData("Kitchen 3", new OperatingDate(new Date(), new Date(), true));
        ScheduleData schedule5 = new ScheduleData("Living room 1", new OperatingDate(new Date(), new Date(), false));

        scheduleData.add(schedule1);
        scheduleData.add(schedule2);
        scheduleData.add(schedule3);
        scheduleData.add(schedule4);
        scheduleData.add(schedule5);
        scheduleData.add(schedule1);
        scheduleData.add(schedule2);
        scheduleData.add(schedule3);
        scheduleData.add(schedule4);
        scheduleData.add(schedule5);
    }
}
