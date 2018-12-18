package ro.sapientia.ms.seo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ro.sapientia.ms.seo.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button changeEmailButton = view.findViewById(R.id.change_email_button);
        Button changePasswordButton = view.findViewById(R.id.change_password_button);
        Button addNewOutletButton = view.findViewById(R.id.add_new_outlet_button);
        Button signOutButton = view.findViewById(R.id.sign_out_button);

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            TODO: OnClick function of change email
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            TODO: OnClick function of change password
            }
        });

        addNewOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            TODO: OnClick function of add new outlet
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            TODO: OnClick function of sign out
            }
        });

        return view;
    }
}
