package com.nexttech.sathethakun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexttech.sathethakun.Fragments.ProfileFragment;
import com.nexttech.sathethakun.MainActivity;
import com.nexttech.sathethakun.Model.UserModel;
import com.nexttech.sathethakun.R;

import java.util.List;
import java.util.Objects;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    List<UserModel> requestUsers;
    List<String> requestKeys;
    List<String> requestUIDs;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String myUID;

    public RequestAdapter(Context context, List<UserModel> requestUsers, List<String> requestKeys, List<String> requestUIDs) {
        this.context = context;
        this.requestUsers = requestUsers;
        this.requestKeys = requestKeys;
        this.requestUIDs = requestUIDs;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_users, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel model = requestUsers.get(position);

        holder.itemBtn.setVisibility(View.VISIBLE);
        holder.itemMsg.setVisibility(View.GONE);

        holder.itemUserName.setText(model.getName());
        holder.itemUserMobileNumber.setText(model.getPhone());

        if (model.getImageUri().equals("default")){
            holder.itemProfilePic.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(model.getImageUri()).into(holder.itemProfilePic);
        }

        holder.itemRequestAccept.setOnClickListener(view -> {
            DatabaseReference reference = database.getReference("Connected");

            String id = requestUIDs.get(position);

            reference.child(myUID).child(id).setValue(id);
            reference.child(id).child(myUID).setValue(myUID);

            DatabaseReference reference2 = database.getReference("User Requests").child(requestKeys.get(position));

            reference2.removeValue();

            holder.itemBtn.setVisibility(View.GONE);
            holder.itemMsg.setText("Request Accepted");
            holder.itemMsg.setVisibility(View.VISIBLE);
        });

        holder.itemRequestDelete.setOnClickListener(view -> {
            DatabaseReference reference = database.getReference("User Requests").child(requestKeys.get(position));

            reference.removeValue();

            holder.itemBtn.setVisibility(View.GONE);
            holder.itemMsg.setText("Request Deleted");
            holder.itemMsg.setVisibility(View.VISIBLE);
        });

        holder.itemView.setOnClickListener(view -> {

            Fragment fragment = new ProfileFragment("request_profile", model.getUserID(), requestKeys.get(position));

            MainActivity.openFragment(((FragmentActivity)context).getSupportFragmentManager().beginTransaction(), fragment, model.getName(), true);
        });
    }

    @Override
    public int getItemCount() {
        return requestUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemProfilePic;
        TextView itemUserName, itemUserMobileNumber, itemMsg;
        Button itemRequestAccept, itemRequestDelete;
        LinearLayout itemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemProfilePic = itemView.findViewById(R.id.item_profile_pic);
            itemUserName = itemView.findViewById(R.id.item_user_name);
            itemUserMobileNumber = itemView.findViewById(R.id.item_user_mobile_number);
            itemMsg = itemView.findViewById(R.id.item_msg);
            itemRequestAccept = itemView.findViewById(R.id.item_request_accept);
            itemRequestDelete = itemView.findViewById(R.id.item_request_delete);
            itemBtn = itemView.findViewById(R.id.item_btn);
        }
    }
}
