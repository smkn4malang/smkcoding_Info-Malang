package com.tugasakhir.ta;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id. pager )
    ViewPager pager;
    @BindView(R.id. tab )
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout. activity_home );
        ButterKnife. bind (this);

        HomeAdapter viewPagerAdapter = new HomeAdapter(getSupportFragmentManager());
        pager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(pager);
    }


    public void Menu(View view) {
        Intent menu = new Intent(this, MenuActivity.class);
        startActivity(menu);
    }

    public void Profil(View view) {
        Intent profil = new Intent(this, ProfilActivity.class);
        startActivity(profil);
    }
}

