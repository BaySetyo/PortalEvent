package com.bunglon2.bunglonpe;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String eventID;
    private String url_photo_event;
    private String nama;
    private String tanggal;
    private String reglink;
    private String tempat;
    private String kategori;
    private String deskripsi;
    private String username;

    public Event() {

    }

    public Event(String eventID, String url_photo_event, String nama, String tanggal, String reglink, String tempat, String kategori, String deskripsi, String username) {
        this.eventID = eventID;
        this.url_photo_event = url_photo_event;
        this.nama = nama;
        this.tanggal = tanggal;
        this.reglink = reglink;
        this.tempat = tempat;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.username = username;
    }

    protected Event(Parcel in) {
        eventID = in.readString();
        url_photo_event = in.readString();
        nama = in.readString();
        tanggal = in.readString();
        reglink = in.readString();
        tempat = in.readString();
        kategori = in.readString();
        deskripsi = in.readString();
        username = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String geteventID() {
        return eventID;
    }

    public String getUrl_photo_event() {
        return url_photo_event;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getReglink() {
        return reglink;
    }

    public String getTempat() {
        return tempat;
    }

    public String getKategori() {
        return kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventID);
        dest.writeString(url_photo_event);
        dest.writeString(nama);
        dest.writeString(tanggal);
        dest.writeString(reglink);
        dest.writeString(tempat);
        dest.writeString(kategori);
        dest.writeString(deskripsi);
        dest.writeString(username);
    }
}
