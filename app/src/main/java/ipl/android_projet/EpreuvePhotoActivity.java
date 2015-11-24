package ipl.android_projet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EpreuvePhotoActivity extends AppCompatActivity {

    Button b1;
    ImageView iv;

    private int etape;
    private int epreuve;
    private int point;
    private String pseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epreuve_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        String question = intent.getStringExtra("question");

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bp);
        }
        if(data!=null) {
            b1.setText("Confirmer");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itnt = new Intent(EpreuvePhotoActivity.this, ListingEpreuvesActivity.class);
                    itnt.putExtra("etape", etape);
                    itnt.putExtra("epreuve", epreuve);
                    itnt.putExtra("point", point);
                    itnt.putExtra("pseudo", pseudo);
                    itnt.putExtra("epreuveOK_KO", "OK");
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
