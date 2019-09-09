package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    EditText updateFirstName, updateSurname, updateAge, updateHeight, updateWeight;
    Button confirm;

    boolean flag;
    String userFName, userSurname, userID;
    int userAge;
    double userWeight, userHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        getSupportActionBar().setTitle("Edit Profile");


        // TODO
        Populate();  // On load

        confirm = findViewById(R.id.btnConfirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm();
            }
        });
    }

    public void Populate(){

        // TODO: Pull from firebase DB &  populate editText
        try{

        }catch (Exception e){
            Toast.makeText(this, " Pull un-successful", Toast.LENGTH_LONG/**/).show();
        }
    }

    public void confirm(){

        updateFirstName = findViewById(R.id.editTextUserFirstname2);
        updateSurname = findViewById(R.id.editTextUserSurname2);
        updateAge = findViewById(R.id.editTextUserAge2);
        updateHeight = findViewById(R.id.editTextUserHeight2);
        updateWeight = findViewById(R.id.editTextUserWeight2);


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

                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
                if (TextUtils.isEmpty(updateFirstName.getText().toString().trim())) {
                    flag = false;
                    updateFirstName.setError("Fill Field");
                } else {
                    userFName = updateFirstName.getText().toString().trim();
                }
            }catch(Exception e) {
                flag = false;
                updateFirstName.setError("Invalid Input");
            }

            try{
                if (TextUtils.isEmpty(updateSurname.getText().toString().trim())) {
                    flag = false;
                    updateSurname.setError("Fill Field");
                }
                else{
                    userSurname = updateSurname.getText().toString().trim();
                }
            }catch(Exception e) {
                flag = false;
                updateSurname.setError("Invalid Input");
            }

            try{
                if (TextUtils.isEmpty(updateAge.getText().toString().trim())) {
                    flag = false;
                    updateAge.setError("Fill Field");
                }
                else{
                    userAge = Integer.parseInt(updateAge.getText().toString().trim());
                }
            }catch(Exception e) {
                flag = false;
                updateAge.setError("Invalid Input, must be a number");
            }

            try{
                if (TextUtils.isEmpty(updateHeight.getText().toString().trim())) {
                    flag = false;
                    updateHeight.setError("Fill Field");
                }else{
                    userHeight  = Double.parseDouble(updateHeight.getText().toString().trim());
                }
            }catch(Exception e) {
                flag = false;
                updateHeight.setError("Invalid Input, must be a number");
            }

            try{
                if (TextUtils.isEmpty(updateWeight.getText().toString().trim())) {
                    flag = false;
                    updateWeight.setError("Fill Field");
                }else{
                    userWeight = Double.parseDouble(updateWeight.getText().toString().trim());
                }
            }catch(Exception e) {
                flag = false;
                updateWeight.setError("Invalid Input, must be a number");
            }
    }


    // Menu expand
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Menu options selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings: // go to settings
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.sign_out: // sign current user out
                // TODO : Sign out user
                // currentUser = null;;
                return true;
            case R.id.profile: // Go to user profile
                Intent j = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(j);
                return true;
            case android.R.id.home:
                Intent h = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(h);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
