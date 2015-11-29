package ipl.android_projet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ipl.android_projet.domaine.Joueur;


/**
 * Hunter Game : a treasure hunt app
 * Copyright (C) 2015 AGNELLO Giordano, NGUYEN Quoc Dat
 *  This file is part of Hunter Game.
 * Hunter Game is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses.
 */
public class JoueurDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Joueurs.db";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ModelContract.JoueurDBEntry.TABLE_NAME;
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ModelContract.JoueurDBEntry.TABLE_NAME  + " (" +
                    ModelContract.JoueurDBEntry._ID                       + " INTEGER PRIMARY KEY," +
                    ModelContract.JoueurDBEntry.COLUMN_NAME_PSEUDO           + " TEXT," +
                    ModelContract.JoueurDBEntry.COLUMN_NAME_POINT          + " INT," +
                    ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS    + " INT," +
                    ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS       + " INT," +
                    ModelContract.JoueurDBEntry.COLUMN_NAME_TEMPS_TOTAL      + " LONG" +
                    " )";
    private final Context myContext;

    public JoueurDBHelper(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        peuplement(db);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insertJoueur(Joueur joueur, SQLiteDatabase db){
        ContentValues valeurs = new ContentValues();
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_PSEUDO,joueur.getPseudo());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_POINT,joueur.getPoint());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,joueur.getEtape());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,joueur.getEpreuve());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_TEMPS_TOTAL,joueur.getTempsTotal());

        db.insert(ModelContract.JoueurDBEntry.TABLE_NAME,null,valeurs);
    }

    private void peuplement(SQLiteDatabase db){
        insertJoueur(new Joueur("dat",2000,20),db);
        insertJoueur(new Joueur("gio",2200,15),db);
    }

}
