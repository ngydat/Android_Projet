package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
    private String aide;
    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;
    private TextView timerValue;
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epreuve_qcm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        timerValue = (TextView)findViewById(R.id.timerValue_epreuves);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);


        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        aide = intent.getStringExtra("aide");
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
        CharSequence text ;
        Intent itnt = new Intent(EpreuveQCMActivity.this, ListingEtapesActivity.class);

       if(bonneRepRb.isChecked()){
           text = "Bonne reponse !";
           Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
           itnt.putExtra("epreuveOK_KO", "OK");


       }else{
           text = "Mauvaise reponse !";
           Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
           point=0;
           itnt.putExtra("epreuveOK_KO","KO");
       }

        itnt.putExtra("point", point);
        itnt.putExtra("etape", etape);
        itnt.putExtra("epreuve", epreuve);
        itnt.putExtra("duree",updatedTime);
        itnt.putExtra("pseudo", pseudo);
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
        switch (item.getItemId()) {
            case R.id.action_aide:
                Toast.makeText(getApplicationContext(),aide, Toast.LENGTH_LONG).show();
                if(point!=0){
                    point--;
                }else{
                    Toast.makeText(getApplicationContext(),"Vous ne pouvez plus demander de l'aide !", Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

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



}
