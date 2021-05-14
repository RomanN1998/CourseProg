package com.example.courseprog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class CarActivity extends AppCompatActivity {
    private EditText editName, edirPric, edirSpe;
    private TextView textStatus, textUser;
    private ImageView imageView;
    private Button btnCreate, btnChoose, btnBack;
    private Button btnReserve, btnCange, btnDelete;
    private DatabaseReference mDataBase;
    private Uri uploadUri;
    private String upStringUri;
    private DatabaseReference mDataBaseSheld;
    private String securiy;

    private StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private Car car;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();
        Log.d("MyLog", "CarActivity UID : " + cUser.getUid());

        editName = findViewById(R.id.edit_car_name);
        edirPric = findViewById(R.id.edit_car_price);
        edirSpe = findViewById(R.id.edit_car_specification);
        textStatus = findViewById(R.id.text_car_status);
        imageView = findViewById(R.id.img_car_view);
        textUser = findViewById(R.id.text_car_user);

        btnChoose = findViewById(R.id.btn_car_choose);
        btnBack = findViewById(R.id.btn_car_back);
        btnCreate = findViewById(R.id.btn_car_create);
        btnReserve = findViewById(R.id.btn_car_reserve);
        btnCange = findViewById(R.id.btn_car_change);
        btnDelete = findViewById(R.id.btn_car_delete);
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference("ImageDB");
        mDataBase = FirebaseDatabase.getInstance().getReference("CAR");
        mDataBaseSheld = FirebaseDatabase.getInstance().getReference("USERS");

        Bundle arguments = getIntent().getExtras();

        if(arguments != null) {
            car = (Car) arguments.getSerializable(Car.class.getSimpleName());
            editName.setText(car.getName());
            edirPric.setText(car.getPrice());
            edirSpe.setText(car.getSpecification());
            upStringUri = car.getUrlImage();
            if (!car.getUrlImage().isEmpty())
                Picasso.get().load(car.getUrlImage()).into(imageView);

            if(car.getIdPeople().isEmpty()) {
                btnReserve.setEnabled(true);
                textStatus.setText("Свободно");
                btnReserve.setText("Забронировать");
            } else if(cUser.getUid().equals(car.getIdPeople())) {
                btnReserve.setEnabled(true);
                if (car.isStatus()) {
                    textStatus.setText("Забранировано");
                    btnReserve.setText("Отменить бронирование");
                } else {
                    textStatus.setText("Свободно");
                    btnReserve.setText("Забронировать");
                }
            } else {
                btnReserve.setEnabled(false);
                textStatus.setText("Забранировано");
                btnReserve.setText("Забранированый");
            }

        }

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("cadillac-escalade.jpg");
//        Glide.with(this /* context */)
//                .load(storageReference)
//                .into(imageView);

        mAuth = FirebaseAuth.getInstance();
        if (cUser != null) {
            Log.d("MyLog", "UID : " + cUser.getUid());
            mDataBaseSheld.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                    } else {
                        if (!(null == task.getResult().getValue(People.class))) {
                            People seUs = task.getResult().getValue(People.class);
                            securiy = seUs.getSheld();
                            if (securiy.equals("user")) {
                                btnCreate.setVisibility(View.GONE);
                                btnReserve.setVisibility(View.VISIBLE);
                                btnCange.setVisibility(View.GONE);
                                btnDelete.setVisibility(View.GONE);
                                btnChoose.setVisibility(View.GONE);
                                textUser.setVisibility(View.GONE);
                            } else if (securiy.equals("admin")) {
                                btnReserve.setEnabled(true);
                                textUser.setVisibility(View.VISIBLE);

                                if(car != null) {
                                    mDataBaseSheld.child(car.getIdPeople()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                Log.e("firebase", "Error getting data", task.getException());
                                            } else {
                                                if (!car.getIdPeople().isEmpty()) {
                                                    People pe;
                                                    pe = task.getResult().getValue(People.class);
                                                    String str = pe.getFullName() + " " + pe.getEmail() + " " + pe.getTelepone();
                                                    textUser.setText(str);
                                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                                } else {
                                                    textUser.setText("НЕТ");
                                                }

                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBase.child(car.getId()).removeValue();
                Toast.makeText(getApplicationContext(), "Удалить", Toast.LENGTH_SHORT).show();
            }
        });

        btnCange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                car.setName(editName.getText().toString());
                car.setPrice(edirPric.getText().toString());
                car.setSpecification(edirSpe.getText().toString());
                car.setUrlImage(upStringUri);
                mDataBase.child(car.getId()).setValue(car);
                Toast.makeText(getApplicationContext(), "Изменено", Toast.LENGTH_SHORT).show();
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (car.isStatus()) {
                    car.setStatus(false);
                    btnReserve.setText("Забронировать");
                    textStatus.setText("Свободно");
                    textUser.setText("НЕТ");
                    mDataBase.child(car.getId()).child("status").setValue(false);
                    mDataBase.child(car.getId()).child("idPeople").setValue("");
                }
                else {
                    car.setStatus(true);
                    btnReserve.setText("Отменить бронирование");
                    textStatus.setText("Забранировано");
                    mDataBase.child(car.getId()).child("status").setValue(true);
                    mDataBase.child(car.getId()).child("idPeople").setValue(cUser.getUid());
                }

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idkey = mDataBase.push().getKey();
                if (upStringUri != null){
                    Car car = new Car(idkey, editName.getText().toString(),
                            edirPric.getText().toString(), edirSpe.getText().toString(), upStringUri);

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

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(CarActivity.this, ShowCarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                }catch (Exception e) {
                }
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
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
                upStringUri = uploadUri.toString();
            }
        });
    }

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{
            Intent intent = new Intent(CarActivity.this, ShowCarActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

        }catch (Exception e) {
        }
    }
}
