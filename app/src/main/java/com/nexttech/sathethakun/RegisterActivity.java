package com.nexttech.sathethakun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexttech.sathethakun.Model.UserModel;

public class RegisterActivity extends AppCompatActivity {

    EditText edtSignupName, edtSignupEmail, edtSignupPhone, edtSignupAge, edtSignupAddress, edtSignupPassword, edtSignupRetypePassword;
    Button btnSignup;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    String name, email, phone, age, address, password, retypePassword;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        edtSignupName = findViewById(R.id.edt_signup_name);
        edtSignupEmail = findViewById(R.id.edt_signup_email);
        edtSignupPassword = findViewById(R.id.edt_signup_password);
        edtSignupRetypePassword = findViewById(R.id.edt_signup_retype_password);
        edtSignupPhone = findViewById(R.id.edt_signup_phone);
        edtSignupAge = findViewById(R.id.edt_signup_age);
        edtSignupAddress = findViewById(R.id.edt_signup_address);

        btnSignup = findViewById(R.id.btn_signup);

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
                    Toast.makeText(RegisterActivity.this, "Input Data", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(retypePassword)){
                    Toast.makeText(RegisterActivity.this, "Password & Retype Password not same.", Toast.LENGTH_SHORT).show();
                } else {
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            insertIntoDatabase(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void insertIntoDatabase(String uid) {

        DatabaseReference myRef = database.getReference("Users").child(uid);

        UserModel userModel = new UserModel(name, email, phone, age, address);

        myRef.setValue(userModel);
    }
}
