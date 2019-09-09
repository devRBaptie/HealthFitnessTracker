package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoalsActivity extends AppCompatActivity {

    EditText GoalDailySteps, GoalTargetWeight, GoalTargetBedTime, GoalTargetWakeUpTime;
    Button btnUpdate;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GoalDailySteps = findViewById(R.id.editTextGoalSteps);
        GoalTargetWeight= findViewById(R.id.editTextGoalWeight);
        GoalTargetBedTime= findViewById(R.id.editTextGoalSleepTime);
        GoalTargetWakeUpTime = findViewById(R.id.editTextGoalWakeTime);

        getSupportActionBar().setTitle("Goals");

        ImageView img =  findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        btnUpdate = findViewById(R.id.btnUpdateGoals);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Update
                Goals();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void Goals(){

        try{
            Goals goals = new Goals();

            FirebaseAuth mAuth  = FirebaseAuth.getInstance();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            goals.setSteps(GoalDailySteps.getText().toString().trim());
            goals.setWeight(GoalTargetWeight.getText().toString().trim());
            goals.setSleepTime(GoalTargetBedTime.getText().toString().trim());
            goals.setWakeTime(GoalTargetWakeUpTime.getText().toString().trim());

            // PUSH USERS INFORMATION TO FIREBASE
            // USING CURRENT USERS ID AND PUSHING A USER OBJECT AS DATA
            reference.child("Goals").child(userID).setValue(goals);

            Toast.makeText(this, " update successful ", Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            Toast.makeText(this, " update un-successful ", Toast.LENGTH_SHORT).show();
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
