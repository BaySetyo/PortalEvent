package com.bunglon2.bunglonpe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEventAct extends AppCompatActivity {
    FloatingActionButton fab;
    ImageView iv_back;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Event> list;
    MyEventAdapter adapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        getUsernameLocal();
        fab = findViewById(R.id.add_event);
        iv_back = findViewById(R.id.iv_back);

        recyclerView = findViewById(R.id.rvMyEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Event>();

        reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.orderByChild("username").equalTo(username_key_new).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Event e = dataSnapshot1.getValue(Event.class);
                    list.add(e);
                }
                adapter = new MyEventAdapter (MyEventAct.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoaddevent = new Intent(MyEventAct.this, AddEventAct.class);
                startActivity(gotoaddevent);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(MyEventAct.this, HomeActivity.class);
                startActivity(gotohome);
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
