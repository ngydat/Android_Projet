package ipl.android_projet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ipl.android_projet.dao.Dao;
import ipl.android_projet.domaine.Epreuve;
import ipl.android_projet.domaine.Joueur;

/**
 * Created by Giordano on 28/11/2015.
 */
public class JoueurCursorAdapter extends CursorAdapter{


    public JoueurCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.classement_item,parent,false);

        return constructView(context,convertView,cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        constructView(context,view,cursor);
    }

    public View constructView(Context context,View convertView,Cursor cursor){
        Joueur joueur = Dao.getJoueurFromCursor(cursor);

        TextView tvPseudo = (TextView) convertView.findViewById(R.id.item_class_pseudo);
        TextView tvTempsTotal = (TextView) convertView.findViewById(R.id.item_class_tempsTotal);

        tvPseudo.setText("Pseudo : "+joueur.getPseudo());
        int secs = (int) (joueur.getTempsTotal() / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        tvTempsTotal.setText("Duree : " + mins + ":"
                + String.format("%02d", secs));
        return convertView;
    }



}
