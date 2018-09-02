package com.tugasakhir.ta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView Foto;
    private TextView Nama;
    private ImageView Foto2;
    private TextView Nama2;
    private ImageView Foto3;
    private TextView Nama3;
    private TextView Email;

    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);

        Foto3 = (ImageView) findViewById(R.id.foto3);
        Nama3 = (TextView) findViewById(R.id.nama3);
        Foto2 = (ImageView) findViewById(R.id.foto2);
        Nama2 = (TextView) findViewById(R.id.nama2);
        Foto = (ImageView) findViewById(R.id.foto);
        Nama = (TextView) findViewById(R.id.nama);
        Email = (TextView) findViewById(R.id.email);


        GoogleSignInOptions signInOptions = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    public void Utama(View view) {
        Intent back = new Intent(ProfilActivity.this, HomeActivity.class);
        startActivity(back);
    }

//Email
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

        @Override
    protected void onStart(){
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Nama.setText(account.getDisplayName());
            Nama2.setText(account.getDisplayName());
            Nama3.setText(account.getDisplayName());
            Email.setText(account.getEmail());

            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto);
            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto2);
            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto3);
        }
    }

//Edit Profil

}
