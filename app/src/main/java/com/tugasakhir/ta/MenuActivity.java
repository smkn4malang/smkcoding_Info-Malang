package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import butterknife.BindView;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
    }

    public void Utama(View view) {
        Intent back = new Intent(MenuActivity.this, HomeActivity.class);
        startActivity(back);
    }


    public void Tentang(View view) {
        Intent tentang = new Intent(MenuActivity.this, TentangActivity.class);
        startActivity(tentang);
    }

    public void EditProfil(View view) {
        Intent edit = new Intent(MenuActivity.this, EditProfilActivity.class);
        startActivity(edit);
    }

}
