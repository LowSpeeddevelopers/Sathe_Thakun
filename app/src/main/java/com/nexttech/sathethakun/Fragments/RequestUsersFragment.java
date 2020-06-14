package com.nexttech.sathethakun.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexttech.sathethakun.Adapter.RequestAdapter;
import com.nexttech.sathethakun.Model.RequestModel;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestUsersFragment extends Fragment {

    private List<UserModel> userRequestList;
    private List<String> requestKey, requestUIDs;
    private RequestAdapter mAdapter;

    private FirebaseDatabase database;

    String myUid;

    public RequestUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_users, container, false);

        RecyclerView rvRequestUsers = view.findViewById(R.id.rv_request_users);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        myUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        userRequestList = new ArrayList<>();
        requestKey = new ArrayList<>();
        requestUIDs = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvRequestUsers.setLayoutManager(mLayoutManager);
        rvRequestUsers.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RequestAdapter(getContext(), userRequestList, requestKey, requestUIDs);
        rvRequestUsers.setAdapter(mAdapter);


        getRequestList();

        return view;
    }

    private void getRequestList() {

        DatabaseReference myRef = database.getReference("User Requests");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userRequestList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestModel requestModel = snapshot.getValue(RequestModel.class);

                    assert requestModel != null;
                    if (requestModel.getReceiver().equals(myUid)){
                        DatabaseReference myRef2 = database.getReference("Users").child(requestModel.getSender());

                        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                                requestKey.add(snapshot.getKey());
                                requestUIDs.add(requestModel.getSender());
                                userRequestList.add(userModel);

                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
