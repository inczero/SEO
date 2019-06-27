package ro.sapientia.ms.seo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import ro.sapientia.ms.seo.R;
import ro.sapientia.ms.seo.activity.LoginActivity;
import ro.sapientia.ms.seo.activity.MainActivity;
import ro.sapientia.ms.seo.model.SmartOutlet;

import static android.support.constraint.Constraints.TAG;

public class ManageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

//        Button changeEmailButton = view.findViewById(R.id.change_email_button);
        Button changePasswordButton = view.findViewById(R.id.change_password_button);
        Button addNewOutletButton = view.findViewById(R.id.add_new_outlet_button);
        Button signOutButton = view.findViewById(R.id.sign_out_button);

//        changeEmailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////            TODO: OnClick function of change email
//            }
//        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.fragment_manage_user_change_password, null);
                final EditText oldPass = mView.findViewById(R.id.old_pass_user);
                final EditText newPass = mView.findViewById(R.id.new_pass_user);
                final EditText confirmPass = mView.findViewById(R.id.confirm_pass_user);

                builder.setView(mView);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(oldPass.length() < 8 || newPass.length() < 8 || confirmPass.length() < 8) {
                            Toast.makeText(getActivity(),
                                    R.string.error_pass_length,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!confirmPass.getText().toString().equals(newPass.getText().toString()) ) {
                            Toast.makeText(getActivity(),
                                    R.string.error_pass_not_match,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!oldPass.getText().toString().isEmpty() && !newPass.getText().toString().isEmpty() && !confirmPass.getText().toString().isEmpty()){
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            try {
                                currentUser.updatePassword(newPass.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()){
                                            try {throw task.getException();}

                                            catch(FirebaseAuthWeakPasswordException e) {
                                                newPass.setError("Password too weak!");
                                                newPass.requestFocus();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            Toast.makeText(getActivity(), R.string.pass_changed, Toast.LENGTH_SHORT).show();
                                            Log.d(TAG,"Successful password change!" + task.isSuccessful());
                                        }
                                    }
                                });
                            } catch(NullPointerException e) {
                                Log.d(TAG,"Something went wrong!");
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), R.string.error_change_message, Toast.LENGTH_SHORT).show();
                        }
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

        addNewOutletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.temporary_fragment_new_device_registration, null);
                final EditText id = dialogView.findViewById(R.id.add_new_device_id_edit_text);
                final EditText name = dialogView.findViewById(R.id.add_new_device_name_edit_text);
                builder.setView(dialogView);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idString = id.getText().toString();
                        String nameString = name.getText().toString();

                        if (idString.isEmpty() || nameString.isEmpty()) {
                            Toast.makeText(getActivity(), "Please fill out all the fields!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MainActivity main = (MainActivity) getActivity();
                        main.getAllSmartOutletList().add(new SmartOutlet(idString, nameString));
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



        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }
}
