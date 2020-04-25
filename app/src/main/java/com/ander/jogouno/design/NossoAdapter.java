package com.ander.jogouno.design;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ander.jogouno.R;
import com.ander.jogouno.jogo.Carta;

import java.util.List;

public class NossoAdapter extends RecyclerView.Adapter {

    private List<Carta> cartas;
    private Context context;

    public NossoAdapter(List<Carta> cartas, Context context) {
        this.cartas = cartas;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.linha_carta, viewGroup,false);

        MyViewHolder holder = new MyViewHolder(view);


        //holder.escudo.set(club.getName());


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        MyViewHolder holder = (MyViewHolder) viewHolder;

        Carta cartasManipular = cartas.get(position);
        holder.carta.setImageResource(cartasManipular.getImagemCarta());

        ((MyViewHolder) viewHolder).carta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                    Toast.makeText(context, "Clicou na Carta", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartas.size();
    }
}
