package ro.sapientia.ms.seo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.activity.MainActivity;
import ro.sapientia.ms.seo.model.SmartOutlet;

public class SmartOutletsFragment extends Fragment implements SmartOutletsListAdapter.ListItemClickListener {

    private List<SmartOutlet> smartOutlets;

    private RecyclerView mRecyclerView;
    private TextView emptyViewText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_outlets, container, false);

        mRecyclerView = view.findViewById(R.id.smart_outlets_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        MainActivity main = (MainActivity) getActivity();
        smartOutlets = main.getAllSmartOutletList();

        RecyclerView.Adapter mAdapter = new SmartOutletsListAdapter(smartOutlets, this);
        mRecyclerView.setAdapter(mAdapter);

        emptyViewText = view.findViewById(R.id.smart_outlets_no_outlets);

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Fragment nextFrag= new SmartOutletOverviewFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("indexOfSmartOutlet", clickedItemIndex);
        nextFrag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey("deleteSmartOutletUnderThisIndex")) {
                int index = bundle.getInt("deleteSmartOutletUnderThisIndex");
                smartOutlets.remove(index);
                MainActivity main = (MainActivity) getActivity();
                main.updateFirebaseData();
            }
        }

        emptyViewIfNoOperation();
    }

    private void emptyViewIfNoOperation() {
        if (smartOutlets.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            emptyViewText.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyViewText.setVisibility(View.GONE);
        }
    }
}
