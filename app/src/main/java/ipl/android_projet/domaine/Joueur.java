package ipl.android_projet.domaine;

import java.io.Serializable;

/**
 * Created by Giordano on 18/11/2015.
 */
public class Joueur implements Serializable {

    private String pseudo;
    private int point;
    private int etape;
    private int epreuve;
    private long tempsTotal;



    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.point = 0;
        this.etape = 1;
        this.epreuve = 0;
        this.tempsTotal=0;

    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getEtape() {
        return etape;
    }

    public void setEtape(int etape) {
        this.etape = etape;
    }

    public int getEpreuve() {
        return epreuve;
    }

    public void setEpreuve(int epreuve) {
        this.epreuve = epreuve;
    }

    public long getTempsTotal() {
        return tempsTotal;
    }

    public void setTempsTotal(long tempsTotal) {
        this.tempsTotal = tempsTotal;
    }
}
