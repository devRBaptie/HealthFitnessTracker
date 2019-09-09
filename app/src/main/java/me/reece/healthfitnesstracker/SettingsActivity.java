package me.reece.healthfitnesstracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {
    TextView measurementSettings, goalWeightMeasurement, settingsName;
    Switch changeMeasurementSwitch;
    private String metric, imperial;
    public String currentState = "";
    private FirebaseAuth mAuth ;
    String userID;
    private ProgressDialog PD;
    Button  BTNEditProfleSettings, BTNSignOutSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        measurementSettings = findViewById(R.id.textViewSettingsMeasurement);
        //goalWeightMeasurement = findViewById(R.id.textViewGoalWeightMeasurement);
        BTNEditProfleSettings = findViewById(R.id.btnEditProfileSettings);

        metric = "cm, m, Kg";
        imperial = "Feet, lb, s";

        changeMeasurementSwitch = findViewById(R.id.switchSettingsMeasurement);
        BTNSignOutSettings = findViewById(R.id.btnSignOutSettings);
        settingsName = findViewById(R.id.textViewSettingsName);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void showData(DataSnapshot dataSnapshot){

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            try {
                User user = new User();

                user.setFirstName(ds.child(userID).getValue(User.class).getFirstName());
                user.setSurname(ds.child(userID).getValue(User.class).getSurname());

                settingsName.setText(user.getFirstName() + " " + user.getSurname());
            }catch(Exception e){
                Toast.makeText(this, "Error 2", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Settings");



        // EDIT PROFILE FROM SETTINGS
        BTNEditProfleSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);            }
        });
        changeMeasurementSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(measurementSettings.getText().toString().equals(imperial)){
                    measurementSettings.setText(metric);
                    //goalWeightMeasurement.setText(metric);
                    currentState = metric;
                }else{
                    measurementSettings.setText(imperial);
                    //goalWeightMeasurement.setText(imperial);
                    currentState = imperial;
                }
            }
        });


        // SIGN OUT OF CURRENT PROFILE
        BTNSignOutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
                mAuth.signOut();
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
