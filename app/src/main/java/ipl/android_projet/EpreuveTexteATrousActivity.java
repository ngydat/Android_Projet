package ipl.android_projet;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class EpreuveTexteATrousActivity extends AppCompatActivity {

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;
    private EditText reponse1;
    private EditText reponse2;
    private String[] reponses;
    private String aide;
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
        setContentView(R.layout.activity_epreuve_texte_atrous);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        timerValue = (TextView)findViewById(R.id.timerValue_epreuves);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        point = intent.getIntExtra("point", 0);
        pseudo = intent.getStringExtra("pseudo");
        aide = intent.getStringExtra("aide");

        etape = intent.getIntExtra("etape", 0);
        epreuve = intent.getIntExtra("epreuve", 0);

        TextView questionTv = (TextView) findViewById(R.id.question_t_a_t);
        questionTv.setText(question + " (" + point + " points)");

        reponses = intent.getStringArrayExtra("reponses");
        reponse1 = (EditText) findViewById(R.id.reponse_texte_a_t_1);
        reponse2 = (EditText) findViewById(R.id.reponse_texte_a_t_2);;



    }

    public void confirmer(View v) {
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_LONG;
        Intent itnt = new Intent(EpreuveTexteATrousActivity.this, ListingEtapesActivity.class);

        if(!(reponse1.getText().toString().equals(reponses[0])) && !(reponse2.getText().toString().equals(reponses[1])) ){
            point=0;
            text = "Les 2 réponses sont mauvaises, les 2 réponses sont : "+reponses[0]+" et "+reponses[1];
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "KO");
        }
        else if (!(reponse1.getText().toString().equals(reponses[0]))) {
            text = "La premiere réponse est mauvaise, la réponse etait : " + reponses[0];
            point--;
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "KO");
        }else if(!(reponse2.getText().toString().equals(reponses[1]))) {
            point--;
            text = "La premiere réponse est mauvaise, la réponse etait : " + reponses[1];
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "KO");

        }else {
            text = "Bonnes réponses";
            Toast.makeText(context, text, duration).show();
            itnt.putExtra("epreuveOK_KO", "OK");
        }
        itnt.putExtra("etape", etape);
        itnt.putExtra("epreuve", epreuve);
        itnt.putExtra("pseudo", pseudo);
        itnt.putExtra("duree",updatedTime);
        itnt.putExtra("point", point);
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
            case R.id.action_aide:
                Toast.makeText(getApplicationContext(),aide, Toast.LENGTH_LONG).show();
                point =0;
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

}
