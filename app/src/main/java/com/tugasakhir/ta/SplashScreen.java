package com.tugasakhir.ta;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar mProgressCircle;
    private TextView Text;
    private ImageView splash;
    private Button Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        mProgressCircle = findViewById(R.id.progress_circle);
        Button = (Button) findViewById(R.id.button);
        Text = (TextView) findViewById(R.id.text);
        splash = (ImageView) findViewById(R.id.tvSplash);
        ulang();
    }

    public void ulang(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    Intent splas = new Intent(SplashScreen.this, AwalActivity.class);
                    splas.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(splas);
                    finish();
                } else {
                    mProgressCircle.setVisibility(View.GONE);
                    splash.setVisibility(View.GONE);
                    Text.setVisibility(View.VISIBLE);
                    Button.setVisibility(View.VISIBLE);
                }
            }

        }, 5000);

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressCircle.setVisibility(View.GONE);
                splash.setVisibility(View.VISIBLE);
                Text.setVisibility(View.GONE);
                Button.setVisibility(View.GONE);
                ulang();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashScreen.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
