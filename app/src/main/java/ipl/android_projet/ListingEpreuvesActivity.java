package ipl.android_projet;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import ipl.android_projet.model.Dao;

public class ListingEpreuvesActivity extends AppCompatActivity {

    Dao dao;
    Spinner spinner;
    String ACTION_FILTER = "com.example.proximity";
    private Document doc;
    private String urlEtape;
    private WebView webView;
    private LocationManager objgps;
    private LocationListener objlistener;
    private TextView mTxtViewlong;
    private TextView mTxtViewlat;
    private String prenom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new Dao(this);
        dao.open();

        doc = this.parseAsset("CampusAlma.xml");
        doc.getDocumentElement().normalize();

        final Intent intent = getIntent();
        prenom = intent.getStringExtra("prenom");

        setContentView(R.layout.content_listing_etapes);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        urlEtape = "file:///android_asset/EtapeEnAttente.html";
        webView = (WebView) findViewById(R.id.webView_content_listing);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if(this.getEtape(doc,0).getAttributes().getNamedItem("visible").getTextContent().contains("false")){
            webView.loadUrl(urlEtape);
        }else{
            webView.loadUrl(this.getUrlEtape(doc, 0));
        }


        spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.etapes_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        if (ListingEpreuvesActivity.this.getEtape(doc, position).getAttributes().getNamedItem("visible").getTextContent().contains("true")) {
                            urlEtape = ListingEpreuvesActivity.this.getUrlEtape(doc, position);
                            break;
                        }

