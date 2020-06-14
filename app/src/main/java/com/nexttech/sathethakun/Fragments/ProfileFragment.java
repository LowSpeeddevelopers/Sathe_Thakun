package com.nexttech.sathethakun.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView profileImage;
    private CardView editProfilePicture;
    private TextInputEditText edtName, edtAge, edtEmail, edtPhoneNumber, edtAddress;
    private LinearLayout requestProfile;
    private LinearLayout connectedProfile;
    private Button btnEdit;
    private Button btnUpdate;

    private String requestFor, myUID, requestKeys, requestId, connectedId;

    private FirebaseDatabase database;

    private Uri mImageUri;
    private StorageReference storageRef;

    private String myImageUrl;

    private final int IMAGE_REQUEST = 33;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(String requestFor, String requestId, String requestKeys) {
        this.requestFor = requestFor;
        this.requestKeys = requestKeys;
        this.requestId = requestId;
    }

    public ProfileFragment(String requestFor, String connectedId) {
        this.requestFor = requestFor;
        this.connectedId = connectedId;
    }

    public ProfileFragment(String requestFor) {
        this.requestFor = requestFor;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profileimage);
        editProfilePicture = view.findViewById(R.id.editprofilepicture);
        edtName = view.findViewById(R.id.edt_name);
        edtAge = view.findViewById(R.id.edt_age);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_number);
        edtAddress = view.findViewById(R.id.edt_address);
        requestProfile = view.findViewById(R.id.request_profile);
        connectedProfile = view.findViewById(R.id.connected_profile);
        LinearLayout myProfile = view.findViewById(R.id.my_profile);
        Button btnAccept = view.findViewById(R.id.btn_accept);
        Button btnDelete = view.findViewById(R.id.btn_delete);
        Button btnRemove = view.findViewById(R.id.btn_remove);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnUpdate = view.findViewById(R.id.btn_update);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("Uploads");

        myUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        MainActivity.progressBarVisible();

        switch (requestFor) {
            case "request_profile":

                requestProfile.setVisibility(View.VISIBLE);
                editProfilePicture.setVisibility(View.GONE);

                getRequestUserProfile(requestId);
                break;
            case "connected_profile":

                connectedProfile.setVisibility(View.VISIBLE);
                editProfilePicture.setVisibility(View.GONE);

                getRequestUserProfile(connectedId);
                break;
            case "my_profile":

                myProfile.setVisibility(View.VISIBLE);
                editProfilePicture.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);

                getRequestUserProfile(myUID);
                break;
        }

        btnAccept.setOnClickListener(view1 -> {
            DatabaseReference reference = database.getReference("Connected");

            reference.child(myUID).child(requestId).setValue(requestId);
            reference.child(requestId).child(myUID).setValue(myUID);

            DatabaseReference reference2 = database.getReference("User Requests").child(requestKeys);

            reference2.removeValue();

            requestProfile.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(view12 -> {
            DatabaseReference reference = database.getReference("User Requests").child(requestKeys);

            reference.removeValue();

            requestProfile.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
        });

        btnRemove.setOnClickListener(view13 -> {
            DatabaseReference reference = database.getReference("Connected");

            reference.child(myUID).child(connectedId).removeValue();
            reference.child(connectedId).child(myUID).removeValue();

            connectedProfile.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
        });

        editProfilePicture.setOnClickListener(view14 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), IMAGE_REQUEST);
        });

        btnEdit.setOnClickListener(view15 -> {
            editProfilePicture.setVisibility(View.VISIBLE);

            edtName.setEnabled(true);
            edtAge.setEnabled(true);
            edtEmail.setEnabled(true);
            edtPhoneNumber.setEnabled(true);
            edtAddress.setEnabled(true);

            btnEdit.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

        btnUpdate.setOnClickListener(view16 -> {
            String name, email, phone, age, address;
            name = edtName.getText().toString();
            email = edtEmail.getText().toString();
            phone = edtPhoneNumber.getText().toString();
            age = edtAge.getText().toString();
            address = edtAddress.getText().toString();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || age.isEmpty() || address.isEmpty()){
                Toast.makeText(getContext(), "Input Data", Toast.LENGTH_SHORT).show();
            } else if (phone.length() != 11){
                edtPhoneNumber.setError("Mobile number must have 11 digits");
                edtPhoneNumber.requestFocus();
            } else if (!(phone.startsWith("017") || phone.startsWith("013") || phone.startsWith("014") || phone.startsWith("016") || phone.startsWith("018") || phone.startsWith("019") || phone.startsWith("015"))) {
                edtPhoneNumber.setError("Invalid mobile number");
                edtPhoneNumber.requestFocus();
            } else {
                UserModel userModel = new UserModel(myUID, name, email, phone, age, address, myImageUrl);
                updateProfile(userModel);
            }
        });

        return view;
    }

    private void updateProfile(UserModel userModel) {

        DatabaseReference myRef = database.getReference("Users").child(myUID);

        myRef.setValue(userModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DatabaseReference myRef2 = database.getReference("Search User").child(userModel.getPhone());
                myRef2.setValue(myUID);

                editProfilePicture.setVisibility(View.GONE);

                edtName.setEnabled(false);
                edtAge.setEnabled(false);
                edtEmail.setEnabled(false);
                edtPhoneNumber.setEnabled(false);
                edtAddress.setEnabled(false);

                btnEdit.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.GONE);
            }else {
                Toast.makeText(getContext(),"unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRequestUserProfile(String user_id) {
        DatabaseReference myRef = database.getReference("Users").child(user_id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                assert userModel != null;
                edtName.setText(userModel.getName());
                edtAge.setText(userModel.getAge());
                edtEmail.setText(userModel.getEmail());
                edtPhoneNumber.setText(userModel.getPhone());
                edtAddress.setText(userModel.getAddress());

                if (userModel.getImageUri().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                    myImageUrl = "default";
                } else {
                    Glide.with(Objects.requireNonNull(getContext())).load(userModel.getImageUri()).into(profileImage);
                    myImageUrl = userModel.getImageUri();
                }

                MainActivity.progressBarGone();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), mImageUri);
                profileImage.setImageBitmap(bitmap);

                uploadImage();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Something went wrong!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (mImageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageRef.child("profile_pic/" + UUID.randomUUID().toString());

            ref.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();

                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    myImageUrl= uri.toString();

                    Glide.with(Objects.requireNonNull(getContext())).load(myImageUrl).into(profileImage);
                });

                Toast.makeText(getContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int)progress + "%");
            });
        }
    }
}
