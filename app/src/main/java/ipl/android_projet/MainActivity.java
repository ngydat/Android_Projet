package ipl.android_projet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);

        toolBar.setTitle(R.string.app_titre);

        String htmlUrl = "file:///android_asset/Explication.html";
        WebView webView = (WebView) findViewById(R.id.webView_content_main);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(htmlUrl);

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
        Context context = getApplicationContext();
        CharSequence text = "Veuillez entrer un nom.";
        int duration = Toast.LENGTH_SHORT;


        EditText editText = (EditText) findViewById(R.id.edit_prenom_content_main);
        String prenom = editText.getText().toString();
        if(prenom.isEmpty()){
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            Intent intentDemarrer = new Intent(MainActivity.this,ListingEpreuvesActivity.class);
            intentDemarrer.putExtra("prenom",prenom);
            startActivity(intentDemarrer);
        }




    }
}
