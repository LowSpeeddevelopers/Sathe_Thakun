package com.nexttech.sathethakun;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.nexttech.sathethakun.Fragments.LoginFragment;

public class LoginAndRegisterHolder extends AppCompatActivity {

    FirebaseAuth mAuth;

    AlertDialog.Builder builder;

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasConnection()) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check your internet connection.").setTitle("No Internet");
            builder.setMessage("Please check your internet connection.")
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, id) -> finish());
            AlertDialog alert = builder.create();
            alert.setTitle("No Internet");
            alert.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginand_registerholder);
        mAuth = FirebaseAuth.getInstance();



        updateFragment(getSupportFragmentManager().beginTransaction(),new LoginFragment(this),"login");
    }

    public static void updateFragment(FragmentTransaction transaction,Fragment fragment,String tag){
        transaction.replace(R.id.container,fragment,tag);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        LoginFragment test = (LoginFragment) getSupportFragmentManager().findFragmentByTag("login");
        if(test!=null && test.isVisible()){
            super.onBackPressed();
        }else {
            updateFragment(getSupportFragmentManager().beginTransaction(),new LoginFragment(LoginAndRegisterHolder.this),"login");
        }

    }

    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
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
