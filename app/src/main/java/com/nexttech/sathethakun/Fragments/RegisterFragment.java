package com.nexttech.sathethakun.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nexttech.sathethakun.LoginAndRegisterHolder;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {
    Context context;
    public RegisterFragment(Context context){
        this.context = context;
    }

    EditText edtSignUpName, edtSignUpEmail, edtSignUpPhone, edtSignUpAge, edtSignUpAddress, edtSignUpPassword, edtSignUpRetypePassword;
    Button btnSignUp;
    TextView tv_SignIn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    String name, email, phone, age, address, password, retypePassword;

    CircleImageView profileImage;
    CardView editProfilePic;

    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;

    String myImageUrl = "default";

    private final int PICK_IMAGE_REQUEST = 22;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("Uploads");

        View vi;

        vi = inflater.inflate(R.layout.fragment_register,container,false);

        tv_SignIn =vi.findViewById(R.id.tv_sign_in);
        edtSignUpName = vi.findViewById(R.id.edt_signup_name);
        edtSignUpEmail = vi.findViewById(R.id.edt_signup_email);
        edtSignUpPassword = vi.findViewById(R.id.edt_signup_password);
        edtSignUpRetypePassword = vi.findViewById(R.id.edt_signup_retype_password);
        edtSignUpPhone = vi.findViewById(R.id.edt_signup_phone);
        edtSignUpAge = vi.findViewById(R.id.edt_signup_age);
        edtSignUpAddress = vi.findViewById(R.id.edt_signup_address);

        profileImage = vi.findViewById(R.id.profileimage);
        editProfilePic = vi.findViewById(R.id.editprofilepicture);

        btnSignUp = vi.findViewById(R.id.btn_signup);

        editProfilePic.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        });

        btnSignUp.setOnClickListener(view -> {
            name = edtSignUpName.getText().toString();
            email = edtSignUpEmail.getText().toString();
            phone = edtSignUpPhone.getText().toString();
            age = edtSignUpAge.getText().toString();
            address = edtSignUpAddress.getText().toString();
            password = edtSignUpPassword.getText().toString();
            retypePassword = edtSignUpRetypePassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || age.isEmpty() || address.isEmpty() || password.isEmpty() || retypePassword.isEmpty()){
                Toast.makeText(context, "Input Data", Toast.LENGTH_SHORT).show();
            } else if (phone.length() != 11){
                edtSignUpPhone.setError("Mobile number must have 11 digits");
                edtSignUpPhone.requestFocus();
            } else if (!(phone.startsWith("017") || phone.startsWith("013") || phone.startsWith("014") || phone.startsWith("016") || phone.startsWith("018") || phone.startsWith("019") || phone.startsWith("015"))) {
                edtSignUpPhone.setError("Invalid mobile number");
                edtSignUpPhone.requestFocus();
            } else if (!password.equals(retypePassword)){
                Toast.makeText(context, "Password & Retype Password not same.", Toast.LENGTH_SHORT).show();
            } else {
                createUser();
            }
        });

        tv_SignIn.setOnClickListener(view -> LoginAndRegisterHolder.updateFragment(Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction(),new LoginFragment(context),"signup"));



        return vi;

    }

    private void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(((Activity)context), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        insertIntoDatabase(user.getUid());
                        MainActivity.updateUI(user,context);


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        MainActivity.updateUI(null,context);
                    }

                    // ...
                });
    }

    private void insertIntoDatabase(String uid) {

        DatabaseReference myRef = database.getReference("Users").child(uid);

        UserModel userModel = new UserModel(uid, name, email, phone, age, address, myImageUrl);

        myRef.setValue(userModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DatabaseReference myRef2 = database.getReference("Search User").child(phone);
                myRef2.setValue(uid);

                DatabaseReference ref = database.getReference("Emergency").child(uid);

                ref.setValue(false);

                Toast.makeText(context,"successful",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context,"unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageUri);
                profileImage.setImageBitmap(bitmap);

                uploadImage();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (mImageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageRef.child("profile_pic/" + UUID.randomUUID().toString());

            ref.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();

                ref.getDownloadUrl().addOnSuccessListener(uri -> myImageUrl= uri.toString());

                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int)progress + "%");
            });
        }
    }
}
