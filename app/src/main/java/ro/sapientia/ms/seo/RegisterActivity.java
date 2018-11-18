package ro.sapientia.ms.seo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView firstNameTextView = findViewById(R.id.registerFirstNameInput);
        final TextView lastNameTextView = findViewById(R.id.registerLastNameInput);
        final TextView emailTextView = findViewById(R.id.registerEmailInput);
        final TextView passwordTextView = findViewById(R.id.registerPasswordInput);
        final TextView reenterPasswordTextView = findViewById(R.id.registerReenterPasswordInput);
        Button signUpButton = findViewById(R.id.emailSignUpButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameTextView.getText().toString();
                final String lastName = lastNameTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String reenterPassword = reenterPasswordTextView.getText().toString();

                if (firstName.isEmpty()) {
                    firstNameTextView.setError("Please enter your first name!");
                    firstNameTextView.requestFocus();
                    return;
                }

                if (lastName.isEmpty()) {
                    lastNameTextView.setError("Please enter your last name!");
                    lastNameTextView.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    emailTextView.setError("Please enter your email address!");
                    emailTextView.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordTextView.setError("Please enter a password!");
                    passwordTextView.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    passwordTextView.setError("The password must be at least 8 characters long!");
                    passwordTextView.requestFocus();
                    return;
                }

                if (reenterPassword.isEmpty()) {
                    reenterPasswordTextView.setError("Please enter your password here again!");
                    reenterPasswordTextView.requestFocus();
                    return;
                }

                if (password.compareTo(reenterPassword) != 0) {
                    reenterPasswordTextView.setError("Your password does not match! Please reenter it!");
                    reenterPasswordTextView.requestFocus();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                emailTextView.setError("This email is already registered!");
                                emailTextView.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, "An error happened!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_LONG).show();

                            ArrayList<SmartSocket> ownedProducts = new ArrayList<>();

                            User newUserInformation = new User(firstName, lastName, "", "", ownedProducts);

                            FirebaseUser newUserAuth = FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference.child(newUserAuth.getUid()).setValue(newUserInformation);
                            startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                        }
                    }
                });

            }
        });


    }
}
