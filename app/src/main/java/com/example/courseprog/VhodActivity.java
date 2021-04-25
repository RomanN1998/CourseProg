package com.example.courseprog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class VhodActivity extends AppCompatActivity {

    private Button btnIn, btnBack;
    private EditText editEmail, editPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vhod);

        btnBack = findViewById(R.id.btn_vhod_back);
        btnIn = findViewById(R.id.btn_vhod_in);

        editEmail = findViewById(R.id.edit_vhod_login);
        editPassword = findViewById(R.id.edit_vhod_password);
        mAuth = FirebaseAuth.getInstance();

        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editEmail.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {

                    mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User SignIn Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VhodActivity.this, ShowCarActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "User SignIn failed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Введите логин пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            Toast.makeText(this, "Пользователь присутствует", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VhodActivity.this, ShowCarActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Пользователь отсутсвует", Toast.LENGTH_SHORT).show();
        }
    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{
            Intent intent = new Intent(VhodActivity.this, InputActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e) {
        }
    }
}