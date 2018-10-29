package com.tugasakhir.ta;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, ImageAdapter.OnItemClickListener{
    private static final String TAG = "ViewDatabase";
    private ImageView Foto;
    private TextView Nama, Email, edit, url;
    private ImageButton Tambah;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mListener;

//    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseDatabase mData;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRef;
    private ValueEventListener mDBListener;
    private String userID, url2;

    private List<Upload> mUploads;

    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);


        Foto = (ImageView) findViewById(R.id.foto);
        Nama = (TextView) findViewById(R.id.nama);
        Email = (TextView) findViewById(R.id.email);
        edit = (TextView) findViewById(R.id.edit_profil);
        Tambah = (ImageButton) findViewById(R.id.tambah);
//ambil data user email
//-----------------------------------------------------------------------------------
        url = (TextView) findViewById(R.id.url);

        mauth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference();
        FirebaseUser user = mauth.getCurrentUser();
        userID = user.getUid();

        notif();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//-----------------------------------------------------------------------------------

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
        Email = (TextView) findViewById(R.id.email);

        //get current user
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        setDataToView(user);
//-----------------------------------------------------------
//        mRecyclerView = findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(ProfilActivity.this, mUploads);

//        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ProfilActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfilActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
//    proses user email
//-----------------------------------------------------------------------------
    private void showData(DataSnapshot dataSnapshot) {
        mProgressCircle.setVisibility(View.INVISIBLE);
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            User user = new User();
            user.setName(ds.child(userID).getValue(User.class).getName());
            user.setEmail(ds.child(userID).getValue(User.class).getEmail());
            user.setAlamat(ds.child(userID).getValue(User.class).getAlamat());
            user.setImageUrl(ds.child(userID).getValue(User.class).getImageUrl());

//            String uuu;
            Nama.setText(user.getName());
            Email.setText(user.getEmail());
            url.setText(user.getImageUrl());
            url2 = url.toString();
            Picasso.with(this)
                    .load(user.getImageUrl())
                    .into(Foto);

        }
        mProgressCircle.setVisibility(View.GONE);
    }

    private void notif() {
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:sign_in" + user.getUid());
//                    toastMessage("Berhasil Masuk Dengan Email " + user.getEmail());
                }
            }
        };
    }
//-----------------------------------------------------------------------------
    //Email
//    @SuppressLint("SetTextI18n")
//    private void setDataToView(FirebaseUser user) {
//
//        Email.setText(user.getEmail());
//
//
//    }


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
        mauth.addAuthStateListener(mListener);
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
            Email.setText(account.getEmail());

            Glide.with(this).load(account.getPhotoUrl().toString()).into(Foto);
        }
    }

    public void tambah(View view) {
        Intent tambah = new Intent(ProfilActivity.this, TambahActivity.class);
        startActivity(tambah);
        finish();
    }

//Edit Profil
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ProfilActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Exit")
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        ProfilActivity.this.finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
//    }

}

