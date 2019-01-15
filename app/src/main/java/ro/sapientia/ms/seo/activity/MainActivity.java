package ro.sapientia.ms.seo.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ro.sapientia.ms.seo.fragment.ManageFragment;
import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.fragment.ScheduleFragment;
import ro.sapientia.ms.seo.fragment.SmartOutletsFragment;
import ro.sapientia.ms.seo.model.SmartOutlet;
import ro.sapientia.ms.seo.model.User;

public class MainActivity extends AppCompatActivity {

    //Firebase attributes
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData = new User();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
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

    //functions for fragment data retrieval and firebase update
    public ArrayList<SmartOutlet> getAllSmartOutletList() {
        return userData.getOwnedProducts();
    }

    public SmartOutlet getSmartOutlet(int i) {
        return userData.getOwnedProducts().get(i);
    }

    public void updateFirebaseData() {
        mDatabase.setValue(userData);
    }
}
