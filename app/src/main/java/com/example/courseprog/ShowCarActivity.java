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

    private Button btnsignout, btnCreate;
    private RecyclerView listShowCar;
    private ImageView imads;
    private FirebaseAuth mAuth;
    private CarAdapter adapter;
    private DatabaseReference mDataBase;

    private ArrayList<Car> cars = new ArrayList<Car>();
    private ArrayList<Car> carsArray = new ArrayList<Car>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcar);

        btnsignout = findViewById(R.id.btn_showcar_signout);
        btnCreate = findViewById(R.id.btn_showcar_create);
        imads = findViewById(R.id.imageView3);
        listShowCar = findViewById(R.id.listShowCar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser cUser = mAuth.getCurrentUser();
        Log.d("MyLog", "ShowCarActivity UID : " + cUser.getUid());

        CarAdapter.OnCarClickListener carClickListener = new CarAdapter.OnCarClickListener() {
            @Override
            public void onCarClick(Car car, int position) {

            }
        };
        adapter = new CarAdapter(this, cars, carClickListener);
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
                carsArray.addAll(cars);
                adapter.notifyDataSetChanged();
                System.out.println(cars.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDataBase.addValueEventListener(vListener);
    }

}
