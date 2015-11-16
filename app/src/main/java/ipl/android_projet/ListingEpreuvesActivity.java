package ipl.android_projet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

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

public class ListingEpreuvesActivity extends AppCompatActivity {
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_etapes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.titre_tool_listing);



        Intent intent = getIntent();
        String prenom = intent.getStringExtra("prenom");

        TextView prenomView = (TextView) findViewById(R.id.prenom_content_listing);
        prenomView.setText("Bienvenue " + prenom);


        final Document doc = this.parseAsset("CampusAlma.xml");
        doc.getDocumentElement().normalize();


        String urlEtape1 = this.getUrlEtape(doc, 0);

        WebView webView = (WebView) findViewById(R.id.webView_content_listing);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(urlEtape1);


        // somewhere on your code...
        WebViewClient yourWebClient = new WebViewClient() {
            // you tell the webclient you want to catch when a url is about to load

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("http://epreuve1_etape1.qcm")) {

                    Element epreuve1 = ListingEpreuvesActivity.this.getEpreuve(doc, 0, 0);
                    Intent itnt = new Intent(ListingEpreuvesActivity.this, EpreuveQCMActivity.class);
                    String question = epreuve1.getFirstChild().getTextContent();
                    itnt.putExtra("question", question);


                    String [] reponses = new String[2];
                    String bonneRep = "";
                    for(int i = 1 ; i<4;i++){
                        if(epreuve1.getChildNodes().item(i).getAttributes().getNamedItem("bonne").getTextContent().contains("true")){
                            bonneRep = epreuve1.getChildNodes().item(i).getTextContent();
                        }else{
                            reponses [i-1]= epreuve1.getChildNodes().item(i).getTextContent();
                        }
                    }

                    Log.i("TEST",epreuve1.getChildNodes().item(1).getTextContent());
                    itnt.putExtra("bonneRep",bonneRep);
                    itnt.putExtra("reponses",reponses);

                    startActivity(itnt);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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



}
