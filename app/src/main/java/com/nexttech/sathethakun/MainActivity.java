package com.nexttech.sathethakun;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.nexttech.sathethakun.Fragments.HelpFragment;
import com.nexttech.sathethakun.Fragments.PrivacyPolicyFragment;
import com.nexttech.sathethakun.Fragments.ProfileFragment;
import com.nexttech.sathethakun.Fragments.RequestUsersFragment;
import com.nexttech.sathethakun.Model.RequestModel;
import com.nexttech.sathethakun.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    Button btnStartService, btnStopService;

    CardView btnProfile, btnLogout , btnHelp, btnPrivacyPolicy;

    TextView userName, userPhone;
    CircleImageView ivProfilePic;

    public static ProgressBar progressBar;

    BottomNavigationView bottomNavigation;
    static DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser fUser;
    TextView navigation_add, connectedCount, requestCount;
    ConstraintLayout navigation_connected, navigation_request;

    String activeUserName;

    static TextView appTitle;
    ImageView navdrawer;

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
                Intent i =new Intent(context,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        }else {
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appTitle = toolbar.findViewById(R.id.title);
        navdrawer = toolbar.findViewById(R.id.nav_drawer);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);

        userName = findViewById(R.id.tv_user_name);
        userPhone = findViewById(R.id.tv_user_phone);
        ivProfilePic = findViewById(R.id.iv_profile_pic);
        progressBar = findViewById(R.id.progress_bar);
        btnProfile = findViewById(R.id.button_profile);
        btnLogout = findViewById(R.id.button_logout);
        btnHelp=findViewById(R.id.button_help);
        btnPrivacyPolicy = findViewById(R.id.button_privacy_policy);


        navigation_connected = bottomNavigation.findViewById(R.id.connected);
        navigation_add = bottomNavigation.findViewById(R.id.add);
        navigation_request = bottomNavigation.findViewById(R.id.request);
        connectedCount = bottomNavigation.findViewById(R.id.connected_count);
        requestCount = bottomNavigation.findViewById(R.id.request_count);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        navigation_connected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(getSupportFragmentManager().beginTransaction(), new ConnectedUsersFragment(), "Connected Users", false);
            }
        });

        navigation_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(getSupportFragmentManager().beginTransaction(), new AddUsersFragment(), "Add User", false);
            }
        });
        navigation_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(getSupportFragmentManager().beginTransaction(), new RequestUsersFragment(), "Requests", false);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fUser = mAuth.getCurrentUser();
        updateUI(fUser,this);

        openFragment(getSupportFragmentManager().beginTransaction(), new ConnectedUsersFragment(), "Connected Users", false);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProfileFragment("my_profile");

                openFragment(getSupportFragmentManager().beginTransaction(), fragment, activeUserName, false);


            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new PrivacyPolicyFragment();

                openFragment(getSupportFragmentManager().beginTransaction(), fragment, "Privacy Policy", true);

            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new HelpFragment();

                openFragment(getSupportFragmentManager().beginTransaction(), fragment, "Help", true);

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginandRegisterholder.class));
                finish();

            }
        });

        getConnectedUserCount();
        getRequestUserCount();


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

    private void getRequestUserCount() {
        DatabaseReference myRef = database.getReference("User Requests");

        final List<RequestModel> requests = new ArrayList<>();;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestModel requestModel = snapshot.getValue(RequestModel.class);

                    if (requestModel.getReceiver().equals(fUser.getUid())){
                        requests.add(requestModel);
                    }
                }

                if (requests.size()>0){
                    requestCount.setVisibility(View.VISIBLE);
                    requestCount.setText(String.valueOf(requests.size()));
                } else {
                    requestCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getConnectedUserCount() {
        DatabaseReference myRef = database.getReference("Connected").child(fUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    connectedCount.setVisibility(View.VISIBLE);
                    connectedCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                } else {
                    connectedCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RetriveUserData() {
        DatabaseReference myRef = database.getReference("Users").child(fUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                activeUserName = userModel.getName();

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

    public static void openFragment(FragmentTransaction transaction, Fragment fragment, String title, boolean back) {
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        appTitle.setText(title);

        if (back){
            transaction.addToBackStack(null);
        }

        closeDrawer();
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
        }
    }

    public void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public static void progressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void progressBarGone(){
        progressBar.setVisibility(View.GONE);
    }
}
