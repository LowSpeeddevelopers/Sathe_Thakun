package com.nexttech.sathethakun.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nexttech.sathethakun.LoginandRegisterholder;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.R;

public class LoginFragment extends Fragment {
    Context context;
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin;
    TextView tvSignup;

    private FirebaseAuth mAuth;

    String email, password;

    public LoginFragment(Context context){
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        View vi = inflater.inflate(R.layout.fragment_login,container,false);




        edtLoginEmail = vi.findViewById(R.id.edt_login_email);
        edtLoginPassword = vi.findViewById(R.id.edt_login_password);
        btnLogin = vi.findViewById(R.id.btn_login);
        tvSignup = vi.findViewById(R.id.tv_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtLoginEmail.getText().toString();
                password = edtLoginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Input Data", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser();
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginandRegisterholder.updateFragment(getFragmentManager().beginTransaction(),new RegisterFragment(context),"register");
            }
        });


        return vi;
    }


    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(((Activity)context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.updateUI(user,context);
                        } else {
                            Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            MainActivity.updateUI(null,context);
                        }
                    }
                });
    }

}
