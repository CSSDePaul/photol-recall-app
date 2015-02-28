package edu.depaul.cdm.css.photolrecall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.depaul.cdm.css.photolrecall.R;
import edu.depaul.cdm.css.photolrecall.adapter.ImageAdapter;

public class PhotoListActivity extends ActionBarActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "PhotoListActivity";

    private ImageButton cameraButton;
    private GridView gridView;

    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        gridView =(GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));

        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToApproveActivity();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile(){

        Environment.getExternalStorageState();
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), TAG);
        Log.d(TAG, "mediaStorageDir == " + mediaStorageDir.getPath() + ".exists() == " + mediaStorageDir.exists());
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d(TAG, "Failed to create directory " + mediaStorageDir );
                //return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    private void sendCameraIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

       imgUri = getOutputMediaFileUri();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void goToApproveActivity(){
        Intent i = new Intent(getApplicationContext(), ApproveActivity.class);
        startActivity(i);
    }
}
