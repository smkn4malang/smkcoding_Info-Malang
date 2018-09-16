package com.tugasakhir.ta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView Foto;
    private TextView Nama;
    private ImageView Foto2;
    private TextView Nama2;
    private ImageView Foto3;
    private TextView Nama3;
    private TextView Email;
    private TextView edit;
    private ImageButton Tambah;
    private FirebaseAuth mauth;

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
        edit = (TextView) findViewById(R.id.edit_profil);
        Tambah = (ImageButton) findViewById(R.id.tambah);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, EditProfilActivity.class));
            }
        });


        GoogleSignInOptions signInOptions = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
//-------------------------------------------------------
        //get firebase auth instance
        mauth = FirebaseAuth.getInstance();
        Email = (TextView) findViewById(R.id.email);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);
    }
    //Email
    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {

        Email.setText(user.getEmail());


    }


    @Override
    protected void onResume() {
        super.onResume();
    }
    //-------------------------------------------
    public void Utama(View view) {
        Intent back = new Intent(ProfilActivity.this, HomeActivity.class);
        startActivity(back);
        finish();
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

    public void tambah(View view) {
        Intent tambah = new Intent(ProfilActivity.this, TambahActivity.class);
        startActivity(tambah);
        finish();
    }

//Edit Profil

}

