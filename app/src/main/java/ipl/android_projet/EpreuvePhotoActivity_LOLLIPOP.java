package ipl.android_projet;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class EpreuvePhotoActivity_LOLLIPOP extends AppCompatActivity {

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
    private double latitudeEpreuve;
    private double longtitudeEpreuve;
    private float rayonEpreuve;
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private Uri uriSaved;
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

        etape = intent.getIntExtra("etape", 0);
        epreuve = intent.getIntExtra("epreuve", 0);
        latitudeEpreuve = intent.getDoubleExtra("latitudeEpreuve", 0.0);
        longtitudeEpreuve = intent.getDoubleExtra("longitudeEpreuve", 0.0);
        rayonEpreuve = intent.getFloatExtra("rayonEpreuve", 0);

        TextView questionTv = (TextView) findViewById(R.id.question_content_epreuve_photo);

        questionTv.setText(question + " (" + point + " points)");

        File destination = new File(Environment
                .getExternalStorageDirectory(), "AndroidProject.jpg");
        uriSaved = Uri.fromFile(destination);

        b1 = (Button) findViewById(R.id.button_content_epreuve_photo);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT,
                        uriSaved);
                startActivityForResult(intentPhoto, REQUEST_IMAGE_CAPTURE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    iv = (ImageView) findViewById(R.id.imageView_content_epreuve_photo);
                    iv.setImageURI(uriSaved);
                    b1.setText("Confirmer");
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }


            }


            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itnt = new Intent(EpreuvePhotoActivity_LOLLIPOP.this, ListingEtapesActivity.class);
                    itnt.putExtra("etape", etape);
                    itnt.putExtra("epreuve", epreuve);
                    itnt.putExtra("point", point);
                    itnt.putExtra("pseudo", pseudo);


                    //pour confirmer la photo, on récupère les coordonnées gps de la photo
                    //http://stackoverflow.com/questions/9868158/get-gps-location-of-a-photo
                    String path = uriSaved.getPath();
                    ExifInterface exif = null;
                    String latitude = "";
                    String longitude = "";
                    String msg = "";

                    try {
                        exif = new ExifInterface(path);
                        latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

                        //La location GPS de l'appareil photo n'a pas été activé
                        if (latitude == null || longitude == null) {
                            msg = "Veuillez activer la localisation GPS sur votre appareil photo";
                        } else {
                            //la latitude ou la longitude est présentée sous la forme : voir url source
                            double latitudeDouble = getCoordonnee(latitude);
                            double longtitudeDouble = getCoordonnee(longitude);

                            //http://stackoverflow.com/questions/22063842/check-if-a-latitude-and-longitude-is-within-a-circle
                            float[] results = new float[1];
                            Location.distanceBetween(latitudeEpreuve, longtitudeEpreuve, latitudeDouble, longtitudeDouble, results);

                            Log.d("latitude photo", String.valueOf(latitudeDouble));
                            Log.d("longitude photo", String.valueOf(longtitudeDouble));
                            Log.d("lat_epreuve", String.valueOf(latitudeEpreuve));
                            Log.d("long_epreuve", String.valueOf(longtitudeEpreuve));
                            Log.d("rayon_epreuve", String.valueOf(rayonEpreuve));
                            float distanceInMeters = results[0];

                            Log.d("distance", String.valueOf(distanceInMeters));
                            boolean isWithin = distanceInMeters <= rayonEpreuve;

                            if (isWithin) {
                                msg = "Photo accepté";
                                itnt.putExtra("epreuveOK_KO", "OK");
                            } else {
                                msg = "Vous n'êtes pas dans la zone de la photo";
                                itnt.putExtra("epreuveOK_KO", "KO");
                            }
                            itnt.putExtra("duree", updatedTime);

                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                    startActivity(itnt);
                }
            });

            //pour confirmer la photo, on récupère les coordonnées gps de la photo

        }

    }

    /*
     * @param exif : la latitude ou la longitude exprimée sous la
     * forme d'une chaine de caractères avec 3 quotients séparés par une virgule
     *
     * @return : la latitude ou la longitude exprimée sous
     * la forme d'un nombre décimale
     */
    private double getCoordonnee(String exif) {
        String[] tab = exif.split(",");

        String degres = tab[0];
        String minutes = tab[1];
        String secondes = tab[2];
        double resultat = 0;

        //Traitement des degrés
        String[] degresTab = degres.split("/");
        double degre = Double.valueOf(degresTab[0]) / Double.valueOf(degresTab[1]);

        //Traitement des minutes
        String[] minutesTab = minutes.split("/");
        double minute = Double.valueOf(minutesTab[0]) / Double.valueOf(minutesTab[1]);

        //Traitement des secondes
        String[] secondesTab = secondes.split("/");
        double seconde = Double.valueOf(secondesTab[0]) / Double.valueOf(secondesTab[1]);


        resultat = (minute * 60 + seconde) / 3600 + degre;

        return resultat;
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

                if (point != 0) {
                    Toast.makeText(getApplicationContext(), aide, Toast.LENGTH_LONG).show();
                    point--;
                } else {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez plus demander l'aide pour cette epreuve !", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