                    case 1:
                        if (ListingEpreuvesActivity.this.getEtape(doc, position).getAttributes().getNamedItem("visible").getTextContent().contains("true")) {
                            urlEtape = ListingEpreuvesActivity.this.getUrlEtape(doc, position);
                            break;
                        }
                    default:
                        urlEtape = "file:///android_asset/EtapeEnAttente.html";

                }
                webView.loadUrl(urlEtape);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // somewhere on your code...
        WebViewClient yourWebClient = new WebViewClient() {
            // you tell the webclient you want to catch when a url is about to load

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String question;
                Element epreuve;
                int point;
                if (url.contains("http://epreuve1_etape1.qcm")) {

                    epreuve = ListingEpreuvesActivity.this.getEpreuve(doc, 0, 0);

                    if(dao.getEtape(prenom)>=0 && dao.getEpreuve(prenom)>=0) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intentQCM = new Intent(ListingEpreuvesActivity.this, EpreuveQCMActivity.class);

                        intentQCM.putExtra("prenom", prenom);
                        intentQCM.putExtra("epreuve", 0);
                        intentQCM.putExtra("etape", 0);


                        question = epreuve.getFirstChild().getTextContent();
                        intentQCM.putExtra("question", question);

                        point= Integer.parseInt(epreuve.getAttribute("points"));
                        intentQCM.putExtra("point", point);

                        String [] reponses = new String[2];
                        String bonneRep = "";
                        for(int i = 1 ; i<4;i++){
                            Element elem = (Element) epreuve.getChildNodes().item(i);
                            if (elem.getAttribute("bonne").equals("true")) {
                                bonneRep = epreuve.getChildNodes().item(i).getTextContent();
                            }else{
                                reponses [i-1]= epreuve.getChildNodes().item(i).getTextContent();
                            }

                        }

                        intentQCM.putExtra("bonneRep", bonneRep);
                        intentQCM.putExtra("reponses", reponses);

                        startActivity(intentQCM);
                    }

                }
                else if(url.contains("http://epreuve2_etape1.photo")){
                    if(dao.getEtape(prenom)>=0 && dao.getEpreuve(prenom)<0){
                        Toast.makeText(getApplicationContext(), "Veuillez terminer l'epreuve 1", Toast.LENGTH_SHORT).show();
                    }else if(dao.getEtape(prenom)==0 && dao.getEpreuve(prenom)==1){
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        epreuve = ListingEpreuvesActivity.this.getEpreuve(doc, 0, 1);
                        question = epreuve.getFirstChild().getTextContent();
                        point= Integer.parseInt(epreuve.getAttribute("points"));

                        Intent intentPhoto = new Intent(ListingEpreuvesActivity.this, EpreuvePhotoActivity.class);
                        intentPhoto.putExtra("question", question);
                        intentPhoto.putExtra("prenom", prenom);
                        intentPhoto.putExtra("epreuve", 1);
                        intentPhoto.putExtra("etape", 0);
                        intentPhoto.putExtra("point",point);


                        startActivity(intentPhoto);
                    }
                } else if (url.contains("http://epreuve3_etape1.texte_trou")) {
                    if (dao.getEtape(prenom) >= 0 && dao.getEpreuve(prenom) < 0) {
                        Toast.makeText(getApplicationContext(), "Veuillez terminer l'epreuve 2", Toast.LENGTH_SHORT).show();
                    } else if (dao.getEtape(prenom) == 0 && dao.getEpreuve(prenom) == 1) {
                        Toast.makeText(getApplicationContext(), "Epreuve deja faite !", Toast.LENGTH_SHORT).show();
                    } else {
                        epreuve = ListingEpreuvesActivity.this.getEpreuve(doc, 0, 2);
                        question = epreuve.getFirstChild().getTextContent();
                        point = Integer.parseInt(epreuve.getAttribute("points"));

                        String[] reponses = new String[3];
                        for (int i = 1; i < 4; i++) {
                            Element elem = (Element) epreuve.getChildNodes().item(i);
                            reponses[i - 1] = epreuve.getChildNodes().item(i).getTextContent();

                        }


                        Intent intentTrou = new Intent(ListingEpreuvesActivity.this, TexteATrousActivity.class);
                        intentTrou.putExtra("question", question);
                        intentTrou.putExtra("prenom", prenom);
                        intentTrou.putExtra("epreuve", 2);
                        intentTrou.putExtra("etape", 0);
                        intentTrou.putExtra("point", point);
                        intentTrou.putExtra("reponses", reponses);

                        startActivity(intentTrou);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int point = dao.getPoint(prenom);
        Dialog box = null;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_point:
                box = new Dialog(this);
                //box.setContentView(R.layout.dialog_layout);
                box.setTitle("Vos point : " + point);
                box.show();
                return true;

            case R.id.action_classement:
                /*Cursor c = dao.getAllPlayers();
                c.getColumnNames();*/
                Toast.makeText(getApplicationContext(), "Classement !", Toast.LENGTH_SHORT).show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }



    @Override
    protected void onResume() {

        super.onResume();


        /**/

        Intent intent = getIntent();
        String epreuveOK_KO = intent.getStringExtra("epreuveOK_KO");
        int etapeCourante = intent.getIntExtra("etape", 0);
        int epreuveCourante = intent.getIntExtra("epreuve", 0);
        int point = intent.getIntExtra("point",0);
        int pointActuel = dao.getPoint(prenom);

        if(epreuveOK_KO!=null && epreuveOK_KO.contains("OK")){
            Log.i("TEST_P",prenom);
            dao.updateJoueur(prenom,point+pointActuel, etapeCourante,epreuveCourante);
            this.getEpreuve(doc, etapeCourante, epreuveCourante).getAttributes().getNamedItem("termine").setTextContent("true");
        }

        if(getEtape(doc,etapeCourante).getAttribute("termine").contains("false")){
            NodeList epreuves = getEtape(doc,etapeCourante).getElementsByTagName("Epreuve");
            Log.i("EPREUVE",""+epreuves.getLength());
            String etapeOK="KO";

            for (int i = 0 ; i<epreuves.getLength();i++){
                if(epreuves.item(i).getAttributes().getNamedItem("termine").getTextContent().contains("true")){
                    etapeOK="OK";
                }else{
                    etapeOK="KO";
                }
            }
            if(etapeOK.contains("OK")){
                getEtape(doc,etapeCourante).getAttributes().getNamedItem("termine").setTextContent("true");
                dao.updateJoueur(prenom,point+pointActuel, (etapeCourante+1),0);
            }
        }

        /*****************************************************************/
        registerReceiver(new ProximityReceiver(),new IntentFilter(ACTION_FILTER));

        //---utilisation  de la class LocationManager pour le gps---
        objgps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //*************ecouteur ou listener*********************
        objlistener = new Myobjlistener();

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
        objgps.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0,
                objlistener);

        //**variable qui pointe sur  mes champs d'affichage*************
        mTxtViewlong = (TextView) findViewById(R.id.textlong);
        mTxtViewlat = (TextView) findViewById(R.id.textlat);


        double latitudeEtape=0;
        double longitudeEtape=0;
        float rayonEtape=0;


        if(getEtape(doc,0).getAttributes().getNamedItem("termine").getTextContent().contains("false")){
           latitudeEtape =Double.parseDouble(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(0).getTextContent());
            longitudeEtape = Double.parseDouble(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(1).getTextContent());
           rayonEtape = Float.parseFloat(getEtape(doc, 0).getElementsByTagName("Zone").item(0).getChildNodes().item(2).getTextContent());
        }else{
            latitudeEtape =Double.parseDouble(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(0).getTextContent());
            longitudeEtape = Double.parseDouble(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(1).getTextContent());
            rayonEtape = Float.parseFloat(getEtape(doc, 1).getElementsByTagName("Zone").item(0).getChildNodes().item(2).getTextContent());
            Log.i("LAT",""+latitudeEtape);
        }
        Intent intentEtape = new Intent(ACTION_FILTER);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), -1, intentEtape, 0);
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


    public String getUrlEtape(Document doc, int indice){
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
        return (Element)epreuves.item(indiceEpreuve);
    }

    public Element getEtape(Document doc, int indiceEtape){
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
            Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
            //affichage des valeurs dans la les zone de saisie
            mTxtViewlat.setText(String.valueOf(location.getLatitude()));
            mTxtViewlong.setText(String.valueOf(location.getLongitude()));
            /*if (location.getLatitude() >= 49.494872 && location.getLongitude() >= 5.980767) {
                Intent intentPhoto = new Intent(ListingEpreuvesActivity.this, EpreuvePhotoActivity.class);
                startActivity(intentPhoto);
            }*/
        }

    }

    public class ProximityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            // The reciever gets the Context & the Intent that fired the broadcast as arg0 & agr1
            String k= LocationManager.KEY_PROXIMITY_ENTERING;
            // Key for determining whether user is leaving or entering
            boolean state=arg1.getBooleanExtra(k, false);
            //Gives whether the user is entering or leaving in boolean form
            int etapeEnCours = dao.getEtape(prenom);
            if(state){
                // Call the Notification Service or anything else that you would like to do here
                Toast.makeText(arg0, "Bienvenue à l'etape n° " + (etapeEnCours + 1), Toast.LENGTH_SHORT).show();
                Element etape = ListingEpreuvesActivity.this.getEtape(doc,etapeEnCours);
                etape.getAttributes().getNamedItem("visible").setTextContent("true");
                webView.loadUrl(urlEtape = ListingEpreuvesActivity.this.getUrlEtape(doc, etapeEnCours));


            }else{
                //Other custom Notification
                Toast.makeText(arg0, "Thank you for visiting my Area,come back again !!", Toast.LENGTH_LONG).show();

            }

        }

    }

}



