package ipl.android_projet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ipl.android_projet.domaine.Joueur;


/**
 * Created by Giordano on 18/11/2015.
 */
public class JoueurDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 3;
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
        insertJoueur(new Joueur("dat"),db);
        insertJoueur(new Joueur("gio"),db);
    }

}
