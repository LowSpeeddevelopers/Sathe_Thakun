package com.nexttech.sathethakun.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.Model.RequestModel;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUsersFragment extends Fragment {

    private EditText edtUserSearch;
    private ImageView ivProfilePic;
    private TextInputEditText edtName, edtAge, edtEmail, edtPhoneNumber, edtAddress;
    private Button btnSendRequest, btnCancelRequest;

    private ScrollView layoutUserInfo;
    private LinearLayout layoutUserNoInfo;
    private TextView tvNotFoundMsg;

    private FirebaseDatabase database;

    private String myUID, searchUID, searchKey;


    public AddUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_users, container, false);

        edtUserSearch = view.findViewById(R.id.edt_user_search);
        ImageView btnSearch = view.findViewById(R.id.btn_search);
        ivProfilePic = view.findViewById(R.id.iv_profile_pic);
        edtName = view.findViewById(R.id.edt_name);
        edtAge = view.findViewById(R.id.edt_age);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhoneNumber = view.findViewById(R.id.edt_phone_number);
        edtAddress = view.findViewById(R.id.edt_address);
        btnSendRequest = view.findViewById(R.id.btn_send_request);
        btnCancelRequest = view.findViewById(R.id.btn_cancel_request);

        layoutUserInfo = view.findViewById(R.id.layout_user_info);
        layoutUserNoInfo = view.findViewById(R.id.layout_user_no_info);
        tvNotFoundMsg = view.findViewById(R.id.tv_not_found_msg);

        database = FirebaseDatabase.getInstance();

        myUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        btnSearch.setOnClickListener(view1 -> {
            String mobile = edtUserSearch.getText().toString().trim();

            if (mobile.length() != 11){
                edtUserSearch.setError("Mobile number must have 11 digits");
                edtUserSearch.requestFocus();
            } else if (!(mobile.startsWith("017") || mobile.startsWith("013") || mobile.startsWith("014") || mobile.startsWith("016") || mobile.startsWith("018") || mobile.startsWith("019") || mobile.startsWith("015"))) {
                edtUserSearch.setError("Invalid mobile number");
                edtUserSearch.requestFocus();
            } else {
                MainActivity.progressBarVisible();
                searchData(mobile);
            }
        });

        btnSendRequest.setOnClickListener(view12 -> sentRequest());

        btnCancelRequest.setOnClickListener(view13 -> cancelRequest());

        return view;
    }

    private void cancelRequest() {
        DatabaseReference myRef = database.getReference("User Requests").child(searchKey);

        myRef.removeValue();

        btnCancelRequest.setVisibility(View.GONE);
        btnSendRequest.setVisibility(View.VISIBLE);

        Toast.makeText(getContext(), "Request Cancel", Toast.LENGTH_SHORT).show();
    }

    private void sentRequest() {

        DatabaseReference myRef = database.getReference("User Requests");

        RequestModel requestModel = new RequestModel(myUID, searchUID);

        myRef.push().setValue(requestModel);

        btnCancelRequest.setVisibility(View.VISIBLE);
        btnSendRequest.setVisibility(View.GONE);

        Toast.makeText(getContext(), "Request Send", Toast.LENGTH_SHORT).show();

    }

    private void searchData(String mobile) {
        DatabaseReference myRef = database.getReference("Search User").child(mobile);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getValue(String.class);

                hideKeyboard(Objects.requireNonNull(getActivity()));

                searchUID = userId;

                if (userId == null){
                    tvNotFoundMsg.setText("Data not found");
                    layoutUserInfo.setVisibility(View.GONE);
                    layoutUserNoInfo.setVisibility(View.VISIBLE);
                } else if (userId.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
                    tvNotFoundMsg.setText("Can't search own data");
                    layoutUserInfo.setVisibility(View.GONE);
                    layoutUserNoInfo.setVisibility(View.VISIBLE);
                } else {
                    checkRequestList(userId);
                    searchUserData(userId);
                    layoutUserInfo.setVisibility(View.VISIBLE);
                    layoutUserNoInfo.setVisibility(View.GONE);
                }

                MainActivity.progressBarGone();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value

                tvNotFoundMsg.setText("Data not found");
                layoutUserInfo.setVisibility(View.GONE);
                layoutUserNoInfo.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkRequestList(String userId) {
        DatabaseReference myRef = database.getReference("User Requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestModel requestModel = snapshot.getValue(RequestModel.class);

                    assert requestModel != null;
                    if ((requestModel.getReceiver().equals(userId) && requestModel.getSender().equals(myUID)) ||
                            (requestModel.getSender().equals(userId) && requestModel.getReceiver().equals(myUID))){
                        searchKey = snapshot.getKey();
                        btnCancelRequest.setVisibility(View.VISIBLE);
                        btnSendRequest.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUserData(String userId) {
        DatabaseReference myRef = database.getReference("Users").child(userId);

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
                    ivProfilePic.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(Objects.requireNonNull(getContext())).load(userModel.getImageUri()).into(ivProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
