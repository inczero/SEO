package ro.sapientia.ms.seo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO : Check if Date holders and outlet holders are empty.
        //TODO : Check if new date is added and update the list.
        //TODO : Put loading icon when downloading takes too much time.
        //TODO : Change the selected color of Bottom Navigation View.

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_profile :
                        selectedFragment = new ProfileFragment();
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
}
