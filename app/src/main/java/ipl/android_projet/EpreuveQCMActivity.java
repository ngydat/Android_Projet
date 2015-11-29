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

/**
 * Hunter Game : a treasure hunt app
 * Copyright (C) 2015 AGNELLO Giordano, NGUYEN Quoc Dat
 *  This file is part of Hunter Game.
 * Hunter Game is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses.
 */
public class EpreuveQCMActivity extends AppCompatActivity {


    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private RadioButton bonneRepRb;
    private RadioButton reponse2Rb;
    private RadioButton reponse3Rb;
    private String aide;
    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;
    private String bonneRep;
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

        bonneRep = intent.getStringExtra("bonneRep");
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
           text = "Bonne réponse !";
           Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
           itnt.putExtra("epreuveOK_KO", "OK");


       }else{
           text = "Mauvaise réponse ! La réponse etait : "+bonneRep;
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
        getMenuInflater().inflate(R.menu.menu_epreuve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_aide:
                Toast.makeText(getApplicationContext(),aide, Toast.LENGTH_LONG).show();
                point = 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }



}
