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
        TextView tvEtape = (TextView) convertView.findViewById(R.id.item_etape);
        TextView tvDuree = (TextView) convertView.findViewById(R.id.item_duree);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.item_point);

        tvNum.setText("Numero epreuve : "+epreuve.getNumero());
        tvEtape.setText("Numero etape : "+epreuve.getEtapeEpreuve());
        int secs = (int) (epreuve.getDuree() / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        tvDuree.setText("Duree : " + mins + ":"
                + String.format("%02d", secs));
        tvPoint.setText("Point : "+epreuve.getPoint());
        return convertView;
    }
}
