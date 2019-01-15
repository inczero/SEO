package ro.sapientia.ms.seo.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.activity.MainActivity;
import ro.sapientia.ms.seo.model.SmartOutlet;

public class SmartOutletOverviewFragment extends Fragment {

    private SmartOutlet smartOutlet;

    //view elements
    private TextView mondayOperation;
    private TextView tuesdayOperation;
    private TextView wednesdayOperation;
    private TextView thursdayOperation;
    private TextView fridayOperation;
    private TextView saturdayOperation;
    private TextView sundayOperation;
    private Button addOperationButton;
    private Button deleteOperationButton;
    private Button deleteDeviceButton;
    private GraphView consumptionGraph;

    //constant value for schedule end hour calculation
    static final long ONE_MINUTE_IN_MILLIS=60000; //milliseconds

    //index of tapped smart outlet
    private int indexOfSmartOutlet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_outlet_overview, container, false);

        //initializing resources
        CardView smartOutletPicture = view.findViewById(R.id.smart_outlet_overview_card_view);
        TextView idTextView = view.findViewById(R.id.smart_outlet_overview_id_text);
        TextView nameTextView = view.findViewById(R.id.smart_outlet_overview_name_text);
        mondayOperation = view.findViewById(R.id.smart_outlet_overview_monday_text);
        tuesdayOperation = view.findViewById(R.id.smart_outlet_overview_tuesday_text);
        wednesdayOperation = view.findViewById(R.id.smart_outlet_overview_wednesday_text);
        thursdayOperation = view.findViewById(R.id.smart_outlet_overview_thursday_text);
        fridayOperation = view.findViewById(R.id.smart_outlet_overview_friday_text);
        saturdayOperation = view.findViewById(R.id.smart_outlet_overview_saturday_text);
        sundayOperation = view.findViewById(R.id.smart_outlet_overview_sunday_text);
        consumptionGraph = view.findViewById(R.id.smart_outlet_overview_consumption_graph);
        addOperationButton = view.findViewById(R.id.smart_outlet_overview_add_operation_button);
        deleteOperationButton = view.findViewById(R.id.smart_outlet_overview_delete_operation_button);
        deleteDeviceButton = view.findViewById(R.id.smart_outlet_overview_delete_device_button);

        indexOfSmartOutlet = getArguments().getInt("indexOfSmartOutlet");

        //retrieving smart outlet data
        final MainActivity main = (MainActivity) getActivity();
        smartOutlet = main.getSmartOutlet(indexOfSmartOutlet);

        //setting the id and name field
        idTextView.setText(smartOutlet.getIdentificationNumber());
        nameTextView.setText(smartOutlet.getName());

        //setting the background color of outlet picture
        if (smartOutlet.getStatus()) {
            smartOutletPicture.setCardBackgroundColor(getResources().getColor(R.color.colorSmartOutletOn));
        } else {
            smartOutletPicture.setCardBackgroundColor(getResources().getColor(R.color.colorSmartOutletOff));
        }

        setWeekScheduleTextViews();
        setConsumptionGraphView();

        addOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add operation date");
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_smart_outlet_overview_add_operation_alert_dialog, null);
                builder.setView(dialogView);

                final EditText dayOfWeek = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_week_day);
                final EditText startHour = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_start_hour);
                final EditText durationMinutes = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_duration);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dowString = dayOfWeek.getText().toString();
                        String shString = startHour.getText().toString();
                        String dmString = durationMinutes.getText().toString();

                        if (dowString.isEmpty() || !dowString.matches("[0-9]+") || shString.isEmpty() ||
                                !shString.matches("[0-9]+") || dmString.isEmpty() || !dmString.matches("[0-9]+")) {
                            Toast.makeText(getActivity(), "Please fill out all the fields!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int dow = Integer.parseInt(dowString);
                        int sh = Integer.parseInt(shString);
                        int dm = Integer.parseInt(dmString);

                        if (dow < 1 || dow > 7) {
                            Toast.makeText(getActivity(), "Please enter a number between 1 and 7!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (sh < 0 || sh > 23) {
                            Toast.makeText(getActivity(), "Please enter a number between 0 and 23!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (dm < 10 || dm > 59) {
                            Toast.makeText(getActivity(), "Please enter a number between 1 and 59!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (smartOutlet.getWeekDay(dow-1).isThisDaySet()) {
                            Toast.makeText(getActivity(), "The selected day is already set!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        smartOutlet.getWeekDay(dow-1).setStartingHourAndMinute(sh, 0);
                        smartOutlet.getWeekDay(dow-1).setOperationDuration(dm);
                        smartOutlet.getWeekDay(dow-1).setThisDay(true);
                        setWeekScheduleTextViews();

                        main.updateFirebaseData();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        deleteOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete operation date");
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_smart_outlet_overview_delete_operation_date_alert_dialog, null);
                builder.setView(dialogView);

                final EditText dayOfWeek = dialogView.findViewById(R.id.smart_outlet_overview_delete_operation_week_day);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dowString = dayOfWeek.getText().toString();

                        if (dowString.isEmpty() || !dowString.matches("[0-9]+")) {
                            Toast.makeText(getActivity(), "Please fill out the field!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int dow = Integer.parseInt(dowString);

                        if (dow < 1 || dow > 7) {
                            Toast.makeText(getActivity(), "Please enter a number between 1 and 7!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!smartOutlet.getWeekDay(dow-1).isThisDaySet()) {
                            Toast.makeText(getActivity(), "The selected day is not set!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        smartOutlet.getWeekDay(dow-1).setThisDay(false);
                        setWeekScheduleTextViews();
                        main.updateFirebaseData();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        deleteDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure to delete this device?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment nextFrag= new SmartOutletsFragment();

                        Bundle bundle = new Bundle();
                        bundle.putInt("deleteSmartOutletUnderThisIndex", indexOfSmartOutlet);
                        nextFrag.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        return view;
    }

    private void setWeekScheduleTextViews() {
        //Monday operation text view setup
        if (smartOutlet.getWeekDay(0).isThisDaySet()) {
            mondayOperation.setText(assembleOperationString(0));
        } else {
            mondayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Tuesday operation text view setup
        if (smartOutlet.getWeekDay(1).isThisDaySet()) {
            tuesdayOperation.setText(assembleOperationString(1));
        } else {
            tuesdayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Wednesday operation text view setup
        if (smartOutlet.getWeekDay(2).isThisDaySet()) {
            wednesdayOperation.setText(assembleOperationString(2));
        } else {
            wednesdayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Thursday operation text view setup
        if (smartOutlet.getWeekDay(3).isThisDaySet()) {
            thursdayOperation.setText(assembleOperationString(3));
        } else {
            thursdayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Friday operation text view setup
        if (smartOutlet.getWeekDay(4).isThisDaySet()) {
            fridayOperation.setText(assembleOperationString(4));
        } else {
            fridayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Saturday operation text view setup
        if (smartOutlet.getWeekDay(5).isThisDaySet()) {
            saturdayOperation.setText(assembleOperationString(5));
        } else {
            saturdayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
        //Sunday operation text view setup
        if (smartOutlet.getWeekDay(6).isThisDaySet()) {
            sundayOperation.setText(assembleOperationString(6));
        } else {
            sundayOperation.setText(R.string.smart_outlet_overview_no_operation_set);
        }
    }

    @NonNull
    private String assembleOperationString(int index) {
        return smartOutlet.getWeekDay(index).getStartingHour() + ":00" + " - "
                + smartOutlet.getWeekDay(index).getStartingHour() + ":" + (smartOutlet.getWeekDay(index).getStartingMinute()+smartOutlet.getWeekDay(index).getOperationDuration());
    }

    private void setConsumptionGraphView() {
        DataPoint[] graphData = new DataPoint[smartOutlet.getConsumptionData().size()];
        for (int i=0; i<smartOutlet.getConsumptionData().size(); i++) {
            graphData[i] = new DataPoint(i, smartOutlet.getConsumptionData().get(i));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(graphData);
        consumptionGraph.setTitle("Consumption");
        consumptionGraph.setTitleColor(Color.rgb(255, 193, 7));
        series.setColor(Color.rgb(255, 193, 7));
        series.setThickness(7);
        consumptionGraph.addSeries(series);
    }
}
