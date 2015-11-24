package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ipl.android_projet.model.Dao;
import ipl.android_projet.model.Joueur;

public class AddUserActivity extends AppCompatActivity {

    String pseudo;
    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dao = new Dao(this);
        dao.open();


    }

    public void addUser(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.yourName);
        pseudo = usernameEditText.getText().toString();
        Joueur joueur = new Joueur(pseudo);
        dao.insertJoueur(joueur);
        Intent intent = new Intent(AddUserActivity.this, ListingEpreuvesActivity.class);
        intent.putExtra("pseudo", pseudo);
        Toast.makeText(getApplicationContext(), "Joueur ajouté", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

}
