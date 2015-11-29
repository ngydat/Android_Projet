package ipl.android_projet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import ipl.android_projet.dao.Dao;

/**
 * Hunter Game : a treasure hunt app
 * Copyright (C) 2015 AGNELLO Giordano, NGUYEN Quoc Dat
 * This file is part of Hunter Game.
 * Hunter Game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses.
 */

public class ListEpreuves extends AppCompatActivity {

    Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_epreuves);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Liste des epreuves");
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();
        String pseudo = intent.getStringExtra("pseudo");
        dao = new Dao(getApplicationContext());
        dao.open();

        ListView epreuvesListView = (ListView) findViewById(R.id.epreuves_listView);

        Cursor allEpreuves = dao.getAllEpreuves(pseudo);
        if(allEpreuves.getCount()==0){
            Toast.makeText(getApplicationContext(), "Aucune epreuve realisées !", Toast.LENGTH_SHORT).show();
        }else{
            EpreuveCursorAdapter epreuvesAdapter = new EpreuveCursorAdapter(getApplicationContext(),allEpreuves,false);

            epreuvesListView.setAdapter(epreuvesAdapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }


}
