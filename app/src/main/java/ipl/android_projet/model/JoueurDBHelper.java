package ipl.android_projet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static ipl.android_projet.model.ModelContract.*;


/**
 * Created by Giordano on 18/11/2015.
 */
public class JoueurDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 34;
    public static final String DATABASE_NAME = "Joueurs.db";
    private final Context myContext;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + JoueurDBEntry.TABLE_NAME  + " (" +
                    JoueurDBEntry._ID                       + " INTEGER PRIMARY KEY," +
                    JoueurDBEntry.COLUMN_NAME_PRENOM           + " TEXT," +
                    JoueurDBEntry.COLUMN_NAME_POINT          + " INT," +
                    JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS    + " INT," +
                    JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS       + " INT" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + JoueurDBEntry.TABLE_NAME;

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
        valeurs.put(JoueurDBEntry.COLUMN_NAME_PRENOM,joueur.getPrenom());
        valeurs.put(JoueurDBEntry.COLUMN_NAME_POINT,joueur.getPoint());
        valeurs.put(JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,joueur.getEtape());
        valeurs.put(JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,joueur.getEpreuve());

        db.insert(JoueurDBEntry.TABLE_NAME,null,valeurs);
    }

    private void peuplement(SQLiteDatabase db){
        insertJoueur(new Joueur("dat"),db);
        insertJoueur(new Joueur("gio"),db);
    }

}
