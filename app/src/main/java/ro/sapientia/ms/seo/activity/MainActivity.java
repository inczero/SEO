package ro.sapientia.ms.seo.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.seo.fragment.ManageFragment;
import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.fragment.ScheduleFragment;
import ro.sapientia.ms.seo.fragment.SmartOutletsFragment;
import ro.sapientia.ms.seo.model.SmartOutlet;
import ro.sapientia.ms.seo.model.User;
import ro.sapientia.ms.seo.model.WeekDay;

public class MainActivity extends AppCompatActivity {

    //Firebase attributes
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUserNode;

    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = new User();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabaseUserNode = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        mDatabaseUserNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_profile :
                        selectedFragment = new ManageFragment();
                        break;
                    case R.id.nav_outlets :
                        selectedFragment = new SmartOutletsFragment();
                        break;
                    case R.id.nav_schedule :
                        selectedFragment = new ScheduleFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                //true because we want to select the clicked item
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SmartOutletsFragment()).commit();
    }

    //debug function
    private void countWeekOperation(SmartOutlet smartOutlet) {
        int counter = 0;
        for (int i=0; i<7; i++) {
            if (smartOutlet.getWeekDay(i).isThisDaySet()) {
                counter++;
            }
        }
        Toast.makeText(this, Integer.toString(counter), Toast.LENGTH_SHORT).show();
    }

    //functions for fragment data retrieval and firebase update
    public ArrayList<SmartOutlet> getAllSmartOutletList() {
        return userData.getOwnedProducts();
    }

    public SmartOutlet getSmartOutlet(int index) {
        return userData.getSmartOutlet(index);
    }

    public void updateFirebaseData() {
        mDatabaseUserNode.setValue(userData);
    }

    public void getSchedule(int index) {
        for (int day=0; day<7; day++) {
            try {
                final List<WeekDay> schedule = new ArrayList<WeekDay>(7);
                mDatabaseUserNode.child("ownedProducts").child(Integer.toString(index)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //schedule = dataSnapshot.getValue(SmartOutlet.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                //return outlet;
            } catch(NullPointerException e) {
                Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        }

    }
//    try {
//        outlet = new SmartOutlet();
//        mDatabaseUserNode.child("ownedProducts").child(Integer.toString(index)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                outlet = dataSnapshot.getValue(SmartOutlet.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
//        return outlet;
//    } catch(NullPointerException e) {
//        Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
//    }
//
//        return outlet;

    public void switchOutlet(int numberOfOutlet, boolean status) {
        try {
            mDatabaseUserNode.child("ownedProducts").child(Integer.toString(numberOfOutlet)).child("status").setValue(status);
        } catch(NullPointerException e) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOperationDay(int numberOfOutlet, int numberOfDay, WeekDay day) {
        try {
            mDatabaseUserNode.child("ownedProducts").child(Integer.toString(numberOfOutlet)).child("weekSchedule").
                    child(Integer.toString(numberOfDay)).setValue(day);
        } catch(NullPointerException e) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOperationDay(int numberOfOutlet, int numberOfDay) {
        try {
            mDatabaseUserNode.child("ownedProducts").child(Integer.toString(numberOfOutlet)).child("weekSchedule").
                    child(Integer.toString(numberOfDay)).child("thisDaySet").setValue(false);
        } catch(NullPointerException e) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFirebaseUserId() {
        String userId = "";
        try {
            userId = mAuth.getCurrentUser().getUid();
            return userId;
        } catch (NullPointerException e) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
        }

        return userId;
    }

    public void setSmartOutletName(int numberOfOutlet, String name) {
        try {
            mDatabaseUserNode.child("ownedProducts").child(Integer.toString(numberOfOutlet)).
                    child("name").setValue(name);
        } catch(NullPointerException e) {
            Toast.makeText(this, "Database error!", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Implement WiFi config (class+methods+GUI)
}
