package edu.depaul.cdm.css.photolrecall.activity;
import android.app.Activity;
import android.os.Bundle;
import edu.depaul.cdm.css.photolrecall.R;
import com.google.android.gms.common.api.GoogleApiClient;
/**
 * Created by OZ on 2/28/2015.
 */
public class MapActivity extends Activity implements GoogleApiClient.ConnectionCallbacks{
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_map);
    }
    @Override
    public void onConnected(Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
}