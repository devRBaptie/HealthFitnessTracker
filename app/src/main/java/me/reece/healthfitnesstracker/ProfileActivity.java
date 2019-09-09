package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button edit;
    TextView profileName, profileAge, profileWeight, profileHeight, profileBmi;
    private FirebaseAuth mAuth ;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth  = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference = database.getReference();
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {

            for(DataSnapshot ds: dataSnapshot.getChildren()){

                try {
                    User user = new User();

                    // PULL CURRENT USERS INFORMATION FROM FIREBASE

                    user.setFirstName(ds.child(userID).getValue(User.class).getFirstName());
                    user.setSurname(ds.child(userID).getValue(User.class).getSurname());
                    user.setAge(ds.child(userID).getValue(User.class).getAge());
                    user.setWeight(ds.child(userID).getValue(User.class).getWeight());
                    user.setHeight(ds.child(userID).getValue(User.class).getHeight());

                    profileName = findViewById(R.id.textViewProfileName);
                    profileAge = findViewById(R.id.textViewProfileAge);
                    profileWeight = findViewById(R.id.textViewProfileWeight);
                    profileHeight = findViewById(R.id.textViewProfileHeight);
                    profileBmi = findViewById(R.id.textViewProfileBMI);

                    // POPULATE VIEW WITH USERS INFORMATION
                    // DISPLAY NAME
                    profileName.setText(user.getFirstName() + " " + user.getSurname());
                    // AGE
                    profileAge.setText(user.getAge().toString());
                    // DISPLAY WEIGHT
                    profileWeight.setText(user.getWeight().toString());
                    // DISPLAY HEIGHT
                    profileHeight.setText(user.getHeight().toString());

                    // CALCULATE & DISPLAY BMI TO 2 DECIMAL PLACES
                    Double bmi;
                    bmi = user.getWeight() / user.getHeight();
                    bmi = bmi / user.getHeight();

                    profileBmi.setText(String.format("%.2f", bmi));
                }catch(Exception ex){
                    //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

                }

            }
    }

    @Override
    public void onStart() {
        super.onStart();

        getSupportActionBar().setTitle("User Profile");


        edit= findViewById(R.id.btnEditProfile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
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
                //currentUser = null;;
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
