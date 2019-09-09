package me.reece.healthfitnesstracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StepsActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorM;

    private TextView tvStepCounter;
    private ProgressBar PB;
    private int progressStatus;
    private Double Steps;
    private Double targetSteps;
    private String userID;

    private Handler hand =new Handler();

    boolean running = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvStepCounter = findViewById(R.id.textViewStepCounter);
        PB = findViewById(R.id.progressBar);

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //showData(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(progressStatus <= 100){

                    progressStatus++;
                    android.os.SystemClock.sleep(50);
                    //progressStatus = (int) (Steps/targetSteps)*100;

                    hand.post(new Runnable() {
                        @Override
                        public void run() {
                            if(progressStatus<100){
                                progressStatus = 100;
                            }
                            PB.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start();

        sensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            Goals goal = new Goals();

            // PULL CURRENT USERS INFORMATION FROM FIREBASE
            try {
                goal.setSteps(ds.child(userID).getValue(Goals.class).getSteps());

                targetSteps = Double.parseDouble(goal.getSteps());
            }catch(Exception ex){

            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Step Counter");
    }

    @Override
    protected void onResume() {
        super.onResume();

        running = true;

        Sensor countSensor = sensorM.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countSensor != null){
            sensorM.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        running = false;
        sensorM.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(running){
            Steps = Double.valueOf(sensorEvent.values[0]);
            tvStepCounter.setText(Steps.toString());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
