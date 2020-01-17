package com.bunglon2.bunglonpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailEventActivity extends AppCompatActivity {

    ImageView IvGambar, iv_kembali;
    TextView jdevent, tanggal, reglink, tempat, kategori, deskripsi;

    public static final String EXTRA_EVENT = "extra_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        IvGambar = findViewById(R.id.IvGambar);
        iv_kembali = findViewById(R.id.iv_back);
        jdevent = findViewById(R.id.jdevent);
        tanggal = findViewById(R.id.tanggal);
        reglink = findViewById(R.id.reglink);
        tempat = findViewById(R.id.tempat);
        kategori = findViewById(R.id.kategori);
        deskripsi = findViewById(R.id.deskripsi);

        Event event = getIntent().getParcelableExtra(EXTRA_EVENT);
        Picasso.get().load(event.getUrl_photo_event()).fit().centerCrop().into(IvGambar);
        jdevent.setText(event.getNama());
        tanggal.setText(event.getTanggal());
        reglink.setText(event.getReglink());
        tempat.setText(event.getTempat());
        kategori.setText(event.getKategori());
        deskripsi.setText(event.getDeskripsi());


        iv_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyevent = new Intent(DetailEventActivity.this, PublicEventAct.class);
                startActivity(gotomyevent);
                finish();
            }
        });




    }


}
