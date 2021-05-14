package com.example.courseprog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowCarActivity extends AppCompatActivity {

    private Button btnsignout, btnCreate, btnSort;
    private RecyclerView listShowCar;
    private ImageView imads;
    private FirebaseAuth mAuth;
    private CarAdapter adapter;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBaseSheld;
    private String securiy;
    private boolean boolBtn = true;

    private ArrayList<Car> cars = new ArrayList<Car>();
    private ArrayList<Car> carsArray = new ArrayList<Car>();
    private ArrayList<Car> carList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcar);

        btnsignout = findViewById(R.id.btn_showcar_signout);
        btnCreate = findViewById(R.id.btn_showcar_create);
        btnSort = findViewById(R.id.btn_showcar_sort);
        imads = findViewById(R.id.imageView3);
        listShowCar = findViewById(R.id.listShowCar);
        mDataBaseSheld = FirebaseDatabase.getInstance().getReference("USERS");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();
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
                                btnSort.setText("Моя бронь");
                                btnSort.setVisibility(View.VISIBLE);
                                btnCreate.setVisibility(View.GONE);
                            } else if (securiy.equals("admin")) {
                                btnSort.setVisibility(View.VISIBLE);
                                btnCreate.setVisibility(View.VISIBLE);
                                btnSort.setText("Сортировка");
                            }
                        }
                    }
                }
            });
        }

        CarAdapter.OnCarClickListener carClickListener = new CarAdapter.OnCarClickListener() {
            @Override
            public void onCarClick(Car car, int position) {
                Intent intent = new Intent(ShowCarActivity.this, CarActivity.class);
                intent.putExtra(Car.class.getSimpleName(), car);
                intent.putExtra("CARSHOW","101");
                startActivityForResult(intent, 101);
            }
        };

        adapter = new CarAdapter(this, carsArray, carClickListener);
        listShowCar.setAdapter(adapter);
        mDataBase = FirebaseDatabase.getInstance().getReference("CAR");
        getDataFromDB();

//        Picasso.get().load("https://dh.img.tyt.by/720x720s/n/obshchestvo/05/f/charnobylski-shlikh-07.jpg").into(imads);

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ShowCarActivity.this, RegActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowCarActivity.this, CarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (securiy.equals("user")) {
                    btnSort.setText("Моя бронь");
                    btnSort.setVisibility(View.VISIBLE);
                    btnCreate.setVisibility(View.GONE);
                } else if (securiy.equals("admin")) {
                    btnSort.setVisibility(View.VISIBLE);
                    btnCreate.setVisibility(View.VISIBLE);
                    btnSort.setText("Сортировка");
                }

                carList.clear();
                if(boolBtn) {

                    if (securiy.equals("user")) {
                        for(Car i : cars) {
                            if (i.getIdPeople().equals(cUser.getUid())) {
                                carList.add(i);
                            }
                        }
                    } else if (securiy.equals("admin")) {
                        for(Car i : cars) {
                            if (i.isStatus() == true) {
                                carList.add(i);
                            }
                        }
                    }
                    boolBtn = false;

                } else {
                    for(Car i : cars) {
                        if (i.isStatus() == false) {
                            carList.add(i);
                        }
                    }
                    boolBtn = true;
                }
                carsArray.clear();
                carsArray.addAll(carList);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bundle arguments = data.getExtras();
            if(arguments !=null) {
                Car car = (Car) arguments.getSerializable(Car.class.getSimpleName());
//                adapter.add(car.getName());
            }
//            adapter.notifyDataSetChanged();
        }
    }

    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (cars.size() > 0)
                    cars.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Car car = ds.getValue(Car.class);
                    assert  car != null;
                    cars.add(car);
                }

                for(Car i : cars) {
                    if (i.isStatus() == false) {
                        carList.add(i);
                    }
                }
                carsArray.addAll(carList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDataBase.addValueEventListener(vListener);
    }

    //Системная кнопка Назад
    @Override
    public void onBackPressed(){
        try{
            Intent intent = new Intent(ShowCarActivity.this, RegActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e) {
        }
    }

}
