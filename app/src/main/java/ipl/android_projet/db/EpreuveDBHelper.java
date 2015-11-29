package ipl.android_projet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ipl.android_projet.domaine.Epreuve;

/**
 * Created by Giordano on 24/11/2015.
 */
public class EpreuveDBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 10;
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
