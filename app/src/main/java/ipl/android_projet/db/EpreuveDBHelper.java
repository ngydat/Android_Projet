package ipl.android_projet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
public class EpreuveDBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Epreuves.db";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ModelContract.EpreuveDBEntry.TABLE_NAME;
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ModelContract.EpreuveDBEntry.TABLE_NAME  + " (" +
                    ModelContract.EpreuveDBEntry._ID                       + " INTEGER PRIMARY KEY," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_NUM          + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_POINT          + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_ETAPE_EPREUVE    + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE        + " LONG," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO           + " TEXT, FOREIGN KEY("+ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO+") REFERENCES joueurs(pseudo)" +
                    " )";
    private final Context myContext;

    public EpreuveDBHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
