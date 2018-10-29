package com.tugasakhir.ta;

//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.nguyenhoanglam.imagepicker.model.Config;
//import com.nguyenhoanglam.imagepicker.model.Image;
//import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class EditProfilActivity extends AppCompatActivity {
//    public static final String EXTRA_NAME ="EXTRANAME" ;
//    @BindView(R.id.namaEdit)
//    EditText Editnama;
//    @BindView(R.id.jkEdit)
//    EditText jkedit;
//    @BindView(R.id.noEdit)
//    EditText noedit;
//    @BindView(R.id.tambah)
//    Button save;
//    @BindView(R.id.account)
//    ImageView Account;
//
//
//    private ArrayList<Image> imageLibrary = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_edit_profil);
//        ButterKnife.bind(this);
//
//    }
//
//    public void Utama(View view) {
//        Intent menu = new Intent(EditProfilActivity.this, HomeActivity.class);
//        startActivity(menu);
//        finish();
//    }
//
//
//    public void profil(View view) {
//        Intent i = new Intent (this, ProfilActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    //image picker
//    @OnClick(R.id.account)
//    public void onViewClickedImage(){
//        ImagePicker. with (this)
//                .setFolderMode(true)
//                .setMaxSize(10)
//                .setMultipleMode(false)
//                .setCameraOnly(false)
//                .setFolderTitle("Albums")
//                .setSelectedImages(imageLibrary)
//                .setAlwaysShowDoneButton(true)
//                .setKeepScreenOn(true)
//                .start();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            imageLibrary = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
//            Glide.with(this)
//                    .load(imageLibrary.get(0).getPath()).into(Account);
//        }else{
//            Toast.makeText(EditProfilActivity.this, "Isi data dengan lengkap", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @OnClick(R.id. tambah )
//    public void onViewClicked() {
//        if (!Editnama.getText().toString().isEmpty()
//                && !jkedit.getText().toString().isEmpty()
//                && !noedit.getText().toString().isEmpty()
//                && !imageLibrary.isEmpty()) {
////            mSiswaModel = new SiswaModel();
////            mSiswaModel.setName(edtName.getText().toString());
////            mSiswaModel.setAddress(edtAddress.getText().toString());
////            mSiswaModel.setPathPicture(imageLibrary.get(0).getPath().toString());
////            MyApp. db .userDao().insertAll(mSiswaModel);
//            Intent intent = new Intent(new Intent(this, ProfilActivity.class));
//            intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK );
//            intent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TASK );
//            startActivity(intent);
//        }
//    }
//}
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

//    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    EditText editTextName, editTextAlamat, editTextGender, editTextPhone;
    TextView editTextEmail;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_profil);

//        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        editTextEmail = (TextView) findViewById(R.id.editTextEmail);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAlamat = (EditText) findViewById(R.id.alamat);
        editTextGender = (EditText) findViewById(R.id.gender);
        editTextPhone = (EditText) findViewById(R.id.phone);
        mStorageRef = FirebaseStorage.getInstance().getReference("profil");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(EditProfilActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {

        editTextEmail.setText(user.getEmail());


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if(mImageUri != null){
            final String name = editTextName.getText().toString().trim();
            final String email = editTextEmail.getText().toString().trim();
            final String alamat = editTextAlamat.getText().toString().trim();
            final String gender = editTextGender.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();
            if (name.isEmpty()) {
                editTextName.setError("Username is required");
                editTextName.requestFocus();
                return;
            }

            if (alamat.isEmpty()) {
                editTextAlamat.setError("Alamat is required");
                editTextAlamat.requestFocus();
                return;
            }
            if (gender.isEmpty()) {
                editTextGender.setError("Gender is required");
                editTextGender.requestFocus();
                return;
            }
            if (phone.isEmpty()) {
                editTextPhone.setError("Number Phone is required");
                editTextPhone.requestFocus();
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            //    -------------------------------------------------------------
            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
//                        Upload friendlyMessage = new Upload(null, mUsername, downloadUri.toString());
//                        mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        mProgressBar.setVisibility(View.GONE);
                        User user = new User(name, alamat, email, downloadUri.toString(), gender, phone);
//                        String uploadId = mDatabaseRef.push().getKey();
//                        mDatabaseRef.child(uploadId).setValue(user);
                        mDatabaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                        Intent ii = new Intent(EditProfilActivity.this, HomeActivity.class);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ii);
                        finish();
                    } else {
                        Toast.makeText(EditProfilActivity.this, "failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(this, "Select Profile First", Toast.LENGTH_LONG).show();
        }


    }

    public void Utama(View view) {
        Intent ii = new Intent(this, HomeActivity.class);
        startActivity(ii);
        finish();
    }
}
