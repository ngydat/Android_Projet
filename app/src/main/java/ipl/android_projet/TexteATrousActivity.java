package ipl.android_projet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TexteATrousActivity extends AppCompatActivity {

    private int etape;
    private int epreuve;
    private int point;
    private String prenom;
    private EditText reponse1;
    private EditText reponse2;
    private EditText reponse3;
    private String[] reponses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texte_atrous);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        point = intent.getIntExtra("point", 0);
        prenom = intent.getStringExtra("prenom");

        etape = intent.getIntExtra("etape", 0);
        epreuve = intent.getIntExtra("epreuve", 0);

        TextView questionTv = (TextView) findViewById(R.id.textView1);
        questionTv.setText(question + " (" + point + " points)");

        reponses = intent.getStringArrayExtra("reponses");
        reponse1 = (EditText) findViewById(R.id.editText1);
        reponse2 = (EditText) findViewById(R.id.editText2);
        reponse3 = (EditText) findViewById(R.id.editText3);


    }

    public void confirmer(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        Intent itnt = new Intent(TexteATrousActivity.this, ListingEpreuvesActivity.class);
        itnt.putExtra("etape", etape);
        itnt.putExtra("epreuve", epreuve);
        itnt.putExtra("point", point);
        itnt.putExtra("prenom", prenom);

        if (!(reponse1.getText().toString().equals(reponses[0]) && reponse2.getText().toString().equals(reponses[1]) && reponse3.getText().toString().equals(reponses[2]))) {
            text = "Une des réponses est mauvaise";
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "KO");
        } else {
            text = "Bonne réponse";
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "OK");
        }
        startActivity(itnt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog box = null;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_aide:
                box = new Dialog(this);
                //box.setContentView(R.layout.dialog_layout);
                box.setTitle("Help !");
                box.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

}
