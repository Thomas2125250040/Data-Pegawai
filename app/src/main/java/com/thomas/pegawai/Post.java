//04
package com.thomas.pegawai;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable { //implements parcelable

    @SerializedName("_id")
    private String id;
    private String gambar;
    private String nama;
    private int umur;
    private String jabatan;
    private String keterangan;
    private String created_date;
    protected Post(Parcel in) {
        id = in.readString();
        gambar = in.readString();
        nama = in.readString();
        umur = in.readInt();
        jabatan = in.readString();
        keterangan = in.readString();
        created_date = in.readString();
    }

    public String getCreated_date() {
        return created_date;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getGambar() {
        return gambar;
    }
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getUmur() {
        return umur;
    }

    public String getJabatan() {
        return jabatan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(gambar);
        dest.writeString(nama);
        dest.writeInt(umur);
        dest.writeString(jabatan);
        dest.writeString(keterangan);
        dest.writeString(created_date);
    }
}
