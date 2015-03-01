package edu.depaul.cdm.css.photolrecall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import edu.depaul.cdm.css.photolrecall.R;

public class ApproveActivity extends ActionBarActivity implements LocationListener {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "ApproveActivity";

    private LocationManager locationManager;

    private ImageView img;

    private static String appname;
    private Button cancel;
    private Button confirm;
    private LatLng loc;
    private Map<LatLng, Bitmap> map;

    private Bitmap bmp;


    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loc= (LatLng) getIntent().getExtras().get("loc");
        setContentView(R.layout.activity_approve);
        appname = getResources().getText(R.string.photo_dir).toString();

        cancel = (Button) findViewById(R.id.buttonCancel);
        confirm = (Button) findViewById(R.id.buttonConfirm);
        img = (ImageView) findViewById(R.id.approveImageView);

        Firebase.setAndroidContext(this);

        sendCameraIntent();

        Log.d(TAG, "imgURI= " + imgUri);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendImage();
            }
        });

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
        Log.d(TAG, "OnActivityResult");
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Activity result with requestCode: " + requestCode);
            //img.setImageURI(data.getData());
           // imgUri = data;
            Log.d(TAG, "Intent: " + data);
            Log.d(TAG, "Intent.getData(): " + data.getExtras().get("data"));
            Log.d(TAG, "Intent.getExtras().get()" + data.getExtras().get("uri"));
            Log.d(TAG, "imgURI: " + imgUri);
           // String path = imgUri.getPath();
            bmp = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bmp);


            Log.d(TAG, "LatLng: " + loc);
        }
        if(resultCode == RESULT_CANCELED)
        {
            finish();
        }
        else
            Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG);
    }

    private void sendImage(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        map = new Map<LatLng, Bitmap>() {
            @Override
            public void clear() {

            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @NonNull
            @Override
            public Set<Entry<LatLng, Bitmap>> entrySet() {
                return null;
            }

            @Override
            public Bitmap get(Object key) {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Set<LatLng> keySet() {
                return null;
            }

            @Override
            public Bitmap put(LatLng key, Bitmap value) {
                return null;
            }

            @Override
            public void putAll(Map<? extends LatLng, ? extends Bitmap> map) {

            }

            @Override
            public Bitmap remove(Object key) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public Collection<Bitmap> values() {
                return null;
            }
        };

        try{
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            if(locationManager == null){
                Log.e(TAG, "failed to get locationManager");
                return;
            }

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            String provider = locationManager.getBestProvider(criteria,true);

            if(provider == null){
                Log.e(TAG, "no provider found");
                return;
            }

            locationManager.requestLocationUpdates(provider,10000000,
                    100000000, this);

            //location = map.getMyLocation();
            Location location = locationManager.getLastKnownLocation(provider);
            if(location != null){
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Log.d(TAG, "Lat: " + lat + " Long: " + lon);
                Toast.makeText(getApplicationContext(), "Lat: " + lat + " Long: " + lon, Toast.LENGTH_LONG).show();
                loc = new LatLng(lat, lon);
            }

            else
                Log.e(TAG, "No location");
        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }



        map.put(loc, bmp);

    }

    @Override
    public void onLocationChanged(Location location){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

    }

    @Override
    public void onProviderEnabled(String provider){

    }

    @Override
    public void onProviderDisabled(String provider){

    }

}
