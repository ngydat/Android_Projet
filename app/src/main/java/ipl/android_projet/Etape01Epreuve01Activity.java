package ipl.android_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


    }


}
