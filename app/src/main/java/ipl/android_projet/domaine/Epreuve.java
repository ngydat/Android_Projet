package ipl.android_projet.domaine;

/**
 * Created by Giordano on 24/11/2015.
 */
public class Epreuve {

    private int numero;



    private String pseudo;
    private int point;
    private int etape;
    private long duree;


    public Epreuve( int numero,String pseudo, int point, int etape) {
        this.numero=numero;
        this.duree = 0;
        this.pseudo = pseudo;
        this.point = point;
        this.etape = etape;
    }
    public Epreuve( int numero,String pseudo, int point, int etape,long duree) {
        this.numero=numero;
        this.duree = duree;
        this.pseudo = pseudo;
        this.point = point;
        this.etape = etape;
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

    public int getEtapeEpreuve() {
        return etape;
    }

    public void setEtapeEpreuve(int etape) {
        this.etape = etape;
    }

    public long getDuree() {
        return duree;
    }

    public void setDuree(long duree) {
        this.duree = duree;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
