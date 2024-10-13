package com.example.whatsp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private final Context context;
    private final List<UserModel> userModelList;

    public UsersAdapter(Context context) {
        this.context = context;
        this.userModelList = new ArrayList<>();
    }

    public void add(UserModel userModel) {
        userModelList.add(userModel);
        notifyItemInserted(userModelList.size() - 1);
    }

    public void clear() {
        userModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final UserModel userModel = userModelList.get(position);

        if (userModel.getUserName() != null) {
            holder.name.setText(userModel.getUserName());
            holder.name.setContentDescription("User name: " + userModel.getUserName());
        } else {
            holder.name.setText("Unknown User");
        }

        if (userModel.getUserEmail() != null) {
            holder.email.setText(userModel.getUserEmail());
            holder.email.setContentDescription("User email: " + userModel.getUserEmail());
        } else {
            holder.email.setText("No email provided");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", userModel.getUserId());
                intent.putExtra("name", userModel.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public List<UserModel> getUserModelList() {
        return userModelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, email;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.useremail);
        }
    }
}
