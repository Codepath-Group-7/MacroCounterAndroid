package com.example.macro_counter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Filter;
import android.widget.Filterable;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foods;
    private String TAG = "FoodAdapter";

    public FoodAdapter(Context context, List<Food> foods){
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fooditem_card, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.bind(food);

        Log.d(TAG, "Food model: " + holder.toString());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFoodName;
        private TextView tvCalNumber;

        private Button btAddFood;
        RelativeLayout container;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvCalNumber = itemView.findViewById(R.id.tvCalNumber);
            container = itemView.findViewById(R.id.container);
            btAddFood = itemView.findViewById(R.id.btAddFood);
        }

        public void bind(Food food) {
            // Bind the food data to the view elements
            tvFoodName.setText(food.getItemName());

            tvCalNumber.setText("Cal: " + food.getCalories());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, tvFoodName.getText(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, SearchDetailActivity.class);
                    i.putExtra("food", Parcels.wrap(food));
                    context.startActivity(i);
            }});
            btAddFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Button Works" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, SearchAddButtonActivity.class);
                    i.putExtra("food", Parcels.wrap(food));
                    context.startActivity(i);
                }});




        }
    }
}
