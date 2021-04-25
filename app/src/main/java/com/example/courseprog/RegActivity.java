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
    private Button btnReg, btnBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;

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

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference("USERS");

        FirebaseUser cUser = mAuth.getCurrentUser();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editEmail.getText().toString())
                && !TextUtils.isEmpty(editFullname.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString()) &&
                        !TextUtils.isEmpty(editTel.getText().toString())) {

                    mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        People people = new People(mAuth.getUid().toString(), editLogin.getText().toString(),
                                                editEmail.getText().toString(), editFullname.getText().toString(),
                                                editTel.getText().toString());

                                        mDataBase.child(mAuth.getUid()).setValue(people);
                                        Toast.makeText(getApplicationContext(), "Создан аккаунт удачно", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegActivity.this, VhodActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Создан аккаунт неудачно", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Вы указали не все данные", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{
            Intent intent = new Intent(RegActivity.this, VhodActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e) {
        }
    }
}