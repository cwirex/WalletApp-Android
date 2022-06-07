package com.example.walletapp.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walletapp.R;

import java.util.List;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.ViewHolder> {

    private List<GroupUser> users;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    GroupUserAdapter(Context context, List<GroupUser> users) {
        this.mInflater = LayoutInflater.from(context);
        this.users = users;
    }

    // inflates the row layout from xml
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupUser user = users.get(position);
        holder.myTextView.setText(user.name);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_item_user);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onUserItemClick(view, getAdapterPosition());
        }
    }

    public GroupUser getItem(int id) {
        return users.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onUserItemClick(View view, int position);
    }
}
