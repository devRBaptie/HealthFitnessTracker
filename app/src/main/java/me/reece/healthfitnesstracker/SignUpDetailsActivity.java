package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpDetailsActivity extends AppCompatActivity {

    EditText FirstName, Surname, Age, Height, Weight;
    Button confirm;
    boolean flag;

    String userFName, userSurname, userID;
    int userAge;
    double userWeight, userHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

    }

    @Override
    public void onStart() {
        super.onStart();

        getSupportActionBar().setTitle("New Users");


        Height = findViewById(R.id.editTextUserHeight);

        confirm = findViewById(R.id.btnConfirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm();
            }
        });

    }

    public void confirm(){

        FirstName = findViewById(R.id.editTextUserFirstname);
        Surname = findViewById(R.id.editTextUserSurname);
        Age = findViewById(R.id.editTextUserAge);
        Height = findViewById(R.id.editTextUserHeight);
        Weight = findViewById(R.id.editTextUserWeight);


        flag = true;

        DataValidation();

        if (flag) { // Data is valid

            try{
                User user = new User();
                // ASSIGN USER INPUT TO OBJECT VALUES
                user.setFirstName(userFName);
                user.setSurname(userSurname);
                user.setAge(userAge);
                user.setHeight(userHeight);
                user.setWeight(userWeight);


                // CONNECT TO FIREBASE DATABASE
                try {
                    FirebaseAuth mAuth  = FirebaseAuth.getInstance();
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    userID = firebaseUser.getUid();

                    // PUSH USERS INFORMATION TO FIREBASE
                    // USING CURRENT USERS ID AND PUSHING A USER OBJECT AS DATA
                    reference.child("Users").child(userID).setValue(user);

                    Toast.makeText(this, " Push update successful ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);

                }catch(Exception ex){
                    Toast.makeText(this, " Push Failed", Toast.LENGTH_SHORT).show();
                }



            }catch(Exception e){
                Toast.makeText(this, " Push update un-successful", Toast.LENGTH_LONG/**/).show();
            }
        }else{
            Toast.makeText(this, " Push update un-successful DataValidation", Toast.LENGTH_LONG/**/).show();

        }

    }

    public void DataValidation (){

        try {
            if (TextUtils.isEmpty(FirstName.getText().toString().trim())) {
                flag = false;
                FirstName.setError("Fill Field");
            } else {
                userFName = FirstName.getText().toString().trim();
            }
        }catch(Exception e) {
            flag = false;
            FirstName.setError("Invalid Input");
        }

        try{
            if (TextUtils.isEmpty(Surname.getText().toString().trim())) {
                flag = false;
                Surname.setError("Fill Field");
            }
            else{
                userSurname = Surname.getText().toString().trim();
            }
        }catch(Exception e) {
            flag = false;
            Surname.setError("Invalid Input");
        }

        try{
            if (TextUtils.isEmpty(Age.getText().toString().trim())) {
                flag = false;
                Age.setError("Fill Field");
            }
            else{
                userAge = Integer.parseInt(Age.getText().toString().trim());
            }
        }catch(Exception e) {
            flag = false;
            Age.setError("Invalid Input, must be a number");
        }

        try{
            if (TextUtils.isEmpty(Height.getText().toString().trim())) {
                flag = false;
                Height.setError("Fill Field");
            }else{
                userHeight  = Double.parseDouble(Height.getText().toString().trim());
            }
        }catch(Exception e) {
            flag = false;
            Height.setError("Invalid Input, must be a number");
        }

        try{
            if (TextUtils.isEmpty(Weight.getText().toString().trim())) {
                flag = false;
                Weight.setError("Fill Field");
            }else{
                userWeight = Double.parseDouble(Weight.getText().toString().trim());
            }
        }catch(Exception e) {
            flag = false;
            Weight.setError("Invalid Input, must be a number");
        }
    }


}
