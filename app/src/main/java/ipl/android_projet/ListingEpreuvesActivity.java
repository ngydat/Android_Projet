package ipl.android_projet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ListingEpreuvesActivity extends AppCompatActivity {
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_epreuves);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.titre_tool_listing);

        Intent intent = getIntent();
        String prenom = intent.getStringExtra("prenom");

        TextView prenomView = (TextView) findViewById(R.id.prenom_content_listing);
        prenomView.setText(prenom);



        //Source : http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        //http://stackoverflow.com/questions/11820142/how-to-pass-a-file-path-which-is-in-assets-folder-to-filestring-path
        Context context = getApplicationContext();
        InputStream in = null;
        try {
            in = context.getAssets().open("CampusAlma.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }


        Document doc = null;

        DocumentBuilderFactory facto = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = facto.newDocumentBuilder();
            doc = builder.parse(in);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Etape");

            Node node = nList.item(0);

            Element e = (Element) node;

            CharSequence text = e.getAttribute("url");
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





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

}
