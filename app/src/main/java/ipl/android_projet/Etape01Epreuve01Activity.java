package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Etape01Epreuve01Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape01_epreuve01);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String question = intent.getStringExtra("question");

        TextView questionTv = (TextView) findViewById(R.id.question_etape01_epreuve01);
        questionTv.setText(question);

        String bonneRep = intent.getStringExtra("bonneRep");
        RadioButton bonneRepRb = (RadioButton) findViewById(R.id.reponse1_etape01_epreuve01);
        bonneRepRb.setText(bonneRep);

        String [] reponses = intent.getStringArrayExtra("reponses");
        RadioButton reponse2Rb = (RadioButton) findViewById(R.id.reponse2_etape01_epreuve01);
        reponse2Rb.setText(reponses[0]);

        RadioButton reponse3Rb = (RadioButton) findViewById(R.id.reponse3_etape01_epreuve01);
        reponse3Rb.setText(reponses[1]);


    }


}
