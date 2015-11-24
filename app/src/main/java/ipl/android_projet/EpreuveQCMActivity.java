package ipl.android_projet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EpreuveQCMActivity extends AppCompatActivity {


    private RadioButton bonneRepRb;
    private RadioButton reponse2Rb;
    private RadioButton reponse3Rb;
    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epreuve_qcm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        point = intent.getIntExtra("point",0);
        pseudo = intent.getStringExtra("pseudo");

        etape = intent.getIntExtra("etape",0);
        epreuve = intent.getIntExtra("epreuve",0);

        TextView questionTv = (TextView) findViewById(R.id.question_epreuveQCM);

        questionTv.setText(question + " (" + point + " points)");

        String bonneRep = intent.getStringExtra("bonneRep");
        bonneRepRb = (RadioButton) findViewById(R.id.reponse1_epreuveQCM);
        bonneRepRb.setText(bonneRep);

        String [] reponses = intent.getStringArrayExtra("reponses");
        reponse2Rb = (RadioButton) findViewById(R.id.reponse2_epreuveQCM);
        reponse2Rb.setText(reponses[0]);

        reponse3Rb = (RadioButton) findViewById(R.id.reponse3_epreuveQCM);
        reponse3Rb.setText(reponses[1]);


    }


    public void confirmer(View v){
        Context context = getApplicationContext();
        CharSequence text ;
        int duration = Toast.LENGTH_SHORT;
        Intent itnt = new Intent(EpreuveQCMActivity.this, ListingEpreuvesActivity.class);
        itnt.putExtra("etape", etape);
        itnt.putExtra("epreuve", epreuve);
        itnt.putExtra("point", point);
        itnt.putExtra("pseudo", pseudo);

       if(bonneRepRb.isChecked()){
           text = "Bonne reponse !";
           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
           itnt.putExtra("epreuveOK_KO", "OK");


       }else{
           text = "Mauvaise reponse !";
           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
           itnt.putExtra("epreuveOK_KO","KO");
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
