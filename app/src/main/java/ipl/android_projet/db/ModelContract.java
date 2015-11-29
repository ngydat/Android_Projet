package ipl.android_projet.db;

import android.provider.BaseColumns;

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
public final class ModelContract {

    public ModelContract(){};

    public static abstract class JoueurDBEntry implements BaseColumns{
        public static final String TABLE_NAME="joueurs";
        public static final String COLUMN_NAME_PSEUDO = "pseudo";
        public static final String COLUMN_NAME_POINT = "points";
        public static final String COLUMN_NAME_ETAPE_EN_COURS = "etape";
        public static final String COLUMN_NAME_EPREUVE_EN_COURS = "epreuve";
        public static final String COLUMN_NAME_TEMPS_TOTAL = "tempsTotal";
    }

    public static abstract class EpreuveDBEntry implements BaseColumns{
        public static final String TABLE_NAME="epreuves";
        public static final String COLUMN_NAME_PSEUDO = "pseudo";
        public static final String COLUMN_NAME_NUM= "numero";
        public static final String COLUMN_NAME_POINT = "points";
        public static final String COLUMN_NAME_ETAPE_EPREUVE = "etape";
        public static final String COLUMN_NAME_DUREE = "duree";
    }


}
