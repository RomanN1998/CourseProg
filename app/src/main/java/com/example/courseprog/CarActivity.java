package com.example.courseprog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CarActivity extends AppCompatActivity {
    private EditText editName, edirPric, edirSpe;
    private ImageView imageView;
    private Button btnCreate, btnChoose;
    private DatabaseReference mDataBase;
    private Uri uploadUri;


    private StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car);
        mAuth = FirebaseAuth.getInstance();
        Log.d("MyLog", "UID : " + mAuth.getUid());

        btnCreate = findViewById(R.id.btn_car_create);
        edirPric = findViewById(R.id.edit_car_price);
        editName = findViewById(R.id.edit_car_name);
        edirSpe = findViewById(R.id.edit_car_specification);
        imageView = findViewById(R.id.img_car_view);
        btnChoose = findViewById(R.id.btn_car_choose);
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference("ImageDB");
        mDataBase = FirebaseDatabase.getInstance().getReference("CAR");

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("cadillac-escalade.jpg");
//        Glide.with(this /* context */)
//                .load(storageReference)
//                .into(imageView);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idkey = mDataBase.push().getKey();
                Car car = new Car(idkey, editName.getText().toString(),
                        edirPric.getText().toString(), edirSpe.getText().toString());

                if(!TextUtils.isEmpty(editName.getText().toString()) && !TextUtils.isEmpty(edirPric.getText().toString())
                        && !TextUtils.isEmpty(edirSpe.getText().toString())) {
                    mDataBase.child(idkey).setValue(car);

                    Toast.makeText(getApplicationContext(), "Машина добавлена в каталог", Toast.LENGTH_SHORT).show();
                    edirPric.setText("");
                    edirSpe.setText("");
                    editName.setText("");

                } else {
                    Toast.makeText(getApplicationContext(), "Зполниет все поля", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImage();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null) {

            if (resultCode == RESULT_OK) {
                Log.d("MyLog", "Image URI : " + data.getData());
                imageView.setImageURI(data.getData());
                uploadImage();

            }

        }
    }

    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef = storageRef.child(System.currentTimeMillis() + "my_image");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
            }
        });
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }
}
