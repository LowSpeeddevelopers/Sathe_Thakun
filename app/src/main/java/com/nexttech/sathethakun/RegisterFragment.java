package com.nexttech.sathethakun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexttech.sathethakun.Model.UserModel;

public class RegisterFragment extends Fragment {
    Context context;
    public RegisterFragment(Context context){
        this.context = context;
    }

    EditText edtSignupName, edtSignupEmail, edtSignupPhone, edtSignupAge, edtSignupAddress, edtSignupPassword, edtSignupRetypePassword;
    Button btnSignup;
    TextView tv_Signin;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    String name, email, phone, age, address, password, retypePassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        View vi;


        vi = inflater.inflate(R.layout.fragment_register,container,false);



        tv_Signin=vi.findViewById(R.id.tv_sign_in);
        edtSignupName = vi.findViewById(R.id.edt_signup_name);
        edtSignupEmail = vi.findViewById(R.id.edt_signup_email);
        edtSignupPassword = vi.findViewById(R.id.edt_signup_password);
        edtSignupRetypePassword = vi.findViewById(R.id.edt_signup_retype_password);
        edtSignupPhone = vi.findViewById(R.id.edt_signup_phone);
        edtSignupAge = vi.findViewById(R.id.edt_signup_age);
        edtSignupAddress = vi.findViewById(R.id.edt_signup_address);

        btnSignup = vi.findViewById(R.id.btn_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtSignupName.getText().toString();
                email = edtSignupEmail.getText().toString();
                phone = edtSignupPhone.getText().toString();
                age = edtSignupAge.getText().toString();
                address = edtSignupAddress.getText().toString();
                password = edtSignupPassword.getText().toString();
                retypePassword = edtSignupRetypePassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || age.isEmpty() || address.isEmpty() || password.isEmpty() || retypePassword.isEmpty()){
                    Toast.makeText(context, "Input Data", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(retypePassword)){
                    Toast.makeText(context, "Password & Retype Password not same.", Toast.LENGTH_SHORT).show();
                } else {
                    createUser();
                }
            }
        });

        tv_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginandRegisterholder.updateFragment(getFragmentManager().beginTransaction(),new LoginFragment(context),context);
            }
        });



        return vi;

    }

    private void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(((Activity)context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            insertIntoDatabase(user.getUid());
                            MainActivity.updateUI(user,context);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.e("reasone",task.getException().toString());
                            MainActivity.updateUI(null,context);
                        }

                        // ...
                    }
                });
    }

    private void insertIntoDatabase(String uid) {

        DatabaseReference myRef = database.getReference("Users").child(uid);

        UserModel userModel = new UserModel(name, email, phone, age, address);

        myRef.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"successful",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
