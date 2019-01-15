package ro.sapientia.ms.seo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.activity.MainActivity;
import ro.sapientia.ms.seo.model.SmartOutlet;
import ro.sapientia.ms.seo.model.WeekDay;

public class ScheduleFragment extends Fragment implements ScheduleListAdapter.ListItemClickListener{

    private List<SmartOutlet> scheduleData;

    private RecyclerView mRecyclerView;
    private TextView emptyViewText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        mRecyclerView = view.findViewById(R.id.schedule_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        MainActivity mainActivity = (MainActivity) getActivity();
        scheduleData = mainActivity.getAllSmartOutletList();

        RecyclerView.Adapter mAdapter = new ScheduleListAdapter(scheduleData, this);
        mRecyclerView.setAdapter(mAdapter);

        emptyViewText = view.findViewById(R.id.schedule_no_operations);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        emptyViewIfNoOperation();
    }

    private boolean areThereAnyOperationsToday() {
        for (int i=0; i<scheduleData.size(); i++) {
            if (scheduleData.get(i).getToday().isThisDaySet()) {
                return true;
            }
        }
        return false;
    }



    private void emptyViewIfNoOperation() {
        if (!areThereAnyOperationsToday()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyViewText.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyViewText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onListItemClick(int clickedPosition) {
        //TODO : Schedule fragment onClick.
    }
}
