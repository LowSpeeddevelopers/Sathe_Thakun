package com.nexttech.sathethakun;


import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginandRegisterholder extends AppCompatActivity {
    FrameLayout container;
    Fragment LoginFragment;
    Fragment RegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginand_registerholder);


        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container,LoginFragment);
        fragmentTransaction.commit();

    }
}
