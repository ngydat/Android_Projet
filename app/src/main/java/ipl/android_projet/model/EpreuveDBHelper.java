package ipl.android_projet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Giordano on 24/11/2015.
 */
public class EpreuveDBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 60;
    public static final String DATABASE_NAME = "Epreuves.db";
    private final Context myContext;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ModelContract.EpreuveDBEntry.TABLE_NAME  + " (" +
                    ModelContract.EpreuveDBEntry._ID                       + " INTEGER PRIMARY KEY," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_NUM          + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_POINT          + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_ETAPE_EPREUVE    + " INT," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE        + " LONG," +
                    ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO           + " TEXT, FOREIGN KEY("+ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO+") REFERENCES joueurs(pseudo)" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ModelContract.EpreuveDBEntry.TABLE_NAME;

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

    public void insertEpreuve(Epreuve epreuve, SQLiteDatabase db){
        ContentValues valeurs = new ContentValues();
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO,epreuve.getPseudo());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_POINT,epreuve.getPoint());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_NUM,epreuve.getNumero());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_ETAPE_EPREUVE,epreuve.getEtapeEpreuve());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE,epreuve.getDuree());


        db.insert(ModelContract.JoueurDBEntry.TABLE_NAME, null, valeurs);
    }

}
