package com.nexttech.sathethakun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            if(user!=null){
                Toast.makeText(context, "Sathe Thakun has started in background.", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(context, ForegroundService.class);
                serviceIntent.putExtra("inputExtra", "Sathe Thakun is running on Background.");
                ContextCompat.startForegroundService(context, serviceIntent);
            }
        }
    }
}
