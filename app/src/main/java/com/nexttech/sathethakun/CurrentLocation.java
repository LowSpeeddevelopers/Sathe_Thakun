package com.nexttech.sathethakun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.mapbox.geojson.Point;

import java.util.HashMap;

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

            }
            // Toast.makeText(context, "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Log.e("Location", location.getProvider() + "==" + location.getAccuracy() + "==" + location.getAltitude() + "==" + location.getLatitude() + "==" + location.getLongitude());
        getLocation.onLocationChanged(location);
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude());
        Log.e("location",message);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e("onStatusChanged", "==" + s);
        getLocation.onStatusChanged(s, i, bundle);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e("onProviderEnabled", "==" + s);
        getLocation.onProviderEnabled(s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e("onProviderDisabled", "==" + s);
        getLocation.onProviderDisabled(s);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("this is a dialog box ");
        alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "ok ive wrote this 'ok' here", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "cancel ' comment same as ok'", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}