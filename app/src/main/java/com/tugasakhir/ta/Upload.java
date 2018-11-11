package com.tugasakhir.ta;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mDesc;
    private String mImageUrl;
    private String mNama;
    private String mProfil;
    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String desc, String imageUrl, String nama, String profil) {

        mDesc = desc;
        mImageUrl = imageUrl;
        mNama = nama;
        mProfil = profil;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getProfil() {
        return mProfil;
    }

    public void setProfil(String profil) {
        mProfil = profil;
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
