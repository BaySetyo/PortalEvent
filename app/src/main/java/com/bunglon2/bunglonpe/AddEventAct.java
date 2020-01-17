package com.bunglon2.bunglonpe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddEventAct extends AppCompatActivity {
    ImageView iv_back, iv_pict_event;
    EditText EtNama, EtTanggal, Reglink, EtTempat, EtDeskripsi, Etkategori;
    FloatingActionButton fab;
    Integer photo_max = 1;
    Button submitEvent;
    Uri photo_location;

    int mYear, mMonth, mDay, mHour, mMinute;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getUsernameLocal();

        iv_back = findViewById(R.id.iv_back);
        fab = findViewById(R.id.add_event);
        EtNama = findViewById(R.id.EtNama);
        EtTanggal = findViewById(R.id.EtTanggal);
        EtTempat = findViewById(R.id.EtTempat);
        iv_pict_event = findViewById(R.id.iv_pict_event);
        Reglink = findViewById(R.id.reglink);
        Etkategori = findViewById(R.id.Etkategori);
        EtDeskripsi = findViewById(R.id.EtDeskripsi);
        submitEvent = findViewById(R.id.submitEvent);
        reference = FirebaseDatabase.getInstance().getReference("Events");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        EtTanggal.setFocusableInTouchMode(false);
        EtTanggal.setFocusable(false);
        EtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                showTimeDialog();
            }
        });


        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent.setEnabled(false);
                submitEvent.setText("Loading . . .");

                storage = FirebaseStorage.getInstance().getReference().child("PhotoEvents");

                if (photo_location !=null){
                    final StorageReference storageReference1 =
                            storage.child(System.currentTimeMillis()+ "." + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String eventID = reference.push().getKey();
                                            String uri_photo_event = uri.toString();
                                            String nama = EtNama.getText().toString();
                                            String tanggal = EtTanggal.getText().toString();
                                            String reglink = Reglink.getText().toString();
                                            String tempat = EtTempat.getText().toString();
                                            String kategori = Etkategori.getText().toString();
                                            String deskripsi = EtDeskripsi.getText().toString();
                                            String username = username_key_new;

                                            Event event = new Event(eventID, uri_photo_event, nama, tanggal, reglink, tempat, kategori, deskripsi, username);

                                            reference.child(eventID).setValue(event);


                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Intent gotomyevent = new Intent(AddEventAct.this, MyEventAct.class);
                                            startActivity(gotomyevent);
                                            finish();
                                        }
                                    });


                                }
                            });
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(AddEventAct.this, MyEventAct.class);
                startActivity(gotohome);
                finish();
            }
        });
    }

    private void showTimeDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        EtTanggal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
    public void  findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data!=null && data.getData() !=null)
        {
            photo_location = data.getData();
            Picasso.get().load(photo_location).centerCrop().fit().into(iv_pict_event);

        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
