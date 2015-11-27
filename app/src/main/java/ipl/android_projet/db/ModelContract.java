package ipl.android_projet.db;

import android.provider.BaseColumns;

/**
 * Created by Giordano on 18/11/2015.
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
        public static final String COLUMN_NAME_POINT = "point";
        public static final String COLUMN_NAME_ETAPE_EPREUVE = "etape";
        public static final String COLUMN_NAME_DUREE = "duree";
    }


}
