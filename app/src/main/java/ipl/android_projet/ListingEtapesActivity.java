package ipl.android_projet;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import ipl.android_projet.dao.Dao;
import ipl.android_projet.domaine.Epreuve;

/**
 * Hunter Game : a treasure hunt app
 * Copyright (C) 2015 AGNELLO Giordano, NGUYEN Quoc Dat
 * This file is part of Hunter Game.
 * Hunter Game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses.
 */

public class ListingEtapesActivity extends AppCompatActivity {

    Dao dao;
    String ACTION_FILTER = "com.example.proximity";
    /*Pour le timer*/
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Document doc;
    private String pseudo = "";
    private String urlEtape;
    private WebView webView;
    /*Pour le gps*/
    private LocationManager objgps;
    private LocationListener objlistener;
    private PendingIntent pi;
    /*Pour la progress bar*/
    private ProgressBar progress;
    private ClipDrawable progressImg;
    private float[] roundedCorners;
    private ShapeDrawable pgDrawable;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();

    // source : http://examples.javacodegeeks.com/android/core/os/handler/android-timer-example/
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new Dao(this);
        dao.open();

        // pour le parsing, source : http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser
        doc = this.parseAsset("Florence.xml");
        doc.getDocumentElement().normalize();

        final Intent intent = getIntent();
        pseudo = intent.getStringExtra("pseudo");

