package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class LocalisationActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_localisation);

            Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
            toolBar.setTitle(R.string.titre_tool_localisation);

            Intent intent = getIntent();
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude",0);


            TextView latitudeView = (TextView) findViewById(R.id.latitude);
            latitudeView.setText(String.valueOf(latitude));
            TextView longitudeView = (TextView) findViewById(R.id.longitude);
            longitudeView.setText(String.valueOf(longitude));


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        public void commencer(View v){

            Intent intentListing = new Intent(LocalisationActivity.this,ListingEpreuvesActivity.class);
            intentListing.putExtra("latitude",latitude);
            intentListing.putExtra("longitude",longitude);
            startActivity(intentListing);
        }

    }



