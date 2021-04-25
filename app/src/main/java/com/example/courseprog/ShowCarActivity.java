package com.example.courseprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ShowCarActivity extends AppCompatActivity {

    private Button btnsignout, btnCreate;
    private ImageView imads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcar);

        btnsignout = findViewById(R.id.btn_showcar_signout);
        btnCreate = findViewById(R.id.btn_showcar_create);
        imads = findViewById(R.id.imageView3);

        Picasso.get().load("https://dh.img.tyt.by/720x720s/n/obshchestvo/05/f/charnobylski-shlikh-07.jpg").into(imads);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ShowCarActivity.this, InputActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ShowCarActivity.this, CarActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
