package me.reece.healthfitnesstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnSignUp, btnLogin;
    private ProgressDialog PD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Sign-Up");


        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);


        inputEmail = (EditText) findViewById(R.id.editTextSignupEmail);
        inputPassword = (EditText) findViewById(R.id.editTextSignupPassword);

        btnSignUp = (Button) findViewById(R.id.btnRegister);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString();


                try {
                    if (isValidEmail(email)&& password.length() >= (6)) {
                        PD.show();

                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            Intent intent = new Intent(SignupActivity.this, SignUpDetailsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {

                                            try
                                            {
                                                throw task.getException();
                                            }catch (FirebaseAuthUserCollisionException existEmail) // if email already exists
                                            {
                                                Toast.makeText(SignupActivity.this,"Email already exists",Toast.LENGTH_SHORT).show();

                                            }
                                            catch (Exception e)
                                            {

                                            }
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {

                        if(password.trim().equals("") || password.length() < 6) {
                            inputPassword.setError("Password must be at least 6 characters");
                        }
                        if(!isValidEmail(inputEmail.getText())) {
                            inputEmail.setError("Invalid email");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this,"User already exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // returns true if email is valid and false if invalid
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

