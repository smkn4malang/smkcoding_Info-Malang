package com.tugasakhir.ta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfilActivity extends AppCompatActivity {
    public static final String EXTRA_NAME ="EXTRANAME" ;
    @BindView(R.id.namaEdit)
    EditText Editnama;
    @BindView(R.id.save)
    Button Save;

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
    }

//Edit Profil
//    @OnClick(R.id.save)
//    public void onViewClicked(){
//        String name = Editnama.getText().toString();
//        Intent intent = new Intent(this, ProfilActivity.class);
//        intent.putExtra(EXTRA_NAME, name);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}
