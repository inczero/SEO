package ro.sapientia.ms.seo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.EditText;
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

        final EditText firstNameEditText = findViewById(R.id.registerFirstNameInput);
        final EditText lastNameEditText = findViewById(R.id.registerLastNameInput);
        final EditText emailEditText = findViewById(R.id.registerEmailInput);
        final EditText passwordEditText = findViewById(R.id.registerPasswordInput);
        final EditText reenterPasswordEditText = findViewById(R.id.registerReenterPasswordInput);
        Button signUpButton = findViewById(R.id.emailSignUpButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String reenterPassword = reenterPasswordEditText.getText().toString();

                if (firstName.isEmpty()) {
                    firstNameEditText.setError("Please enter your first name!");
                    firstNameEditText.requestFocus();
                    return;
                }

                if (lastName.isEmpty()) {
                    lastNameEditText.setError("Please enter your last name!");
                    lastNameEditText.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    emailEditText.setError("Please enter your email address!");
                    emailEditText.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Invalid email address!");
                    emailEditText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEditText.setError("Please enter a password!");
                    passwordEditText.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    passwordEditText.setError("The password must be at least 8 characters long!");
                    passwordEditText.requestFocus();
                    return;
                }

                if (reenterPassword.isEmpty()) {
                    reenterPasswordEditText.setError("Please enter your password here again!");
                    reenterPasswordEditText.requestFocus();
                    return;
                }

                if (password.compareTo(reenterPassword) != 0) {
                    reenterPasswordEditText.setError("Your password does not match! Please reenter it!");
                    reenterPasswordEditText.requestFocus();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                emailEditText.setError("This email is already registered!");
                                emailEditText.requestFocus();
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
