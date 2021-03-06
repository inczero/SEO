package ro.sapientia.ms.seo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import ro.sapientia.ms.seo.R;

public class LoginActivity extends AppCompatActivity {

    //Firebase attributes
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isOnline()) {
            setContentView(R.layout.activity_no_network);
            Button exitButton = findViewById(R.id.exit_button);
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            setContentView(R.layout.activity_login);

            //Layout elements
            final EditText emailEditText = findViewById(R.id.email_input);
            final EditText passwordEditText = findViewById(R.id.password_input);
            Button loginButton = findViewById(R.id.email_login_button);
            Button forgotPasswordButton = findViewById(R.id.forgot_password_button);
            TextView signUpTextView = findViewById(R.id.sign_up_text_view);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

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
                        passwordEditText.setError("Please enter your password!");
                        passwordEditText.requestFocus();
                        return;
                    }

                    firebaseAuth = FirebaseAuth.getInstance();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    View v = findViewById(android.R.id.content);
                                    Snackbar.make(v, "Invalid user!", Snackbar.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    View v = findViewById(android.R.id.content);
                                    Snackbar.make(v, "Login failed!", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            });

            forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = emailEditText.getText().toString();

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

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        View v = findViewById(android.R.id.content);
                                        Snackbar.make(v, "Password reset email sent!", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });

            signUpTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finish();
                }
            });
        }
    }

    //Check for internet connection
    public boolean isOnline() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }
}
