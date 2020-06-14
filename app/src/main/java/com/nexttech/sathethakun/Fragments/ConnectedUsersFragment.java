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
import com.nexttech.sathethakun.Adapter.ConnectedAdapter;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class ConnectedUsersFragment extends Fragment {

    private List<UserModel> userConnectedList;

    private ConnectedAdapter mAdapter;

    private FirebaseDatabase database;

    String myUid;


    public ConnectedUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connected_users, container, false);

        RecyclerView rvConnectedUsers = view.findViewById(R.id.rv_connected_users);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        myUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        MainActivity.progressBarVisible();

        userConnectedList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvConnectedUsers.setLayoutManager(mLayoutManager);
        rvConnectedUsers.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ConnectedAdapter(getContext(), userConnectedList);
        rvConnectedUsers.setAdapter(mAdapter);

        getConnectedList();

        return view;
    }

    private void getConnectedList() {
        DatabaseReference myRef = database.getReference("Connected").child(myUid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userConnectedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String id = snapshot.getValue(String.class);

                    assert id != null;
                    DatabaseReference myRef2 = database.getReference("Users").child(id);

                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);

                            userConnectedList.add(userModel);

                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    MainActivity.progressBarGone();
                }

                if(userConnectedList.isEmpty()){
                    MainActivity.progressBarGone();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
