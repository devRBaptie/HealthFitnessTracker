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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SleepActivity extends AppCompatActivity {

    TextView sleepTime, wakeTime;
    EditText inputSleepTime, inputWakeTime;
    Button sleepInput;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sleepTime = findViewById(R.id.textViewTargetSleepTime);
        wakeTime = findViewById(R.id.textViewTargetWakeTime);

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
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

        sleepInput = findViewById(R.id.btnSleepTimes);

        sleepInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepTimes();
            }
        });

    }
    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            Goals goal = new Goals();
            Sleep sleep = new Sleep();

            // PULL CURRENT USERS INFORMATION FROM FIREBASE
            try {
                goal.setSleepTime(ds.child(userID).getValue(Goals.class).getSleepTime());
                goal.setWakeTime(ds.child(userID).getValue(Goals.class).getWakeTime());

                // POPULATE VIEW WITH USERS INFORMATION
                // TARGET WEIGHT
                sleepTime.setText(goal.getSleepTime());
                wakeTime.setText(goal.getWakeTime());
            }catch(Exception ex){

            }

        }
    }

    public void SleepTimes(){

        inputSleepTime = findViewById(R.id.editTextInputSleepTime);
        inputWakeTime = findViewById(R.id.editTextInputWakeTime);

        try{
            Sleep s =new Sleep();

            FirebaseAuth mAuth  = FirebaseAuth.getInstance();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            s.setSleepTime(inputSleepTime.getText().toString().trim());
            s.setWakeTime(inputWakeTime.getText().toString().trim());

            reference.child("Sleep").child(userID).setValue(s);

            Toast.makeText(this, " Push update successful ", Toast.LENGTH_SHORT).show();
        }catch(Exception e){

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Sleep Times");
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
