package com.nexttech.sathethakun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class MainActivity extends AppCompatActivity {
    Button btnStartService, btnStopService;

    CardView btnLogout;

    TextView userNmae, userPhone;

    BottomNavigationView bottomNavigation;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser fUser;

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


        //startActivity(new Intent(this,RegisterActivity.class));
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fUser = mAuth.getCurrentUser();

        updateUI(fUser,this);

        userNmae = findViewById(R.id.tv_user_name);
        userPhone = findViewById(R.id.tv_user_phone);

        btnLogout = findViewById(R.id.button_logout);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationItemSelectedListener);
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

                userNmae.setText(userModel.getName());
                userPhone.setText(userModel.getPhone());
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

    BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_connected:
                            openFragment(new ConnectedUsersFragment());
                            return true;
                        case R.id.navigation_add:
                            openFragment(new AddUsersFragment());
                            return true;
                        case R.id.navigation_request:
                            openFragment(new RequestUsersFragment());
                            return true;
                    }
                    return false;
                }
            };


}
