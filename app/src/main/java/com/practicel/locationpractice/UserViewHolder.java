package com.practicel.locationpractice;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textUsername;
    private IRecylerItemClickListerner iRecylerItemClickListerner;

    public void setiRecylerItemClickListerner(IRecylerItemClickListerner iRecylerItemClickListerner) {
        this.iRecylerItemClickListerner = iRecylerItemClickListerner;
    }

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        textUsername = itemView.findViewById(R.id.text_user_email);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        iRecylerItemClickListerner.onClick(v,getAdapterPosition());
    }
}
