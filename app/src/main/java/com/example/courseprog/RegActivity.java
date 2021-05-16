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
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {

    private EditText editLogin, editEmail, editFullname, editPassword, editTel;
    private Button btnReg, btnBack, btnVhod, btnSignIn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

    private boolean flagReg = false;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);

        editLogin = findViewById(R.id.edit_reg_login);
        editEmail = findViewById(R.id.edit_reg_email);
        editFullname = findViewById(R.id.edit_reg_fullname);
        editPassword = findViewById(R.id.edit_reg_password);
        editTel = findViewById(R.id.edit_reg_tel);

        btnReg = findViewById(R.id.btn_reg_reg);
        btnBack = findViewById(R.id.btn_reg_back);
        btnVhod = findViewById(R.id.btn_reg_vhod);
        btnSignIn = findViewById(R.id.btn_reg_signin);
        btnBack = findViewById(R.id.btn_reg_back);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("USERS");

        FirebaseUser cUser = mAuth.getCurrentUser();

        btnVhod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, ShowCarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editEmail.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "User SignIn Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegActivity.this, ShowCarActivity.class);
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
                    Toast.makeText(getApplicationContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLogin.setVisibility(View.GONE);
                editEmail.setVisibility(View.VISIBLE);
                editFullname.setVisibility(View.GONE);
                editPassword.setVisibility(View.VISIBLE);
                editTel.setVisibility(View.GONE);
                btnReg.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);

                btnVhod.setVisibility(View.GONE);
                flagReg = false;
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLogin.setVisibility(View.VISIBLE);
                editEmail.setVisibility(View.VISIBLE);
                editFullname.setVisibility(View.VISIBLE);
                editPassword.setVisibility(View.VISIBLE);
                editTel.setVisibility(View.VISIBLE);
                btnReg.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);

                btnVhod.setVisibility(View.GONE);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagReg) {
                    if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editEmail.getText().toString())
                            && !TextUtils.isEmpty(editFullname.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString()) &&
                            !TextUtils.isEmpty(editTel.getText().toString())) {

                        mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            People people = new People(mAuth.getUid().toString(), editLogin.getText().toString(),
                                                    editEmail.getText().toString(), editFullname.getText().toString(),
                                                    editTel.getText().toString());

                                            mDataBase.child(mAuth.getUid()).setValue(people);
                                            Toast.makeText(getApplicationContext(), "Аккаунт создан удачно", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(RegActivity.this, ShowCarActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Аккаунт создан неудачно", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Вы указали не все данные", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    flagReg = true;
                    editLogin.setVisibility(View.VISIBLE);
                    editEmail.setVisibility(View.VISIBLE);
                    editFullname.setVisibility(View.VISIBLE);
                    editPassword.setVisibility(View.VISIBLE);
                    editTel.setVisibility(View.VISIBLE);
                    btnReg.setVisibility(View.VISIBLE);
                    btnSignIn.setVisibility(View.GONE);

                    btnVhod.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
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
            editLogin.setVisibility(View.GONE);
            editEmail.setVisibility(View.GONE);
            editFullname.setVisibility(View.GONE);
            editPassword.setVisibility(View.GONE);
            editTel.setVisibility(View.GONE);
            btnReg.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);

            btnVhod.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "Пользователь отсутсвует", Toast.LENGTH_SHORT).show();
            editLogin.setVisibility(View.GONE);
            editEmail.setVisibility(View.VISIBLE);
            editFullname.setVisibility(View.GONE);
            editPassword.setVisibility(View.VISIBLE);
            editTel.setVisibility(View.GONE);
            btnReg.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);

            btnVhod.setVisibility(View.GONE);
        }

    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{

        }catch (Exception e) {
        }
    }
}