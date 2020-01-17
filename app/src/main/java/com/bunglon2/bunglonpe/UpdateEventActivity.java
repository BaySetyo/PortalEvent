package com.bunglon2.bunglonpe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class UpdateEventActivity extends AppCompatActivity {

    ImageView iv_back, iv_pict_event;
    EditText EtNama, EtTanggal, Reglink, EtTempat, EtDeskripsi, Etkategori;
    FloatingActionButton fab;
    Integer photo_max = 1;
    Button updateEvent;
    Uri photo_location;
    public static final String EXTRA_EVENT = "extra_event";

    int mYear, mMonth, mDay, mHour, mMinute;

    DatabaseReference reference;
    StorageReference storage;
    String eventimage, namaevent;
    String storagepath = "PhotoEvents/";



    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
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
        updateEvent = findViewById(R.id.updateEvent);
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

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(UpdateEventActivity.this, MyEventAct.class);
                startActivity(gotohome);
                finish();
            }
        });

        Event event = getIntent().getParcelableExtra(EXTRA_EVENT);
        if ( event != null) {
            namaevent = event.getNama();
            eventimage = event.getUrl_photo_event();
            Picasso.get().load(event.getUrl_photo_event()).fit().centerCrop().into(iv_pict_event);
            EtNama.setText(event.getNama());
            EtTanggal.setText(event.getTanggal());
            Reglink.setText(event.getReglink());
            EtTempat.setText(event.getTempat());
            Etkategori.setText(event.getKategori());
            EtDeskripsi.setText(event.getDeskripsi());

        }

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent.setEnabled(false);
                updateEvent.setText("Loading . . .");
                deletePreviousImage();
            }
        });
    }


    private void deletePreviousImage() {
        storage = getInstance().getReferenceFromUrl(eventimage);
        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateEventActivity.this, "Previous Image Deleted...", Toast.LENGTH_SHORT).show();

                uploadNewImage();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void uploadNewImage() {
        storage = FirebaseStorage.getInstance().getReference().child("PhotoEvents");
        String imageName = System.currentTimeMillis()+ ".jpg";
        StorageReference storageReference1 =
                storage.child(imageName);

        Bitmap bitmap = ((BitmapDrawable) iv_pict_event.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // compress image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imagesBytes = baos.toByteArray();
        UploadTask uploadTask = storageReference1.putBytes(imagesBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(UpdateEventActivity.this, "New Image Uploaded...", Toast.LENGTH_SHORT).show();


                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();



                updateDatabase(downloadUri.toString());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UpdateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateDatabase(final String s) {

        final String nama = EtNama.getText().toString();
        final String tanggal = EtTanggal.getText().toString();
        final String reglink = Reglink.getText().toString();
        final String tempat = EtTempat.getText().toString();
        final String kategori = Etkategori.getText().toString();
        final String deskripsi = EtDeskripsi.getText().toString();
        reference = FirebaseDatabase.getInstance().getReference("Events");

        Query query = reference.orderByChild("nama").equalTo(namaevent);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {

                    ds.getRef().child("nama").setValue(nama);
                    ds.getRef().child("tanggal").setValue(tanggal);
                    ds.getRef().child("reglink").setValue(reglink);
                    ds.getRef().child("tempat").setValue(tempat);
                    ds.getRef().child("kategori").setValue(kategori);
                    ds.getRef().child("deskripsi").setValue(deskripsi);
                    ds.getRef().child("url_photo_event").setValue(s);
                }
                Toast.makeText(UpdateEventActivity.this, "Event Updated...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateEventActivity.this, MyEventAct.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void findPhoto() {
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
