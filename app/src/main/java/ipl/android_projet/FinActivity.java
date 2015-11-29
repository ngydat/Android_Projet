package ipl.android_projet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import ipl.android_projet.dao.Dao;

public class FinActivity extends AppCompatActivity {

    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);
        Intent intent = getIntent();
        String pseudo = intent.getStringExtra("pseudo");
        long tempsTotal = intent.getLongExtra("tempsTotal", 0);
        int secs = (int) (tempsTotal / 1000);
        int mins = secs / 60;
        secs = secs % 60;

        TextView tv = (TextView) findViewById(R.id.tv_fin);
        tv.setText("Bravo " + pseudo + " vous avez fini le jeu en "+ mins + ":"
                + String.format("%02d", secs));

        dao = new Dao(getApplicationContext());
        dao.open();

        ListView joueursListView = (ListView) findViewById(R.id.classement_listView);

        Cursor allJoueurs= dao.getAllPlayers();
        JoueurCursorAdapter joueursAdapter = new JoueurCursorAdapter(getApplicationContext(),allJoueurs,false);
        joueursListView.setAdapter(joueursAdapter);

    }

}
