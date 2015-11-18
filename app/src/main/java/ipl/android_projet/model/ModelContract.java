package ipl.android_projet.model;

import android.provider.BaseColumns;

/**
 * Created by Giordano on 18/11/2015.
 */
public final class ModelContract {

    public ModelContract(){};

    public static abstract class JoueurDBEntry implements BaseColumns{
        public static final String TABLE_NAME="joueurs";
        public static final String COLUMN_NAME_PRENOM = "prenom";
        public static final String COLUMN_NAME_POINT = "points";
        public static final String COLUMN_NAME_ETAPE_EN_COURS = "etape";
        public static final String COLUMN_NAME_EPREUVE_EN_COURS = "epreuve";

    }


}
