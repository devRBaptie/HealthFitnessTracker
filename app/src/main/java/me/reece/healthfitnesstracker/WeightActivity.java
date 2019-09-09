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

public class WeightActivity extends AppCompatActivity {

    TextView targetWeight, currentweight;
    EditText inputWeight;
    Button btnWeight;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        targetWeight =findViewById(R.id.textViewTargetWeight);
        currentweight = findViewById(R.id.textViewUsersWeight);

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

        btnWeight = findViewById(R.id.btnUpdateWeight);

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadWeight();
            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            Goals goal = new Goals();
            Weight w = new Weight();

            // PULL CURRENT USERS INFORMATION FROM FIREBASE
            try {
                goal.setWeight(ds.child(userID).getValue(Goals.class).getWeight());

                // POPULATE VIEW WITH USERS INFORMATION
                // TARGET WEIGHT
                targetWeight.setText(goal.getWeight());
            }catch(Exception ex){

            }
            try {
                w.setWeight(ds.child(userID).getValue(Weight.class).getWeight());

                // POPULATE VIEW WITH USERS INFORMATION
                // CURRENT WEIGHT
                currentweight.setText(w.getWeight().toString());
            }catch(Exception ex){

            }
        }
    }

    private void UploadWeight(){



        inputWeight = findViewById(R.id.editTextUsersNewWeight);

        try{
            Weight weight = new Weight();

            FirebaseAuth mAuth  = FirebaseAuth.getInstance();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            weight.setWeight(Double.parseDouble(inputWeight.getText().toString().trim()));

            reference.child("Weight").child(userID).setValue(weight);

            Toast.makeText(this, " Push update successful ", Toast.LENGTH_SHORT).show();
        }catch(Exception e){

        }
    }
    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Weight Tracker");
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
