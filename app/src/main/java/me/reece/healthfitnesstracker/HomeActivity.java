package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();

        getSupportActionBar().setTitle("Home");


        ImageView goalsImage = findViewById(R.id.goalsImageView);
        ImageView stepsImage = findViewById(R.id.stepsImageView);
        ImageView weightImage = findViewById(R.id.weightImageView);
        ImageView sleepImage = findViewById(R.id.sleepImageView);
        ImageView imagesImage = findViewById(R.id.imagesImageView);

        goalsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GoalsActivity.class);
                startActivity(i);
            }
        });

        stepsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), StepsActivity.class);
                startActivity(i);
            }
        });

        weightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WeightActivity.class);
                startActivity(i);
            }
        });

        sleepImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SleepActivity.class);
                startActivity(i);
            }
        });

        imagesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ImagesActivity.class);
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
                Intent k = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(k);
                return true;
            case R.id.profile: // Go to user profile
                Intent j = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(j);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
