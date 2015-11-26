package ipl.android_projet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ipl.android_projet.model.Dao;
import ipl.android_projet.model.Joueur;

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
                loginDialog();
            }
        });


        String htmlUrl = "file:///android_asset/Explication.html";
        WebView webView = (WebView) findViewById(R.id.webView_content_main);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(htmlUrl);


        Log.i("TEST",""+dao.getAllPlayers().getCount());

    }

    //http://stackoverflow.com/questions/12402983/i-want-to-show-my-login-form-in-dialog-box-or-in-pop-up-box-using-android
    private void loginDialog() {
        Dialog login = new Dialog(this);
        login.setContentView(R.layout.content_add_user);
        login.setCancelable(true);
        login.setTitle("Nom d'utilisateur :");


        final String pseudo = ((EditText) login.findViewById(R.id.yourName)).getText().toString();
        Button confirm = (Button) login.findViewById(R.id.confirmAddUser);

        login.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dao.getPseudo(pseudo)) {
                    Toast.makeText(getApplicationContext(), "Ce pseudo existe deja !", Toast.LENGTH_SHORT).show();
                } else {
                    Joueur joueur = new Joueur(pseudo);
                    dao.insertJoueur(joueur);
                    Intent intent = new Intent(MainActivity.this, ListingEtapesActivity.class);
                    intent.putExtra("pseudo", pseudo);
                    Toast.makeText(getApplicationContext(), "Joueur ajouté", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void demarrer(View view){

        EditText editText = (EditText) findViewById(R.id.edit_pseudo_content_main);
        String pseudo = editText.getText().toString();
        if(pseudo.isEmpty()){
            Toast.makeText(getApplicationContext(), "Veuillez entrer un nom.", Toast.LENGTH_SHORT).show();
        }
        else if(!dao.getPseudo(pseudo)){
            Toast.makeText(getApplicationContext(), "Ce pseudo n'existe pas", Toast.LENGTH_SHORT).show();
        }

        else{
            Intent intentDemarrer = new Intent(MainActivity.this,ListingEtapesActivity.class);
            intentDemarrer.putExtra("pseudo",pseudo);
            startActivity(intentDemarrer);
        }



    }
}
