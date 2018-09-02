package com.tugasakhir.ta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.btn_sign_out) ImageView out;
    private ImageView Foto3;
    private TextView Nama3;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        Foto3 = (ImageView) findViewById(R.id.foto3);
        Nama3 = (TextView) findViewById(R.id.nama3);

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
////Out
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
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//
//    }
//
//    private void goLogInScreen(){
//        Intent inten = new Intent(this, AwalActivity.class);
//        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(inten);
//    }
//
//    public void logOut(View view){
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                if(status.isSuccess()){
//                    goLogInScreen();
//                }
//            }
//        });
//    }
//
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
            Nama3.setText(account.getDisplayName());

            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto3);
        }else{

        }
    }


    @OnClick(R.id.btn_sign_out)
    public void onBtnSignOutClicked(){
        signOut();
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new
                                                                                ResultCallback<Status>() {
                                                                                    @Override
                                                                                    public void onResult(@NonNull Status status) {
                                                                                        if(status.isSuccess()){
                                                                                            updateUI();
                                                                                        }
                                                                                    }
                                                                                });
    }

    public void updateUI(){
        Intent i = new Intent(this, AwalActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
