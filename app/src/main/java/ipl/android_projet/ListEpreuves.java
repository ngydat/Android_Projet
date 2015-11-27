package ipl.android_projet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ipl.android_projet.dao.Dao;

public class ListEpreuves extends AppCompatActivity {

    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_epreuves);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_list_epreuves);

        Intent intent = getIntent();
        String pseudo = intent.getStringExtra("pseudo");
        dao = new Dao(getApplicationContext());
        dao.open();

        ListView epreuvesListView = (ListView) findViewById(R.id.epreuves_listView);

        Cursor allEpreuves = dao.getAllEpreuves(pseudo);
        EpreuveCursorAdapter epreuvesAdapter = new EpreuveCursorAdapter(getApplicationContext(),allEpreuves,false);

        epreuvesListView.setAdapter(epreuvesAdapter);

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
        return super.onOptionsItemSelected(item);
    }


}
