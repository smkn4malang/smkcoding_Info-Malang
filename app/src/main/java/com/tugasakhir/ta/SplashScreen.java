package com.tugasakhir.ta;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    //    TextView tvSplash;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_splash_screen);
//
//        tvSplash = (TextView) findViewById(R.id.tvSplash);
//
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext(), AwalActivity.class));
//                finish();
//            }
//        }, 1500L);
//    }

    private ProgressBar mProgressCircle;
    private TextView Text;
    private Button Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        mProgressCircle = findViewById(R.id.progress_circle);
        Button = (Button) findViewById(R.id.button);
        Text = (TextView) findViewById(R.id.text);
        ulang();
    }

    public void ulang(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    startActivity(new Intent(SplashScreen.this, AwalActivity.class));
                    finish();
                } else {
                    mProgressCircle.setVisibility(View.GONE);
                    Text.setVisibility(View.VISIBLE);
                    Button.setVisibility(View.VISIBLE);
                }
            }

        }, 5000);

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressCircle.setVisibility(View.VISIBLE);
                Text.setVisibility(View.GONE);
                Button.setVisibility(View.GONE);
                ulang();
            }
        });
    }

}
