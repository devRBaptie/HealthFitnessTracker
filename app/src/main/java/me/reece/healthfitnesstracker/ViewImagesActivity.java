package me.reece.healthfitnesstracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class ViewImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Images");
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
