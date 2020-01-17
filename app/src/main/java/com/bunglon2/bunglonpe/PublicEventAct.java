package com.bunglon2.bunglonpe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PublicEventAct extends AppCompatActivity {

    ImageView iv_back;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Event> list;
    PublicEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_event);

        iv_back = findViewById(R.id.iv_back);

        recyclerView = findViewById(R.id.rvPublicEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Event>();

        reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Event e = dataSnapshot1.getValue(Event.class);
                    list.add(e);
                }
                adapter = new PublicEventAdapter(PublicEventAct.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(PublicEventAct.this, HomeActivity.class);
                startActivity(gotohome);
                finish();
            }
        });
    }
}
