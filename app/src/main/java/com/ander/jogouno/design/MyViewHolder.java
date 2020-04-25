package com.ander.jogouno.design;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ander.jogouno.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView carta;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        carta = itemView.findViewById(R.id.carta);
    }
}
