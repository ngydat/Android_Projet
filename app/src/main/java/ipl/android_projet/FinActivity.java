package ipl.android_projet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

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
