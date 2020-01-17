package com.bunglon2.bunglonpe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> events;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");;

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
    public MyEventAdapter(Context c, ArrayList<Event> e)
    {
        context = c;
        events = e;
    }
    @NonNull
    @Override
    public MyEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyEventAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventAdapter.MyViewHolder holder, final int position) {

        holder.NameEvent.setText(events.get(position).getNama());
        holder.Date.setText(events.get(position).getTanggal());
        holder.Location.setText(events.get(position).getTempat());
        holder.Link.setText(events.get(position).getReglink());
        holder.Category.setText(events.get(position).getKategori());
        Picasso.get().load(events.get(position).getUrl_photo_event()).fit().centerCrop().into(holder.EvFoto);


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String eventid = events.get(position).geteventID();
                final String gambar = events.get(position).getUrl_photo_event();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] options = {" Update"," Delete"};

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Update Clicked
                            Intent intent = new Intent(context, UpdateEventActivity.class);
                            intent.putExtra(DetailEventActivity.EXTRA_EVENT, events.get(position));
                            context.startActivity(intent);
                        }
                        if (which == 1) {
                            ShowDeleteDataDialog(eventid, gambar);
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private void ShowDeleteDataDialog(final String eventid, final String gambar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure went to delete?");
        builder.setMessage("You will delete all event data");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = reference.orderByChild("eventID").equalTo(eventid);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(context, "Event deleted successfully",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });


                StorageReference storage = getInstance().getReferenceFromUrl(gambar);
                storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(context, "Event image deleted successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();
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
