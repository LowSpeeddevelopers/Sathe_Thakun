package com.nexttech.sathethakun.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nexttech.sathethakun.LoginAndRegisterHolder;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.R;

import java.util.Objects;

public class LoginFragment extends Fragment {
    Context context;
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin;
    TextView tvSignup,signup_up;

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



        signup_up=vi.findViewById(R.id.sign_up_tv_up);
        edtLoginEmail = vi.findViewById(R.id.edt_login_email);
        edtLoginPassword = vi.findViewById(R.id.edt_login_password);
        btnLogin = vi.findViewById(R.id.btn_login);
        tvSignup = vi.findViewById(R.id.tv_signup);

        btnLogin.setOnClickListener(view -> {
            email = edtLoginEmail.getText().toString();
            password = edtLoginPassword.getText().toString();

            if (TextUtils.isEmpty(email)){
                edtLoginEmail.setError("Field Empty!");
                edtLoginEmail.requestFocus();
                edtLoginEmail.setCursorVisible(true);
            }else if(TextUtils.isEmpty(password)){
                edtLoginPassword.setError("Field Empty!");
                edtLoginPassword.requestFocus();
                edtLoginPassword.setCursorVisible(true);
            } else {
                loginUser();
            }
        });

        tvSignup.setOnClickListener(view -> LoginAndRegisterHolder.updateFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction(),new RegisterFragment(context),"register"));

        signup_up.setOnClickListener(view -> LoginAndRegisterHolder.updateFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction(),new RegisterFragment(context),"register"));


        return vi;
    }


    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(((Activity)context), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        MainActivity.updateUI(user,context);
                    } else {
                        Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        MainActivity.updateUI(null,context);
                    }
                });
    }

}
