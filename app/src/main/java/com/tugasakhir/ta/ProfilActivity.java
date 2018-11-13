package com.tugasakhir.ta;

import android.annotation.SuppressLint;
import android.app.Person;
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
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity
        implements ImageAdapter.OnItemClickListener{
    private static final String TAG = "ViewDatabase";
    private ImageView Foto;
    private TextView Nama, Email, url, gender, phone, alamat;
    private ImageButton Tambah;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mListener;
    private FirebaseDatabase mData;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatalike;
    private DatabaseReference mRef;
    private String userID;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private ValueEventListener mDBListener;

    private List<Upload> mUploads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);


        Foto = (ImageView) findViewById(R.id.foto);
        Nama = (TextView) findViewById(R.id.nama);
        Email = (TextView) findViewById(R.id.email);
        gender = (TextView) findViewById(R.id.gender);
        phone = (TextView) findViewById(R.id.phone);
        alamat = (TextView) findViewById(R.id.alamat);
//        edit = (TextView) findViewById(R.id.edit_profil);
        Tambah = (ImageButton) findViewById(R.id.tambah);
//-----------------------------------------------------------------------------------
        url = (TextView) findViewById(R.id.url);

        mauth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference("users");
        FirebaseUser user = mauth.getCurrentUser();
        userID = user.getUid();

        mRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notif();

        Email = (TextView) findViewById(R.id.email);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(ProfilActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ProfilActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mDatalike = FirebaseDatabase.getInstance().getReference("like");

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
            User user = dataSnapshot.getValue(User.class);
            Nama.setText(Objects.requireNonNull(user).getName());
            Email.setText(Objects.requireNonNull(user).getEmail());
            gender.setText(Objects.requireNonNull(user).getgender());
            phone.setText(Objects.requireNonNull(user).getphone());
            alamat.setText(Objects.requireNonNull(user).getAlamat());

            url.setText(user.getImageUrl());
            Picasso.with(this)
                    .load(user.getImageUrl())
                    .into(Foto);
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


    @Override
    protected void onResume() {
        super.onResume();
    }
    //-------------------------------------------
    public void Utama(View view) {
        Intent back = new Intent(ProfilActivity.this, HomeActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        finish();
    }


    @Override
    protected void onStart(){
        super.onStart();
        mauth.addAuthStateListener(mListener);
    }

    public void tambah(View view) {
        Intent tambah = new Intent(ProfilActivity.this, TambahActivity.class);
        tambah.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tambah);
        finish();
    }


    public void EditProfil(View view) {
        Intent edit = new Intent(ProfilActivity.this, EditProfilActivity.class);
        edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(edit);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent inn = new Intent(this, HomeActivity.class);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inn);
        finish();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    mDatalike.child(selectedKey).child(userID).removeValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}

