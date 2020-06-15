package com.practicel.locationpractice;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textUsername;
    public Button accept, decline;
    private IRecylerItemClickListerner iRecylerItemClickListerner;

    public void setiRecylerItemClickListerner(IRecylerItemClickListerner iRecylerItemClickListerner) {
        this.iRecylerItemClickListerner = iRecylerItemClickListerner;
    }

    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        textUsername = itemView.findViewById(R.id.text_user_email);
        accept = itemView.findViewById(R.id.acceptButton);
        decline = itemView.findViewById(R.id.declineButton);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        iRecylerItemClickListerner.onClick(v, getAdapterPosition());
    }


}