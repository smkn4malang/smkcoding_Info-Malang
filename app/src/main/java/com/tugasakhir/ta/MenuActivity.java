package com.tugasakhir.ta;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "ViewDatabase";
    @BindView(R.id.btn_sign_out)
    LinearLayout out;
    private ImageView Foto3;
    private TextView Nama3, Email3, url;
    private ProgressBar mProgressCircle;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private String userID ;
    private FirebaseDatabase mData;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

//-------------------------------------------------------
        Foto3 = (ImageView) findViewById(R.id.foto3);
        Nama3 = (TextView) findViewById(R.id.nama3);
        Email3 = (TextView) findViewById(R.id.email3);
        mProgressCircle = findViewById(R.id.progress_circle);
        mProgressCircle.setVisibility(View.GONE);

        //ambil data user email
//-----------------------------------------------------------------------------------
        url = (TextView) findViewById(R.id.url);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference("users");
        FirebaseUser user =mAuth.getCurrentUser();
        userID = user.getUid();

//        notif();

        mRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//-----------------------------------------------------------------------------------
        Foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ProfilActivity.class));
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MenuActivity.this, AwalActivity.class));
                    finish();
                }
            }
        };

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(MenuActivity.this, AwalActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }
    //    proses user email
//-----------------------------------------------------------------------------
    private void showData(DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            Nama3.setText(Objects.requireNonNull(user).getName());
            Email3.setText(Objects.requireNonNull(user).getEmail());

            url.setText(user.getImageUrl());
            Picasso.with(this)
                    .load(user.getImageUrl())
                    .into(Foto3);

    }


    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MenuActivity.this, AwalActivity.class));
                finish();
            } else {
//                setDataToView(user);

            }
        }


    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

//    -------------------------------------------------------------------------------------------

    public void Utama(View view) {
        Intent back = new Intent(MenuActivity.this, HomeActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        finish();
    }


    public void Tentang(View view) {
        Intent tentang = new Intent(MenuActivity.this, TentangActivity.class);
        tentang.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tentang);
    }

    public void EditProfil(View view) {
        Intent edit = new Intent(MenuActivity.this, EditProfilActivity.class);
        edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(edit);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if(opr.isDone()){
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        }else {
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//        mAuth.addAuthStateListener(authListener);
//    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if(result.isSuccess()){
//            GoogleSignInAccount account = result.getSignInAccount();
//            Nama3.setText(account.getDisplayName());
//            Email3.setText(account.getEmail());
//
//            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto3);
//        }else{
//
//        }
//    }


//    @OnClick(R.id.btn_sign_out)
//    public void onBtnSignOutClicked(){
//        signOut();
//        mProgressCircle.setVisibility(View.VISIBLE);
//    }
//
//    public void signOut(){
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new
//                                                                                ResultCallback<Status>() {
//                                                                                    @Override
//                                                                                    public void onResult(@NonNull Status status) {
//                                                                                        if(status.isSuccess()){
//                                                                                            updateUI();
//                                                                                        }else{
//
//                                                                                        }
//                                                                                    }
//                                                                                });
////        mAuth.signOut();
//
//
//// this listener will be called when there is change in firebase user session
//        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    startActivity(new Intent(MenuActivity.this, AwalActivity.class));
//                    finish();
//                }
//            }
//        };
//    }

//    public void updateUI(){
//        mAuth.signOut();
//        Intent i = new Intent(this, AwalActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
//
//    }


    public void Tambah(View view) {
        Intent tambah = new Intent(this, TambahActivity.class);
        tambah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tambah);
    }
    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        Intent inn = new Intent(this, HomeActivity.class);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inn);
        finish();
    }


    public void profil(View view) {
        Intent inn = new Intent(this, ProfilActivity.class);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inn);
        finish();
    }
}