        setContentView(R.layout.activity_listing_etapes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        timerValue = (TextView) findViewById(R.id.timerValue);
        startTime = SystemClock.uptimeMillis();
        timeSwapBuff = dao.getTempsTotal(pseudo);
        customHandler.postDelayed(updateTimerThread, 0);


        urlEtape = "file:///android_asset/html/EtapeEnAttente.html";
        webView = (WebView) findViewById(R.id.webView_content_listing);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.loadUrl(urlEtape);

        //source : http://stackoverflow.com/questions/3250034/android-webview-intercept-clicks
        WebViewClient yourWebClient = new WebViewClient() {
            // you tell the webclient you want to catch when a url is about to load

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String question;
                String aide;
                Element epreuve;
                int point;
                if (url.contains("http://epreuve1_etape1.qcm")) {

                    epreuve = ListingEtapesActivity.this.getEpreuve(doc, 0, 0);

                    if (dao.getEtape(pseudo) == 1 && dao.getEpreuve(pseudo) == 1) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intentQCM = new Intent(ListingEtapesActivity.this, EpreuveQCMActivity.class);

                        intentQCM.putExtra("pseudo", pseudo);
                        intentQCM.putExtra("epreuve", 1);
                        intentQCM.putExtra("etape", 1);


                        question = epreuve.getFirstChild().getTextContent();
                        intentQCM.putExtra("question", question);

                        aide = epreuve.getLastChild().getTextContent();
                        intentQCM.putExtra("aide", aide);

                        point = Integer.parseInt(epreuve.getAttribute("points"));
                        intentQCM.putExtra("point", point);

                        String[] reponses = new String[2];
                        String bonneRep = "";
                        for (int i = 1; i < 4; i++) {
                            Element elem = (Element) epreuve.getChildNodes().item(i);
                            if (elem.getAttribute("bonne").equals("true")) {
                                bonneRep = epreuve.getChildNodes().item(i).getTextContent();
                            } else {
                                reponses[i - 1] = epreuve.getChildNodes().item(i).getTextContent();
                            }

                        }

                        intentQCM.putExtra("bonneRep", bonneRep);
                        intentQCM.putExtra("reponses", reponses);

                        startActivity(intentQCM);
                    }

                } else if (url.contains("http://epreuve2_etape1.photo")) {
                    double latitudeEpreuve = 0;
                    double longitudeEpreuve = 0;
                    float rayonEpreuve = 0;

                    if (dao.getEtape(pseudo) == 1 && dao.getEpreuve(pseudo) < 1) {
                        Toast.makeText(getApplicationContext(), "Veuillez terminer l'epreuve 1", Toast.LENGTH_SHORT).show();
                    } else if (dao.getEtape(pseudo) == 1 && dao.getEpreuve(pseudo) == 2) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    } else {

                        epreuve = ListingEtapesActivity.this.getEpreuve(doc, 0, 1);
                        latitudeEpreuve = Double.valueOf(epreuve.getElementsByTagName("Zone").item(0).getFirstChild().getTextContent());
                        longitudeEpreuve = Double.valueOf(epreuve.getElementsByTagName("Zone").item(0).getFirstChild().getNextSibling().getTextContent());
                        rayonEpreuve = Float.valueOf(epreuve.getElementsByTagName("Zone").item(0).getLastChild().getTextContent());


                        question = epreuve.getFirstChild().getTextContent();
                        point = Integer.parseInt(epreuve.getAttribute("points"));
                        aide = epreuve.getLastChild().getTextContent();
                        Intent intentPhoto = null;
                        //http://stackoverflow.com/questions/3551959/possible-to-only-load-specific-lines-of-code-according-to-android-os-version
                        if (Build.VERSION.RELEASE.compareTo("5.0") >= 0) {
                            intentPhoto = new Intent(ListingEtapesActivity.this, EpreuvePhotoActivity_LOLLIPOP.class);
                        } else {
                            intentPhoto = new Intent(ListingEtapesActivity.this, EpreuvePhotoActivity.class);
                        }
                        intentPhoto.putExtra("question", question);
                        intentPhoto.putExtra("pseudo", pseudo);
                        intentPhoto.putExtra("epreuve", 2);
                        intentPhoto.putExtra("etape", 1);
                        intentPhoto.putExtra("point", point);
                        intentPhoto.putExtra("aide", aide);
                        intentPhoto.putExtra("latitudeEpreuve", latitudeEpreuve);
                        intentPhoto.putExtra("longitudeEpreuve", longitudeEpreuve);
                        intentPhoto.putExtra("rayonEpreuve", rayonEpreuve);
                        startActivity(intentPhoto);
                    }
                } else if (url.contains("http://epreuve1_etape2.texte_trou")) {
                    if (dao.getEtape(pseudo) == 1) {
                        Toast.makeText(getApplicationContext(), "Veuillez terminer l'etape 1", Toast.LENGTH_SHORT).show();
                    } else if (dao.getEtape(pseudo) == 2 && dao.getEpreuve(pseudo) == 3) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    } else {
                        epreuve = ListingEtapesActivity.this.getEpreuve(doc, 1, 0);
                        question = epreuve.getFirstChild().getTextContent();
                        point = Integer.parseInt(epreuve.getAttribute("points"));
                        aide = epreuve.getLastChild().getTextContent();


                        String[] reponses = new String[2];
                        for (int i = 1; i < 3; i++) {
                            reponses[i - 1] = epreuve.getChildNodes().item(i).getTextContent();
                        }

                        Intent intentTrou = new Intent(ListingEtapesActivity.this, EpreuveTexteATrousActivity.class);
                        intentTrou.putExtra("question", question);
                        intentTrou.putExtra("pseudo", pseudo);
                        intentTrou.putExtra("epreuve", 3);
                        intentTrou.putExtra("etape", 2);
                        intentTrou.putExtra("point", point);
                        intentTrou.putExtra("reponses", reponses);
                        intentTrou.putExtra("aide", aide);


                        startActivity(intentTrou);
                    }
                }
                else if(url.contains("http://epreuve2_etape2.qcm")){
                    if (dao.getEtape(pseudo) == 2 && dao.getEpreuve(pseudo) < 1) {
                        Toast.makeText(getApplicationContext(), "Veuillez terminer l'epreuve 1", Toast.LENGTH_SHORT).show();
                    } else if (dao.getEtape(pseudo) == 2 && dao.getEpreuve(pseudo) == 4) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    } else {
                        epreuve = ListingEtapesActivity.this.getEpreuve(doc, 1, 1);

                        Intent intentQCM = new Intent(ListingEtapesActivity.this, EpreuveQCMActivity.class);

                        intentQCM.putExtra("pseudo", pseudo);
                        intentQCM.putExtra("epreuve", 4);
                        intentQCM.putExtra("etape", 2);


                        question = epreuve.getFirstChild().getTextContent();
                        intentQCM.putExtra("question", question);

                        aide = epreuve.getLastChild().getTextContent();
                        intentQCM.putExtra("aide", aide);

                        point = Integer.parseInt(epreuve.getAttribute("points"));
                        intentQCM.putExtra("point", point);

                        String[] reponses = new String[2];
                        String bonneRep = "";
                        for (int i = 1; i < 4; i++) {
                            Element elem = (Element) epreuve.getChildNodes().item(i);
                            if (elem.getAttribute("bonne").equals("true")) {
                                bonneRep = epreuve.getChildNodes().item(i).getTextContent();
                            } else {
                                reponses[i - 1] = epreuve.getChildNodes().item(i).getTextContent();
                            }

                        }

                        intentQCM.putExtra("bonneRep", bonneRep);
                        intentQCM.putExtra("reponses", reponses);

                        startActivity(intentQCM);
                    }

                }



                return true;
            }

            // here you execute an action when the URL you want is about to load
            @Override
            public void onLoadResource(WebView view, String url) {
            }


        };

        webView.setWebViewClient(yourWebClient);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int point = dao.getPoint(pseudo);
        int derniereEtape = dao.getEtape(pseudo);
        int derniereEpreuve = dao.getEpreuve(pseudo);
        long tempsTotal = dao.getTempsTotal(pseudo);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListingEtapesActivity.this);
        switch (item.getItemId()) {
            case R.id.action_point:

                Dialog stat = new Dialog(this);
                stat.setContentView(R.layout.layout_dialog_stat);
                stat.setCancelable(true);
                stat.setTitle("Vos statistiques");


                TextView tvPoint = (TextView) stat.findViewById(R.id.pointJoueurTextView);
                TextView tvEtape = (TextView) stat.findViewById(R.id.derniereEtapeTextView);
                TextView tvEpreuve = (TextView) stat.findViewById(R.id.derniereEpreuveTextView);
                TextView tvTempsTotal = (TextView) stat.findViewById(R.id.tempsTotalTextView);

                tvPoint.setText(""+point);
                tvEtape.setText(""+derniereEtape);
                tvEpreuve.setText(""+derniereEpreuve);

                int secs = (int) (tempsTotal / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                tvTempsTotal.setText("" + mins + ":"
                        + String.format("%02d", secs));


                // source : http://easy-android.over-blog.com/2013/12/changer-la-couleur-d-une-barre-de-progression.html
                roundedCorners = new float[] { 10, 10, 10, 10, 10, 10, 10, 10 };
                pgDrawable = new ShapeDrawable (new RoundRectShape(roundedCorners, null, null));

                pgDrawable.getPaint ().setColor (Color.rgb(102,0,102));
                progressImg = new ClipDrawable (pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                progress=(ProgressBar) stat.findViewById(R.id.progressbar);
                progress.setProgressDrawable(progressImg);
                progress.setMax(100);
                progress.setBackgroundColor(Color.rgb(202, 203, 182));

                double progression = ((double)dao.getEpreuve(pseudo)/getNbEpreuveTotal(doc))*100;
                progress.setProgress ((int)progression);


                stat.show();


                return true;

            case R.id.action_epreuves:

                Intent intent = new Intent(ListingEtapesActivity.this, ListEpreuves.class);
                intent.putExtra("pseudo", pseudo);
                startActivity(intent);
                return true;


            case R.id.action_map:
                Intent intentMap = new Intent(ListingEtapesActivity.this, PlanActivity.class);
                startActivity(intentMap);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onResume() {

        super.onResume();


        Intent intent = getIntent();
        String epreuveOK_KO = intent.getStringExtra("epreuveOK_KO");
        int etapeCourante = intent.getIntExtra("etape", 1);
        int epreuveCourante = intent.getIntExtra("epreuve", 0);
        int point = intent.getIntExtra("point", 0);
        int pointActuel = dao.getPoint(pseudo);
        long duree = intent.getLongExtra("duree", 0);


        if (epreuveOK_KO != null) {
            if (dao.containsEpreuveDBEpreuve(pseudo, etapeCourante, epreuveCourante) == 0) {
                dao.updateJoueur(pseudo, pointActuel + point, etapeCourante, epreuveCourante);
                dao.insertEpreuve(new Epreuve(epreuveCourante, pseudo, point, etapeCourante, duree));
            }
        }

        if (getNbEpreuve(doc, (etapeCourante - 1)) == epreuveCourante) {
            Toast.makeText(getApplicationContext(), "Bravo vous venez de finir l'etape n° " + etapeCourante, Toast.LENGTH_LONG).show();
            dao.updateJoueur(pseudo, (pointActuel + point), (etapeCourante + 1), epreuveCourante);
        }

        if(dao.getEpreuve(pseudo)==getNbEpreuveTotal(doc)){
            Toast.makeText(getApplicationContext(), "Bravo vous avez fini !!", Toast.LENGTH_LONG).show();
            Intent intentFin = new Intent(ListingEtapesActivity.this,FinActivity.class);
            intentFin.putExtra("pseudo",pseudo);

            intentFin.putExtra("tempsTotal",dao.getTempsTotal(pseudo));

            startActivity(intentFin);

        }


        /*****************************************************************/
        registerReceiver(new ProximityReceiver(), new IntentFilter(ACTION_FILTER));

        // souce : http://houssem-lahiani.blogspot.be/2015/04/la-geolocalisation-et-les-alerte-de.html
        objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        objlistener = new Myobjlistener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        objgps.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                objlistener);

        double latitudeEtape = 0;
        double longitudeEtape = 0;
        float rayonEtape = 0;


        if (dao.getEtape(pseudo) == 1) {
            latitudeEtape = Double.parseDouble(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(0).getTextContent());
            longitudeEtape = Double.parseDouble(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(1).getTextContent());
            rayonEtape = Float.parseFloat(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(2).getTextContent());
        } else {
            latitudeEtape = Double.parseDouble(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(0).getTextContent());
            longitudeEtape = Double.parseDouble(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(1).getTextContent());
            rayonEtape = Float.parseFloat(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(2).getTextContent());
        }
        Intent intentEtape = new Intent(ACTION_FILTER);
        pi = PendingIntent.getBroadcast(getApplicationContext(), -1, intentEtape, 0);
        objgps.addProximityAlert(latitudeEtape, longitudeEtape, rayonEtape, -1, pi);

    }

    public Document parseAsset(String fileName) {
        Context context = getApplicationContext();
        InputStream in = null;
        Document doc = null;
        try {
            in = context.getAssets().open(fileName);
            DocumentBuilderFactory facto = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = facto.newDocumentBuilder();
            doc = builder.parse(in);

            // source : http://stackoverflow.com/questions/4650878/how-to-remove-text-from-my-node-parsing-in-java-dom-xml-parsing
            XPathFactory xpathFactory = XPathFactory.newInstance();
            // XPath to find empty text nodes.
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);
            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public String getUrlEtape(Document doc, int indice) {
        NodeList nList = doc.getElementsByTagName("Etape");
        Node node = nList.item(indice);
        Element e = (Element) node;
        return e.getAttribute("url");
    }

    public Element getEpreuve(Document doc, int indiceEtape, int indiceEpreuve) {
        NodeList nList = doc.getElementsByTagName("Etape");
        Node etapeChoisie = nList.item(indiceEtape);
        Element element = (Element) etapeChoisie;
        NodeList epreuves = element.getElementsByTagName("Epreuve");
        return (Element) epreuves.item(indiceEpreuve);
    }

    public int getNbEpreuve(Document doc, int indiceEtape) {
        NodeList nList = doc.getElementsByTagName("Etape");
        Node etapeChoisie = nList.item(indiceEtape);
        Element element = (Element) etapeChoisie;
        NodeList epreuves = element.getElementsByTagName("Epreuve");
        return epreuves.getLength();
    }

    public int getNbEpreuveTotal(Document doc) {
        NodeList nList = doc.getElementsByTagName("Epreuve");
        return nList.getLength();
    }

    public Element getEtape(Document doc, int indiceEtape) {
        NodeList nList = doc.getElementsByTagName("Etape");
        Node node = nList.item(indiceEtape);
        Element e = (Element) node;
        return (Element) node;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(), voice_text, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dao.updateJoueur(pseudo, updatedTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        objgps.removeProximityAlert(pi);
    }

    @Override
    protected void onPause() {

        super.onPause();
        dao.updateJoueur(pseudo, updatedTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    private class Myobjlistener implements LocationListener
    {

        TextView mtextView = (TextView) findViewById(R.id.gpsText);

        public void onProviderDisabled(String provider) {

            mtextView.setVisibility(View.VISIBLE);
        }

        //Pour la visibilité, http://stackoverflow.com/questions/5335787/making-buttons-appear-and-disappear-on-an-image-view

        public void onProviderEnabled(String provider) {

            mtextView.setVisibility(View.GONE);
        }


        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // TODO Auto-generated method stub
        }


        public void onLocationChanged(Location location) {
        }

    }

    public class ProximityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            // The reciever gets the Context & the Intent that fired the broadcast as arg0 & agr1
            String k = LocationManager.KEY_PROXIMITY_ENTERING;
            // Key for determining whether user is leaving or entering
            boolean state = arg1.getBooleanExtra(k, false);
            //Gives whether the user is entering or leaving in boolean form
            int etapeEnCours = dao.getEtape(pseudo);
            urlEtape = ListingEtapesActivity.this.getUrlEtape(doc, (etapeEnCours - 1));
            if (state) {
                webView.loadUrl(urlEtape);
            } else {
                webView.loadUrl("file:///android_asset/html/EtapeEnAttente.html");
            }

        }

    }


}



