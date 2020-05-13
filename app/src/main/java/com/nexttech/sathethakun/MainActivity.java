package com.nexttech.sathethakun;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.nexttech.sathethakun.Fragments.ContactFragment;
import com.nexttech.sathethakun.Fragments.HelpFragment;
import com.nexttech.sathethakun.Fragments.PrivacyPolicyFragment;
import com.nexttech.sathethakun.Fragments.ProfileFragment;
import com.nexttech.sathethakun.Fragments.RequestUsersFragment;
import com.nexttech.sathethakun.Model.RequestModel;
import com.nexttech.sathethakun.Model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {



    CardView btnHome, btnProfile, btnAbout, btnLogout, btnHelp, btnPrivacyPolicy;

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

    FloatingActionButton fabAlert;

    String activeUserName;

    static TextView appTitle;
    ImageView navdrawer;

    boolean alert = false;

    AlertDialog.Builder builder;

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasConnection()){
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check your internet connection.") .setTitle("No Internet");
            builder.setMessage("Please check your internet connection.")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("No Internet");
            alert.show();
        }


        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser,this);
    }

    public static void updateUI(FirebaseUser currentUser, Context context) {

        if (currentUser != null ) {

            if(context instanceof MainActivity){
                ((MainActivity)context).fetchAlertData();
                ((MainActivity)context).RetriveUserData();
                ((MainActivity)context).getConnectedUserCount();
                ((MainActivity)context).getRequestUserCount();

                ((MainActivity)context).startService();

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
        btnHome = findViewById(R.id.button_home);
        btnProfile = findViewById(R.id.button_profile);
        btnAbout = findViewById(R.id.button_about);
        btnLogout = findViewById(R.id.button_logout);
        btnHelp=findViewById(R.id.button_help);
        btnPrivacyPolicy = findViewById(R.id.button_privacy_policy);
        fabAlert = findViewById(R.id.fab_alert);
        navigation_connected = bottomNavigation.findViewById(R.id.connected);
        navigation_add = bottomNavigation.findViewById(R.id.add);
        navigation_request = bottomNavigation.findViewById(R.id.request);
        connectedCount = bottomNavigation.findViewById(R.id.connected_count);
        requestCount = bottomNavigation.findViewById(R.id.request_count);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fUser = mAuth.getCurrentUser();
        updateUI(fUser,this);
        openFragment(getSupportFragmentManager().beginTransaction(), new ConnectedUsersFragment(), "Connected Users", false);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 5000);
            }
        }


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        fabAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlert();
            }
        });
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
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(getSupportFragmentManager().beginTransaction(), new ConnectedUsersFragment(), "Connected Users", false);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment("my_profile");
                openFragment(getSupportFragmentManager().beginTransaction(), fragment, activeUserName, false);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ContactFragment();
                openFragment(getSupportFragmentManager().beginTransaction(), fragment, "Contact Us", false);
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
                stopService();
            }
        });




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("STATUS");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String valid_until = dataSnapshot.child("DATE").getValue().toString();
                String value =dataSnapshot.child("VALUE").getValue().toString();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
                String finaldate = dateFormat.format(cal.getTime());
                Date today = new Date(finaldate);
                Date validuntil = new Date(valid_until);
                if (today.after(validuntil)) {
                    Toast.makeText(MainActivity.this, "invalid", Toast.LENGTH_SHORT).show();
                    Integer.parseInt(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void fetchAlertData() {
        DatabaseReference ref = database.getReference("Emergency").child(fUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean res = dataSnapshot.getValue(Boolean.class);
                if (res!=null && res){
                    alert = true;
                    fabAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.alertRed)));
                } else {
                    alert = false;
                    fabAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.alertGreen)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setAlert() {
        if (alert){
            alert = false;

            fabAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.alertGreen)));

            Toast.makeText(this, "Alert off", Toast.LENGTH_SHORT).show();
        } else {
            alert = true;

            fabAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.alertRed)));

            Toast.makeText(this, "Alert on", Toast.LENGTH_SHORT).show();
        }

        DatabaseReference ref = database.getReference("Emergency").child(fUser.getUid());

        ref.setValue(alert);
    }

    private void getRequestUserCount() {
        DatabaseReference myRef = database.getReference("User Requests");

        final List<RequestModel> requests = new ArrayList<>();

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == 5000 && resultCode == RESULT_OK) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission denied 'Access Location'", Toast.LENGTH_SHORT).show();
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

    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = null;
        if (cm != null) {
            wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
