package com.example.courseprog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder>  {

    interface OnCarClickListener{
        void onCarClick(Car car, int position);
    }

    private final CarAdapter.OnCarClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<Car> car;

    CarAdapter(Context context, List<Car> cars, CarAdapter.OnCarClickListener onCarClick) {
        this.car = cars;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onCarClick;
    }

    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.car_show, parent, false);
        return new CarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarAdapter.ViewHolder holder, int position) {
        Car car = this.car.get(position);
        holder.inent.setText(car.getName());
        holder.name.setText(car.getPrice());
        holder.name.setText(car.getSpecification());
        if (!car.getUrlImage().isEmpty())
            Picasso.get().load(car.getUrlImage()).into(holder.imgView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onCarClick(car, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return car.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView inent, name, spec;
        final ImageView imgView;
        ViewHolder(View view){
            super(view);
            inent = view.findViewById(R.id.text_car_show_car);
            name = view.findViewById(R.id.text_car_show_price);
            imgView = view.findViewById(R.id.image_view_car_show);
            spec = view.findViewById(R.id.text_car_show_specification);
        }

    }
}