package ipl.android_projet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Giordano on 18/11/2015.
 */
public class Dao {
    private JoueurDBHelper mJoueursDB;
    private SQLiteDatabase db;


    public Dao(Context context){

        mJoueursDB = new JoueurDBHelper(context);
    }

    public  void open(){
        try{
            db = mJoueursDB.getWritableDatabase();
        }catch(Exception e){
            db = mJoueursDB.getReadableDatabase();
        }

    }


    public void close(){
        mJoueursDB.close();
    }


    public Cursor getAllPlayers(){
        return db.rawQuery("select * from joueurs",null);
    }

    public boolean getPrenom(String prenom){
        Cursor c = db.rawQuery("select prenom from joueurs where prenom like '" + prenom + "'", null);
        if(c!=null && c.getCount()>0){
            return true;
        }
        return false;
    }

    public int getPoint(String prenom){
        Cursor c = db.rawQuery("select * from joueurs where prenom like '" + prenom + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("points"));
    }


    public int getEpreuve(String prenom){
        Cursor c = db.rawQuery("select * from joueurs where prenom like '" + prenom + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("epreuve"));
    }

    public int getEtape(String prenom){
        Cursor c = db.rawQuery("select * from joueurs where prenom like '" + prenom + "'", null);

        c.moveToFirst(); // On se positionne sur le premier

        return c.getInt(c.getColumnIndex("etape"));
    }


    public void insertJoueur(Joueur joueur){
        ContentValues valeurs = new ContentValues();
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_PRENOM,joueur.getPrenom());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_POINT,joueur.getPoint());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,joueur.getEtape());
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,joueur.getEpreuve());
        db.insert(ModelContract.JoueurDBEntry.TABLE_NAME, null, valeurs);

    }

    public void updateJoueur(String prenom, int point,int etape,int epreuve){
        ContentValues valeurs = new ContentValues();

        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_POINT,point);
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_ETAPE_EN_COURS,etape);
        valeurs.put(ModelContract.JoueurDBEntry.COLUMN_NAME_EPREUVE_EN_COURS,epreuve);

        db.update(ModelContract.JoueurDBEntry.TABLE_NAME,valeurs, ModelContract.JoueurDBEntry.COLUMN_NAME_PRENOM + "='"+prenom+"'",null);
    }







}
