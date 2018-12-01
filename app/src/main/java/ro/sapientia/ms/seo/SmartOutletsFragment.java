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
import java.util.List;

public class SmartOutletsFragment extends Fragment implements SmartOutletsListAdapter.ListItemClickListener {

    private List<SmartOutlet> smartOutlets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_outlets, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.smart_outlets_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        smartOutlets = new ArrayList<>();
        fillUpList();

        RecyclerView.Adapter mAdapter = new SmartOutletsListAdapter(smartOutlets, this);
        mRecyclerView.setAdapter(mAdapter);

        //ADD EMPTY VIEW!!!!!

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //Add a separate fragment here
    }

    private void fillUpList() {
        SmartOutlet outlet1 = new SmartOutlet("12345", "Kitchen outlet", true, null);
        SmartOutlet outlet2 = new SmartOutlet("12346", "Bedroom1 outlet", true, null);
        SmartOutlet outlet3 = new SmartOutlet("12347", "Bedroom2 outlet", false, null);

        smartOutlets.add(outlet1);
        smartOutlets.add(outlet2);
        smartOutlets.add(outlet3);
        smartOutlets.add(outlet1);
        smartOutlets.add(outlet2);
        smartOutlets.add(outlet3);
        smartOutlets.add(outlet1);
        smartOutlets.add(outlet2);
        smartOutlets.add(outlet3);
    }
}
