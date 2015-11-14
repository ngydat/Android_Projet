package ipl.android_projet.Model;

import java.net.URI;

/**
 * Created by Giordano on 14/11/2015.
 */
public class Epreuve {


    private int point;
    private String type;
    private URI uri;
    private boolean termine;

    public Epreuve(int point, String type,URI uri,boolean termine){
        this.point = point;
        this.type = type;
        this.uri = uri;
        this.termine=termine;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public boolean isTermine() {
        return termine;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }
}
