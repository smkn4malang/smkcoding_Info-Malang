package com.tugasakhir.ta;

//import com.google.firebase.database.Exclude;

public class User {
    public String name, alamat, email, mImageUrl, gender, phone;

    public User(){

    }


    public User(String name, String alamat, String email, String mImageUrl, String gender, String phone){
        this.name = name;
        this.alamat = alamat;
        this.email = email;
        this.mImageUrl = mImageUrl;
        this.gender = gender;
        this.phone = phone;
    }

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getName(){ return name; }
    public void setName(String name){this.name = name;}
    public String getAlamat(){ return alamat; }
    public void setAlamat(String alamat){this.alamat = alamat;}
    public String getImageUrl() {return mImageUrl;}
    public void setImageUrl(String mImageUrl) {this.mImageUrl = mImageUrl;}
    public String getgender() {return gender;}
    public void setgender(String gender) {this.gender = gender;}
    public String getphone() {return phone;}
    public void setphone(String phone) {this.phone = phone;}





}

