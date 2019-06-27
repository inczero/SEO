package ro.sapientia.ms.seo.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.activity.MainActivity;
import ro.sapientia.ms.seo.model.SmartOutlet;
import ro.sapientia.ms.seo.model.WeekDay;

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
        final CardView smartOutletPicture = view.findViewById(R.id.smart_outlet_overview_card_view);
        TextView idTextView = view.findViewById(R.id.smart_outlet_overview_id_text);
        final TextView nameTextView = view.findViewById(R.id.smart_outlet_overview_name_text);
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

        //initialize schedule data
        DatabaseReference databaseSchedule = FirebaseDatabase.getInstance().getReference("Users").child(main.getFirebaseUserId())
                .child("ownedProducts").child(Integer.toString(indexOfSmartOutlet)).child("weekSchedule");

        databaseSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot weekDaySnapshot: dataSnapshot.getChildren()) {
                    WeekDay weekDay = weekDaySnapshot.getValue(WeekDay.class);
                    smartOutlet.setOperationWeekDay(i, weekDay.getStartingHour(), weekDay.getStartingMinute(), weekDay.getOperationDuration());
                    smartOutlet.setWeekDayStatus(i, weekDay.isThisDaySet());
                    i++;
                }
                setWeekScheduleTextViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error!", Toast.LENGTH_SHORT).show();
            }
        });

        //initialize consumption data
        DatabaseReference databaseConsumption = FirebaseDatabase.getInstance().getReference("Users").child(main.getFirebaseUserId())
                .child("ownedProducts").child(Integer.toString(indexOfSmartOutlet)).child("consumptionData");

        databaseConsumption.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Integer> consumptionList = new ArrayList<>();
                for (DataSnapshot consumptionSnapshot: dataSnapshot.getChildren()) {
                    int consumption = consumptionSnapshot.getValue(Integer.class);
                    consumptionList.add(consumption);
                    smartOutlet.setConsumptionData(consumptionList);
                }
                setConsumptionGraphView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error!", Toast.LENGTH_SHORT).show();
            }
        });

        smartOutletPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smartOutlet.getStatus()) {
                    smartOutlet.setStatus(false);
                    smartOutletPicture.setCardBackgroundColor(getResources().getColor(R.color.colorSmartOutletOff));
                    main.switchOutlet(indexOfSmartOutlet, false);
                } else {
                    smartOutlet.setStatus(true);
                    smartOutletPicture.setCardBackgroundColor(getResources().getColor(R.color.colorSmartOutletOn));
                    main.switchOutlet(indexOfSmartOutlet, true);
                }
            }
        });

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Change smart outlet name");
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_smart_outlet_overview_name_change, null);
                builder.setView(dialogView);

                final EditText newName = dialogView.findViewById(R.id.new_smart_outlet_name);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newNameString = newName.getText().toString();

                        if (newNameString.isEmpty()) {
                            Toast.makeText(getActivity(), "Please fill out the new name field!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        main.setSmartOutletName(indexOfSmartOutlet, newNameString);
                        smartOutlet.setName(newNameString);
                        nameTextView.setText(newNameString);
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

        addOperationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add operation date");
                View dialogView = getLayoutInflater().inflate(R.layout.fragment_smart_outlet_overview_add_operation_alert_dialog, null);
                builder.setView(dialogView);

                final EditText dayOfWeek = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_week_day);
                final EditText startHour = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_start_hour);
                final EditText startMinute = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_start_minute);
                final EditText durationMinutes = dialogView.findViewById(R.id.smart_outlet_overview_add_operation_duration);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dowString = dayOfWeek.getText().toString();
                        String shString = startHour.getText().toString();
                        String smString = startMinute.getText().toString();
                        String dmString = durationMinutes.getText().toString();

                        if (dowString.isEmpty() || !dowString.matches("[0-9]+") || shString.isEmpty() ||
                                !shString.matches("[0-9]+")|| smString.isEmpty() ||
                                !smString.matches("[0-9]+") || dmString.isEmpty() || !dmString.matches("[0-9]+")) {
                            Toast.makeText(getActivity(), "Please fill out all the fields correctly!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int dow = Integer.parseInt(dowString); //day of week
                        int sh = Integer.parseInt(shString); //starting hour
                        int sm = Integer.parseInt(smString); //starting minute
                        int dm = Integer.parseInt(dmString); //duration minutes

                        if (dow < 1 || dow > 7) {
                            Toast.makeText(getActivity(), "Please enter a number between 1 and 7!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (sh < 0 || sh > 23) {
                            Toast.makeText(getActivity(), "Please enter a number between 0 and 23!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (sm < 0 || sm > 59) {
                            Toast.makeText(getActivity(), "Please enter a number between 0 and 59!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (dm < 1 || dm > 1440) {
                            Toast.makeText(getActivity(), "Please enter a number between 1 and 1440!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (smartOutlet.getWeekDay(dow-1).isThisDaySet()) {
                            Toast.makeText(getActivity(), "The selected day is already set!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        smartOutlet.setOperationWeekDay(dow-1, sh, sm, dm);
                        setWeekScheduleTextViews();

                        main.setOperationDay(indexOfSmartOutlet, dow-1, smartOutlet.getWeekDay(dow-1));
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
                        main.deleteOperationDay(indexOfSmartOutlet, dow-1);
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
        Calendar timeHolder = Calendar.getInstance();

        timeHolder.set(Calendar.HOUR_OF_DAY, smartOutlet.getWeekDay(index).getStartingHour());
        timeHolder.set(Calendar.MINUTE, smartOutlet.getWeekDay(index).getStartingMinute());
        Date start = timeHolder.getTime();

        timeHolder.add(Calendar.MINUTE, smartOutlet.getWeekDay(index).getOperationDuration());
        Date finish = timeHolder.getTime();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        return format.format(start) + " - " + format.format(finish);
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
