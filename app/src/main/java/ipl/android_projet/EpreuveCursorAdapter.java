package ipl.android_projet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ipl.android_projet.model.Dao;
import ipl.android_projet.model.Epreuve;

/**
 * Created by Giordano on 25/11/2015.
 */
public class EpreuveCursorAdapter extends CursorAdapter {
    public EpreuveCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }




    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.list_item_epreuves,parent,false);

        return constructView(context,convertView,cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        constructView(context,view,cursor);
    }


    public View constructView(Context context,View convertView,Cursor cursor){
        Epreuve epreuve = Dao.getEpreuveFromCursor(cursor);

        TextView tvNum = (TextView) convertView.findViewById(R.id.item_numero_epreuve);
        TextView tvPseudo = (TextView) convertView.findViewById(R.id.item_speudo);
        TextView tvEtape = (TextView) convertView.findViewById(R.id.item_etape);
        TextView tvDuree = (TextView) convertView.findViewById(R.id.item_duree);

        tvNum.setText("Numero epreuve : "+epreuve.getNumero());
        tvPseudo.setText("Nom : "+epreuve.getPseudo());
        tvEtape.setText("Numero etape : "+epreuve.getEtapeEpreuve());
        int secs = (int) (epreuve.getDuree() / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        tvDuree.setText("" + mins + ":"
                + String.format("%02d", secs));

        return convertView;
    }
}
