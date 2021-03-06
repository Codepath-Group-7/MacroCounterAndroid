package com.example.macro_counter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapterFb extends FirebaseRecyclerAdapter<Food, FoodAdapterFb.FoodsViewholder> {
    private Context context;
    private List<Food> foods;

    private String TAG = "FoodAdapterFB";
    private Object databaseReference;
    private OnItemClickListener listener;

    public FoodAdapterFb(@NonNull FirebaseRecyclerOptions<Food> options) {
        super(options);
//        Log.d(TAG, "options model: " + options );
    }

    @NonNull
    @Override
    public FoodsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_entry, parent, false);
        return new FoodAdapterFb.FoodsViewholder(view);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull FoodsViewholder holder, int position, @NonNull Food model)     {

        holder.tvAdapterFoodName.setText(model.getItemName());
        holder.tvAdapterCaloriesValue.setText(model.getCalories());

    }


    class FoodsViewholder extends RecyclerView.ViewHolder {
        TextView tvAdapterFoodName, tvAdapterCalories, tvAdapterCaloriesValue;
        Button btnAdapterAdd;
        RelativeLayout container;
        private Context context;


        public FoodsViewholder(@NonNull View itemView) {
            super(itemView);

            tvAdapterFoodName = itemView.findViewById(R.id.tvAdapterFoodName);
            tvAdapterCaloriesValue = itemView.findViewById(R.id.tvAdapterCaloriesValue);
            btnAdapterAdd = itemView.findViewById(R.id.btnAdapterAdd);
            container =  itemView.findViewById(R.id.container);

            //listener set on ENTIRE ROW, you may set on individual components within a row.
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);

//                        Food foods = getSnapshots().getSnapshot(position).getValue(Food.class);
//                        Log.d(TAG,"snap item: " + foods.getItemName());
//                        Intent i = new Intent(context, SearchDetailActivity.class);
//                        i.putExtra("food", Parcels.wrap(foods));
//                        context.startActivity(i);

                    }

                }
            });

            btnAdapterAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);

//                        Food foods = getSnapshots().getSnapshot(position).getValue(Food.class);
//                        Log.d(TAG,"snap item: " + foods.getItemName());
//                        Intent i = new Intent(context, SearchAddButtonActivity.class);
//                        i.putExtra("food", Parcels.wrap(foods));
//                        context.startActivity(i);
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
//        void onItemClick(DocumentSnapshot ds, int position);

        void OnItemClick(DataSnapshot snapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
