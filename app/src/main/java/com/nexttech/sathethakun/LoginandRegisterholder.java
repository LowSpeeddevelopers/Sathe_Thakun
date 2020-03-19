package com.nexttech.sathethakun;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.nexttech.sathethakun.Fragments.LoginFragment;

public class LoginandRegisterholder extends AppCompatActivity {

    FirebaseAuth mAuth;

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
            updateFragment(getSupportFragmentManager().beginTransaction(),new LoginFragment(LoginandRegisterholder.this),"login");
        }


    }
}
