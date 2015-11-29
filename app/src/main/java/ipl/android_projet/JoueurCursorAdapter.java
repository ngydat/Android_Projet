package ipl.android_projet;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ipl.android_projet.dao.Dao;
import ipl.android_projet.domaine.Joueur;

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
        TextView tvPoint = (TextView) convertView.findViewById(R.id.item_class_point);

        tvPseudo.setText("Pseudo : "+joueur.getPseudo());
        int secs = (int) (joueur.getTempsTotal() / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        tvTempsTotal.setText("Duree : " + mins + ":"
                + String.format("%02d", secs));
        tvPoint.setText("Point : " + joueur.getPoint());
        return convertView;
    }



}
