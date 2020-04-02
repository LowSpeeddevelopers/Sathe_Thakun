package com.nexttech.sathethakun;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.nexttech.sathethakun.Model.FDM;
import com.nexttech.sathethakun.Model.UserModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ForegroundService extends Service implements GetLocation {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    DatabaseReference database;
    FirebaseUser user;
    CurrentLocation currentLocation;
    Ringtone r;
    List<String> list;
    AlertDialog dialog;
    @Override
    public void onCreate() {
        super.onCreate();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child("Location").child(user.getUid());
        list = new ArrayList<>();
        getConnectedList();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        DatabaseReference tempref = FirebaseDatabase.getInstance().getReference().child("Emergency");
        tempref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e("key", dataSnapshot.getKey());
                Log.e("value", dataSnapshot.getValue().toString());
                if (list.contains(dataSnapshot.getKey()) && dataSnapshot.getValue(Boolean.class)) {

                    if (!r.isPlaying()) {
                        r.play();
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View VI = inflater.inflate(R.layout.worninglayout, null, false);
                    builder.setView(VI);
                    TextView message = VI.findViewById(R.id.worningmessage);
                    TextView call = VI.findViewById(R.id.call);
                    TextView openLocation = VI.findViewById(R.id.openlocation);
                    builder.setCancelable(false);
                    dialog = builder.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                    DatabaseReference tenoref = FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot.getKey());


                    tenoref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);

                            message.setText(model.getName()+" might be in trouble. Would you like to make a phone call or review his location?");
                            call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String phone = model.getPhone();
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                    startActivity(intent);
                                    dialog.dismiss();

                                }
                            });
                            openLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {



                                    Intent i = new Intent(getApplicationContext(),DirectionsActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.putExtra("userid",model.getUserID());
                                    startActivity(i);
                                    dialog.dismiss();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                    dialog.show();





                }else if(list.contains(dataSnapshot.getKey())&&!dataSnapshot.getValue(Boolean.class)){
                    if(r.isPlaying()){
                        r.stop();
                    }
                    if(dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");


        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sathe Thakun")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        currentLocation = new CurrentLocation(this);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentLocation.locationManager.removeUpdates(currentLocation);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e("location","changed");
        Date date = new Date(location.getTime());
        DateFormat dateFormat =  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
        String time = dateFormat.format(date);
        FDM fdm = new FDM(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),time);
        database.setValue(fdm);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Your GPS is Turned off please turn it on then try again.", Toast.LENGTH_SHORT).show();
    }


    private void getConnectedList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Connected").child(user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getValue(String.class);
                    list.add(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
