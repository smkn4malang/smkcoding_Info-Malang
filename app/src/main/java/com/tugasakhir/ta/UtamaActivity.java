package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class UtamaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_utama);


    }

    public void Utama2(View view) {
        Intent terdahulu = new Intent(UtamaActivity.this, Utama2Activity.class);
        startActivity(terdahulu);
    }

    public void Menu(View view) {
        Intent menu = new Intent(UtamaActivity.this, MenuActivity.class);
        startActivity(menu);
    }

    public void Profil(View view) {
        Intent profil = new Intent(UtamaActivity.this, ProfilActivity.class);
        startActivity(profil);
    }
}
