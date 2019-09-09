package me.reece.healthfitnesstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button signup, login;
    private FirebaseAuth mAuth ;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    String userID;
    private ProgressDialog PD;


    String passedEmail, passedPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Login");

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnSignUp);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(j);
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //String outputUser= currentUser = currentUser.toString();

        // TODO: * Clear current user(No auto login) *Change for POE
        currentUser = null;

        if (currentUser != null) {
            Toast.makeText(this, " is already signed in ", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);

        } else {

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PD.show();
                    // Get Email and Password from TextEdit (Text Field)
                    email = findViewById(R.id.editTextEmail);
                    password = findViewById(R.id.editTextPassword);


                    //Parse email and Password from object to String
                    passedPassword = password.getText().toString().trim();
                    passedEmail = email.getText().toString();


                    //Call loginUser method with email and password as perimeters
                    loginUser(passedEmail, passedPassword);

                }
            });
        }
    }

// LOGIN IN USERS METHOD
    public void loginUser(final String email, final String password) {



        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            EditText Email, Password;
                            Email = findViewById(R.id.editTextEmail);
                            Password = findViewById(R.id.editTextPassword);

                            if (task.isSuccessful()) {

                                mAuth  = FirebaseAuth.getInstance();
                                database=FirebaseDatabase.getInstance();
                                reference = database.getReference();
                                final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                userID = firebaseUser.getUid();

                                reference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                            User user = new User();

                                            //user.setFirstName(ds.child(userID).getValue(User.class).getFirstName());
                                            //Toast.makeText(LoginActivity.this, "Welcome Back " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                                            PD.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);

                            } else {
                                try
                                {
                                    // details are incorrect
                                    throw task.getException();
                                }

                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                {
                                    if(passedPassword.length() < 6 && isValidEmail(passedEmail)) {  //
                                        PD.dismiss();
                                        Password.setError("Password must be at least 6 characters");
                                    }else if (isValidEmail(passedEmail)){
                                        PD.dismiss();
                                        Toast.makeText(LoginActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();

                                    }else if(!isValidEmail(passedEmail)) {
                                        PD.dismiss();
                                        Email.setError("Email not valid");
                                    }else {
                                        PD.dismiss();
                                        //INCORRECT PASSWORD
                                        Password.setError("");
                                        // TODO: Set errors
                                    }
                                }

                                catch (Exception e)
                                {
                                    if(!isValidEmail(passedEmail)) {
                                        PD.dismiss();
                                        Email.setError("Email not valid");
                                    }else {
                                        PD.dismiss();
                                        // INCORRECT EMAIL
                                        Toast.makeText(LoginActivity.this, "Email doesn't exist, Sign-up", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            PD.dismiss();
                        }


                    });
        }catch(Exception e){

            Toast.makeText(LoginActivity.this, "Incorrect input details", Toast.LENGTH_SHORT).show();
        }

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
