package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import ipl.android_projet.model.Dao;

public class MainActivity extends AppCompatActivity {

    Dao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new Dao(this);
        dao.open();

        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);

        toolBar.setTitle(R.string.app_titre);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addUser);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent addUserIntent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(addUserIntent);
            }
        });


        String htmlUrl = "file:///android_asset/Explication.html";
        WebView webView = (WebView) findViewById(R.id.webView_content_main);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(htmlUrl);


        Log.i("TEST",""+dao.getAllPlayers().getCount());

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


    public void demarrer(View view){

        EditText editText = (EditText) findViewById(R.id.edit_prenom_content_main);
        String prenom = editText.getText().toString();
        if(prenom.isEmpty()){
            Toast.makeText(getApplicationContext(), "Veuillez entrer un nom.", Toast.LENGTH_SHORT).show();
        }
        else if(!dao.getPrenom(prenom)){
            Toast.makeText(getApplicationContext(), "Ce nom n'existe pas", Toast.LENGTH_SHORT).show();
        }

        else{
            Intent intentDemarrer = new Intent(MainActivity.this,ListingEpreuvesActivity.class);
            intentDemarrer.putExtra("prenom",prenom);
            startActivity(intentDemarrer);
        }



    }
}
