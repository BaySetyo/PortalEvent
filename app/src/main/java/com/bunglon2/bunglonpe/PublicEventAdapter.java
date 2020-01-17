package com.bunglon2.bunglonpe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PublicEventAdapter extends RecyclerView.Adapter<PublicEventAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> events;

    public PublicEventAdapter(Context c, ArrayList<Event> e)
    {
        context = c;
        events = e;
    }
    @NonNull
    @Override
    public PublicEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicEventAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PublicEventAdapter.MyViewHolder holder, final int position) {
        holder.NameEvent.setText(events.get(position).getNama());
        holder.Date.setText(events.get(position).getTanggal());
        holder.Location.setText(events.get(position).getTempat());
        holder.Link.setText(events.get(position).getReglink());
        holder.Category.setText(events.get(position).getKategori());
        Picasso.get().load(events.get(position).getUrl_photo_event()).fit().centerCrop().into(holder.EvFoto);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailActivity = new Intent(context, DetailEventActivity.class);
                detailActivity.putExtra(DetailEventActivity.EXTRA_EVENT, events.get(position));
                context.startActivity(detailActivity);
            }
        });


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView EvFoto;
        TextView NameEvent, Date, Location, Link, Category;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            EvFoto = itemView.findViewById(R.id.EvFoto);
            NameEvent = itemView.findViewById(R.id.NameEvent);
            Date = itemView.findViewById(R.id.Date);
            Location = itemView.findViewById(R.id.Location);
            Link = itemView.findViewById(R.id.Link);
            Category = itemView.findViewById(R.id.Category);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
