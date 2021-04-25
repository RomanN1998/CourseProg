package com.example.courseprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);
    }

    public void InputIn(View view) {
        Intent intent = new Intent(InputActivity.this, VhodActivity.class);
        startActivity(intent);
        finish();
    }

    public void InputSign(View view) {
        Intent intent = new Intent(InputActivity.this, RegActivity.class);
        startActivity(intent);
        finish();
    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{
            Intent intent = new Intent(InputActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e) {
        }
    }

}
