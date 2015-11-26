package ipl.android_projet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static ipl.android_projet.model.ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE;
import static ipl.android_projet.model.ModelContract.EpreuveDBEntry.COLUMN_NAME_ETAPE_EPREUVE;
import static ipl.android_projet.model.ModelContract.EpreuveDBEntry.COLUMN_NAME_NUM;
import static ipl.android_projet.model.ModelContract.EpreuveDBEntry.COLUMN_NAME_POINT;
import static ipl.android_projet.model.ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO;

/**
 * Created by Giordano on 18/11/2015.
 */
public class Dao {
    private JoueurDBHelper mJoueursDB;
    private SQLiteDatabase dbJoueur;
    private SQLiteDatabase dbEpreuve;
    private  EpreuveDBHelper mEpreuvesDB;


    public Dao(Context context){

        mJoueursDB = new JoueurDBHelper(context);
        mEpreuvesDB = new EpreuveDBHelper(context);
    }

    public static Epreuve getEpreuveFromCursor(Cursor cursor) {

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        int numero = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_NUM));
        String pseudo = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PSEUDO));
        int etape = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ETAPE_EPREUVE));
        int point = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_POINT));
        long duree = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_DUREE));

        return new Epreuve(numero, pseudo, point, etape, duree);
    }

    public  void open(){
        try{
            dbJoueur = mJoueursDB.getWritableDatabase();
            dbEpreuve = mEpreuvesDB.getWritableDatabase();

        }catch(Exception e){
            dbJoueur = mJoueursDB.getReadableDatabase();
            dbEpreuve = mEpreuvesDB.getReadableDatabase();
        }

    }

    public void close(){
        mJoueursDB.close();
        mEpreuvesDB.close();
    }

    public Cursor getAllPlayers(){

        return dbJoueur.rawQuery("select * from joueurs",null);
    }

    /*
     * @param pseudo : le pseudo d'un joueur
     * @return : vrai si le joueur est déjà présent dans la base de données, faux sinon
     */
    public boolean estPresent(String pseudo) {
        Cursor c = dbJoueur.rawQuery("select pseudo from joueurs where pseudo = '" + pseudo + "'", null);
        return c != null && c.getCount() > 0;

    }

    public int getPoint(String pseudo){
        Cursor c = dbJoueur.rawQuery("select * from joueurs where pseudo = '" + pseudo + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("points"));
    }

    public int getEpreuve(String pseudo){
        Cursor c = dbJoueur.rawQuery("select * from joueurs where pseudo = '" + pseudo + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("epreuve"));
    }

    public int getEtape(String pseudo){
        Cursor c = dbJoueur.rawQuery("select * from joueurs where pseudo = '" + pseudo + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("etape"));
    }

    public void insertJoueur(Joueur joueur){
        ContentValues valeurs = new ContentValues();
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_PSEUDO,joueur.getPseudo());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_POINT,joueur.getPoint());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,joueur.getEtape());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,joueur.getEpreuve());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_TEMPS_TOTAL,joueur.getTempsTotal());
        dbJoueur.insert(ModelContract.JoueurDBEntry.TABLE_NAME, null, valeurs);

    }

    public void updateJoueur(String pseudo, int point,int etape,int epreuve){
        ContentValues valeurs = new ContentValues();

        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_POINT,point);
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,etape);
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,epreuve);

        dbJoueur.update(ModelContract.JoueurDBEntry.TABLE_NAME, valeurs, ModelContract.JoueurDBEntry.COLUMN_NAME_PSEUDO + "='" + pseudo + "'", null);
    }

    public void updateJoueur(String pseudo,long tempsTotal){
        ContentValues valeurs = new ContentValues();

        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_TEMPS_TOTAL, tempsTotal);

        dbJoueur.update(ModelContract.JoueurDBEntry.TABLE_NAME, valeurs, ModelContract.JoueurDBEntry.COLUMN_NAME_PSEUDO + "='" + pseudo + "'", null);
    }

    /*DB Epreuves*/

    public long getTempsTotal(String pseudo){
        Cursor c = dbJoueur.rawQuery("select * from joueurs where pseudo like '" + pseudo + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getLong(c.getColumnIndex("tempsTotal"));
    }

    public void insertEpreuve(Epreuve epreuve){
        ContentValues valeurs = new ContentValues();
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_NUM,epreuve.getNumero());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO,epreuve.getPseudo());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_POINT,epreuve.getPoint());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_ETAPE_EPREUVE,epreuve.getEtapeEpreuve());
        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE,epreuve.getDuree());
        dbEpreuve.insert(ModelContract.EpreuveDBEntry.TABLE_NAME, null, valeurs);
    }

    public long getDuree(String pseudo){
        Cursor c = dbEpreuve.rawQuery("select * from epreuves where pseudo like '" + pseudo + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getLong(c.getColumnIndex("duree"));
    }

    public void updateEpreuve(String pseudo,long duree){
        ContentValues valeurs = new ContentValues();

        valeurs.put(ModelContract.EpreuveDBEntry.COLUMN_NAME_DUREE,duree);

        dbEpreuve.update(ModelContract.EpreuveDBEntry.TABLE_NAME, valeurs, ModelContract.EpreuveDBEntry.COLUMN_NAME_PSEUDO + "='" + pseudo + "'", null);
    }

    public Cursor getAllEpreuves(String pseudo){
        return dbEpreuve.rawQuery("select * from epreuves where pseudo like '" + pseudo + "'", null);
    }

    public int containsEpreuveDBEpreuve(String pseudo, int etape, int epreuve){
        Cursor c = dbEpreuve.rawQuery("select * from epreuves where pseudo like '" + pseudo + "'" + " AND etape = " + etape + " AND numero = " + epreuve + "", null);
        return c.getCount();
    }













}
