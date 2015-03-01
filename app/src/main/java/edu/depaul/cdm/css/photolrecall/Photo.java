package edu.depaul.cdm.css.photolrecall;

import android.net.Uri;

/**
 * Created by mattwylder on 3/1/15.
 */
public class Photo {
    private float lat;
    private float lng;

    Uri uri;

    public Photo( Uri uri, float lat, float lng){
        this.lat = lat;
        this.lng = lng;

        this.uri = uri;
    }


}
