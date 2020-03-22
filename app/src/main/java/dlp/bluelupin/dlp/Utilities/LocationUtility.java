package dlp.bluelupin.dlp.Utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import dlp.bluelupin.dlp.Consts;

public class LocationUtility {

    static public double currentLatitude = 0.0;
    static public double currentLongitude = 0.0;
    static private FusedLocationProviderClient mFusedLocationClient;
    static private LocationRequest mLocationRequest;
    static private LocationCallback mLocationCallback;
    static private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    static private long FASTEST_INTERVAL = 2000; /* 2 sec */

    /**
     * Location update and
     * <p>
     * get currentLongitude
     * get
     *
     * @param context
     */


    public static void getmFusedLocationClient(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

    }


    // Trigger new location updates at interval
    public static void startLocationUpdates(Context context) {
        if (mFusedLocationClient != null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            SettingsClient settingsClient = LocationServices.getSettingsClient(context);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
               /* for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }*/
                    onLocationChanged(locationResult.getLastLocation());
                }
            };
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
       /* mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());*/
        }

    }


    //get location change
    public static void onLocationChanged(Location location) {
        // New location has now been determined
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Log.d(Consts.LOG_TAG, "Current Location*******" + msg);

      /*  Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
         LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());*/
    }

    //get last location
    public void getLastLocation(Context context) {
        // Get last known recent location using new Google Play Services SDK (v11+)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    onLocationChanged(location);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Consts.LOG_TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }


    public static void removemFusedLocationClient() {
        if (mFusedLocationClient != null && mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }


    }


    //stop location update
    public static void stopLocationUpdates() {
        removemFusedLocationClient();
    }


    /**
     * Calculate the getDistance between two Location points using Location API functions:
     * @param latlngA
     * @param latlngB
     * @return
     */

    public static String getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");
        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);
        float distance = locationA.distanceTo(locationB)/1000;//To convert Meter in Kilometer
        return String.format("%.2f", distance);
    }

    /**
     * Calculate the distance between two LatLng points using Math function:
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static String Distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        // earth radius is in mile
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat_a))
                * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        double kmConvertion = 1.6093;
        // return new Float(distance * meterConversion).floatValue();
        return String.format("%.2f", new Float(distance * kmConvertion).floatValue()) + " km";
        // return String.format("%.2f", distance)+" m";
    }
}
