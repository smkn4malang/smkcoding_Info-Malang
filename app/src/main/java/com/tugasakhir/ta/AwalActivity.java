package com.tugasakhir.ta;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AwalActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    //Google Login
    @BindView(R.id.prof_section)
    LinearLayout profSection;
    @BindView(R.id.btn_login)
    SignInButton btnLogin;
    @BindView(R.id.editText)
    LinearLayout Edittext;
    private static final int REQ_CODE = 3;

    private GoogleApiClient googleApiClient;
//    private ProgressBar mProgressCircle;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //Email Login
    private EditText inputEmail, inputPassword;
    private TextView btnSignup, btnReset;
    private Button SignUP;
    private FirebaseAuth mAuth;
    private FirebaseAuth BAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_awal);
        ButterKnife.bind(this);
//Google Login
//-------------------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent langsung = new Intent(this, HomeActivity.class);
            langsung.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(langsung);
            finish();
        }

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuthAuth) {

            }

        };

        GoogleSignInOptions signInOptions = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
//Email Login
//-------------------------------------------------------------------------------------
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignup = (TextView) findViewById(R.id.txt_register);
        SignUP = (Button) findViewById(R.id.btn_sign_in);
        btnReset = (TextView) findViewById(R.id.txt_forgot);
//        mProgressCircle = findViewById(R.id.progress_circle);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Get Firebase auth instance
        BAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent up = new Intent(AwalActivity.this, RegisterActivity.class);
                up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(up);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(AwalActivity.this, ForgotPasswordActivity.class);
                reset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(reset);
            }
        });

        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    inputEmail.setError("Masukan Email");
                    inputEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputEmail.setError("Masukan Email yang benar");
                    inputEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    inputPassword.setError("Masukan Password");
                    inputPassword.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    inputPassword.setError("Password minimal 8 karakter");
                    inputPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                BAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(AwalActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // there was an error

                                    Intent intent = new Intent(AwalActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AwalActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }
//Google Login

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handleResult(GoogleSignInResult result) {
        progressBar.setVisibility(View.VISIBLE);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                                txtName.setText(firebaseUser.getDisplayName());
//                                txtEmail.setText(firebaseUser.getEmail());
//
//                                Glide.with(AwalActivity.this).load(firebaseUser.getPhotoUrl().toString()).into(imgProfile);
                                updateUI(true);
                            } else {
                                updateUI(false);
                            }

                        }
                    });
        } else {
            Toast.makeText(this, "Lihat koneksi anda", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            Intent home = new Intent(this, HomeActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        signIn();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AwalActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}