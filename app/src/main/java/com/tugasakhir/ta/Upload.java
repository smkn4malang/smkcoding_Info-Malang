package com.tugasakhir.ta;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mNama;
    private String mImageUrl;
    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String nama, String imageUrl) {
        if (nama.trim().equals("")) {
            nama = "No Name";
        }

        mNama = nama;
        mImageUrl = imageUrl;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
