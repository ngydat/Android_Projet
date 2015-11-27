package ipl.android_projet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EpreuvePhotoActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button b1;
    ImageView iv;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;
    private String aide;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
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
        setContentView(R.layout.activity_epreuve_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        timerValue = (TextView) findViewById(R.id.timerValue_epreuves);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);


        Intent intent = getIntent();

        String question = intent.getStringExtra("question");
        aide = intent.getStringExtra("aide");

        point = intent.getIntExtra("point", 0);
        pseudo = intent.getStringExtra("pseudo");

        etape = intent.getIntExtra("etape",0);
        epreuve = intent.getIntExtra("epreuve",0);

        TextView questionTv = (TextView) findViewById(R.id.question_content_epreuve_photo);

        questionTv.setText(question + " (" + point + " points)");


        b1=(Button)findViewById(R.id.button_content_epreuve_photo);
        iv=(ImageView)findViewById(R.id.imageView_content_epreuve_photo);

        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerPhoto();
            }
        });

    }

    private void lancerPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(data!=null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                if (bp != null) {
                    iv.setImageBitmap(bp);
                }


            }
            b1.setText("Confirmer");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itnt = new Intent(EpreuvePhotoActivity.this, ListingEtapesActivity.class);
                    itnt.putExtra("etape", etape);
                    itnt.putExtra("epreuve", epreuve);
                    itnt.putExtra("point", point);
                    itnt.putExtra("pseudo", pseudo);
                    itnt.putExtra("epreuveOK_KO", "OK");
                    itnt.putExtra("duree",updatedTime);
                    startActivity(itnt);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

                if(point!=0){
                    Toast.makeText(getApplicationContext(),aide, Toast.LENGTH_LONG).show();
                    point--;
                }else{
                    Toast.makeText(getApplicationContext(), "Vous en pouvez plus demander l'aide pour cette epreuve !", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }





}
