package edu.depaul.cdm.css.photolrecall.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.depaul.cdm.css.photolrecall.R;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private static final String TAG = "";

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    private static final long MIN_TIME_BTWN_UPDATES = 20 * 1000;

    private SupportMapFragment frag;
    private ImageButton img;
    GoogleMap map;
    Location location;
    LocationManager locationManager;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageButton) findViewById(R.id.imageButton);
        frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToApproveActivity();
            }
        });

        map = frag.getMap();
        map.setMyLocationEnabled(true);

        try{
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            if(locationManager == null){
                Log.e(TAG, "failed to get locationManager");
                return;
            }

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

            String provider = locationManager.getBestProvider(criteria,true);

            if(provider == null){
                Log.e(TAG, "no provider found");
                return;
            }

            locationManager.requestLocationUpdates(provider,MIN_TIME_BTWN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            location = map.getMyLocation();
            location = locationManager.getLastKnownLocation(provider);
            if(location != null){
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Log.d(TAG, "Lat: " + lat + " Long: " + lon);
                Toast.makeText(getApplicationContext(), "Lat: " + lat + " Long: " + lon, Toast.LENGTH_LONG).show();
                latLng = new LatLng(lat, lon);
            }

            else
                Log.e(TAG, "No location");

            map.addMarker(new MarkerOptions().position(latLng).title("Here"));
        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }

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
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private void goToApproveActivity(){
        Intent i = new Intent(getApplicationContext(), ApproveActivity.class);
        startActivity(i);
    }

}
