package com.nexttech.sathethakun;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.nexttech.sathethakun.Fragments.AddUsersFragment;
import com.nexttech.sathethakun.Fragments.ConnectedUsersFragment;
import com.nexttech.sathethakun.Fragments.RequestUsersFragment;
import com.nexttech.sathethakun.Model.UserModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Button btnStartService, btnStopService;

    CardView btnLogout;

    TextView userName, userPhone;
    CircleImageView ivProfilePic;

    BottomNavigationView bottomNavigation;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser fUser;
    TextView navigation_connected,navigation_add,navigation_request;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser,this);
    }

    public static void updateUI(FirebaseUser currentUser, Context context) {

        if (currentUser != null ) {

            if(context instanceof MainActivity){
                ((MainActivity)context).RetriveUserData();
            }else {
                Log.e("activity","main");
                Intent i =new Intent(context,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        }else {
            Log.e("user","null");
            Intent i = new Intent(context,LoginandRegisterholder.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        userName = findViewById(R.id.tv_user_name);
        userPhone = findViewById(R.id.tv_user_phone);
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        btnLogout = findViewById(R.id.button_logout);
        //startActivity(new Intent(this,RegisterActivity.class));
        navigation_connected = bottomNavigation.findViewById(R.id.connected);
        navigation_add = bottomNavigation.findViewById(R.id.add);
        navigation_request = bottomNavigation.findViewById(R.id.request);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        navigation_connected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new ConnectedUsersFragment());
            }
        });

        navigation_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new AddUsersFragment());
            }
        });
        navigation_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new RequestUsersFragment());
            }
        });



        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fUser = mAuth.getCurrentUser();
        updateUI(fUser,this);



        openFragment(new ConnectedUsersFragment());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginandRegisterholder.class));
                finish();

            }
        });


        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void RetriveUserData() {
        DatabaseReference myRef = database.getReference("Users").child(fUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                userName.setText(userModel.getName());
                userPhone.setText(userModel.getPhone());

                if (userModel.getImageUri().equals("default")){
                    ivProfilePic.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(userModel.getImageUri()).into(ivProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Sathe Thakun is running on Background.");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Location Permission Granted!", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied 'Access Location'", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
