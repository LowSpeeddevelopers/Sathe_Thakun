package com.nexttech.sathethakun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class CurrentLocation implements LocationListener {

    Context context;
    LocationManager locationManager;
    String provider;
    GetLocation getLocation;


    public CurrentLocation(Context context) {

        this.context = context;
        getLocation = (GetLocation) context;
        location();
    }

    public void location() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals(" ")) {

            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 5000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else {
                 Toast.makeText(context, "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(context, "No Provider Found or Permission Not Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Log.e("Location", location.getProvider() + "==" + location.getAccuracy() + "==" + location.getAltitude() + "==" + location.getLatitude() + "==" + location.getLongitude());
        getLocation.onLocationChanged(location);
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        getLocation.onStatusChanged(s, i, bundle);
    }

    @Override
    public void onProviderEnabled(String s) {
        getLocation.onProviderEnabled(s);
    }

    @Override
    public void onProviderDisabled(String s) {
        getLocation.onProviderDisabled(s);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Warning Message");
        alertDialog.setPositiveButton("Allow", (dialog, which) -> {
            // TODO Auto-generated method stub
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(myIntent);
            dialog.dismiss();
        });
        alertDialog.setNegativeButton("cancel", (dialog, which) -> {
            // TODO Auto-generated method stub
            Toast.makeText(context, "Permission Refused", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}