package com.nexttech.sathethakun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.ProfileActivity;
import com.nexttech.sathethakun.R;

import java.util.List;
import java.util.Objects;

public class ConnectedAdapter extends RecyclerView.Adapter<ConnectedAdapter.ViewHolder> {

    Context context;
    List<UserModel> requestUsers;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String myUID;

    public ConnectedAdapter(Context context, List<UserModel> requestUsers) {
        this.context = context;
        this.requestUsers = requestUsers;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_connected_users, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel model = requestUsers.get(position);

        holder.itemUserName.setText(model.getName());
        holder.itemUserMobileNumber.setText(model.getPhone());

        holder.itemProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProfileActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemProfilePic;
        TextView itemUserName, itemUserMobileNumber, itemMsg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemProfilePic = itemView.findViewById(R.id.item_profile_pic);
            itemUserName = itemView.findViewById(R.id.item_user_name);
            itemUserMobileNumber = itemView.findViewById(R.id.item_user_mobile_number);
            itemMsg = itemView.findViewById(R.id.item_msg);




        }
    }
}
