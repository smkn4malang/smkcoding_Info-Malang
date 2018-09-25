package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfilActivity extends AppCompatActivity {
    public static final String EXTRA_NAME ="EXTRANAME" ;
    @BindView(R.id.namaEdit)
    EditText Editnama;
    @BindView(R.id.jkEdit)
    EditText jkedit;
    @BindView(R.id.noEdit)
    EditText noedit;
    @BindView(R.id.tambah)
    Button save;
    @BindView(R.id.account)
    ImageView Account;


    private ArrayList<Image> imageLibrary = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_profil);
        ButterKnife.bind(this);

    }

    public void Utama(View view) {
        Intent menu = new Intent(EditProfilActivity.this, HomeActivity.class);
        startActivity(menu);
        finish();
    }


    public void profil(View view) {
        Intent i = new Intent (this, ProfilActivity.class);
        startActivity(i);
        finish();
    }

    //image picker
    @OnClick(R.id.account)
    public void onViewClickedImage(){
        ImagePicker. with (this)
                .setFolderMode(true)
                .setMaxSize(10)
                .setMultipleMode(false)
                .setCameraOnly(false)
                .setFolderTitle("Albums")
                .setSelectedImages(imageLibrary)
                .setAlwaysShowDoneButton(true)
                .setKeepScreenOn(true)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            imageLibrary = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            Glide.with(this)
                    .load(imageLibrary.get(0).getPath()).into(Account);
        }else{
            Toast.makeText(EditProfilActivity.this, "Isi data dengan lengkap", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id. tambah )
    public void onViewClicked() {
        if (!Editnama.getText().toString().isEmpty()
                && !jkedit.getText().toString().isEmpty()
                && !noedit.getText().toString().isEmpty()
                && !imageLibrary.isEmpty()) {
//            mSiswaModel = new SiswaModel();
//            mSiswaModel.setName(edtName.getText().toString());
//            mSiswaModel.setAddress(edtAddress.getText().toString());
//            mSiswaModel.setPathPicture(imageLibrary.get(0).getPath().toString());
//            MyApp. db .userDao().insertAll(mSiswaModel);
            Intent intent = new Intent(new Intent(this, ProfilActivity.class));
            intent.addFlags(Intent. FLAG_ACTIVITY_NEW_TASK );
            intent.addFlags(Intent. FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }
    }
}
