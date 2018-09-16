package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Utama2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_utama2);
    }

    public void Utama(View view) {
        Intent hariini = new Intent(Utama2Activity.this, UtamaActivity.class);
        startActivity(hariini);
    }

    public void Menu(View view) {
        Intent menuu = new Intent(Utama2Activity.this, MenuActivity.class);
        startActivity(menuu);
    }
    public void Profil(View view) {
        Intent profil = new Intent(Utama2Activity.this, ProfilActivity.class);
        startActivity(profil);
    }

    public void tambah(View view) {
        Intent tambah = new Intent(Utama2Activity.this, TambahActivity.class);
        startActivity(tambah);
    }
}
