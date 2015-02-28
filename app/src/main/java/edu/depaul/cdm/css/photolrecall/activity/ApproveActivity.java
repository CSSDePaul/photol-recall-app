package edu.depaul.cdm.css.photolrecall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.depaul.cdm.css.photolrecall.R;

public class ApproveActivity extends ActionBarActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "ApproveActivity";

    ImageView img;

    private static String appname;

    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        appname = getResources().getText(R.string.photo_dir).toString();

        img = (ImageView) findViewById(R.id.approveImageView);

        sendCameraIntent();

        Log.d(TAG, "imgURI= " + imgUri);

        img.setImageURI(imgUri);
        img.invalidate();
        img.postInvalidate();

        //Bitmap bit = BitmapFactory.decodeFile(imgUri.getPath());
       // img.setImageBitmap(bit);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_approve, menu);
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
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appname);
        Log.d(TAG, "mediaStorageDir == " + mediaStorageDir.getAbsolutePath() + ".exists() == " + mediaStorageDir.exists());
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

       // imgUri = getOutputMediaFileUri();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "Activity result with requestCode: " + requestCode);
            //img.setImageURI(data.getData());
           // imgUri = data;
            Log.d(TAG, "Intent: " + data);
            Log.d(TAG, "Intent.getData(): " + data.getExtras().get("data"));
            Log.d(TAG, "Intent.getExtras().get()" + data.getExtras().get("uri"));
            Log.d(TAG, "imgURI: " + imgUri);
           // String path = imgUri.getPath();
            img.setImageBitmap((Bitmap) data.getExtras().get("data"));

        }
        else
            Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG);
    }
}
