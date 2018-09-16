package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class TambahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tambah);
    }

    public void Utama(View view) {
        Intent inten = new Intent (this, HomeActivity.class);
        startActivity(inten);
        finish();
    }

    public void profil(View view) {
        Intent intent = new Intent (this, ProfilActivity.class);
        startActivity(intent);
        finish();
    }
}
