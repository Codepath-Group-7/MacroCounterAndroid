package com.example.macro_counter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchAddButtonActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SearchAddButtonActivity";

    private TextView textItemName, textCalories, textProteinCnt, textFat, textCholesterolCDF, textFiber;
    private Button btnCancel, btnApply;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference db;
    String uid;
    String userName;
    Food food;
    String foodKey;

//    private Map mapTimeStamp = new HashMap();
//    private Map mapUser = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Foods");

        food = Parcels.unwrap(getIntent().getParcelableExtra("food"));

        textItemName = findViewById(R.id.textItemName);
        textCalories = findViewById(R.id.textCalories);
        textProteinCnt = findViewById(R.id.textProteinCnt);
        textFat = findViewById(R.id.textFat);
        textCholesterolCDF = findViewById(R.id.textCholesterolCDF);
        textFiber = findViewById(R.id.textFiber);

        textItemName.setText(food.getItemName());
        textCalories.setText(food.getCalories());
        textProteinCnt.setText(food.proteinCnt);
        textFat.setText(food.getFat());
        textCholesterolCDF.setText(food.getCholesterol());
        textFiber.setText(food.getFiber());

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCancel:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btnApply:
                registerFood();
                break;
        }
    }

    private void registerFood() {


        String itemName = textItemName.getText().toString().trim();
        String calories = textCalories.getText().toString().trim();
        String proteinCnt = textProteinCnt.getText().toString().trim();
        String fat = textFat.getText().toString().trim();
        String cholesterol = textCholesterolCDF.getText().toString().trim();
        String fiber = textFiber.getText().toString().trim();

        String itemId = mDatabase.push().getKey();

        Food foodDB = new Food(itemName, calories, proteinCnt, fat, cholesterol, fiber);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String foodPostKey = database.getReference("FoodPost").push().getKey();
        foodKey = database.getReference("Foods").push().getKey();

        if (user != null) {


            readUserName(new FirebaseCallback() {
                @Override
                public void onCallBack(String Uname) {
                    userName = Uname;
                    writeFoodData();


                }
            });








        } else {
            // No user is signed in
            Toast.makeText(SearchAddButtonActivity.this, "Not Signed in! Please sign in!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SearchAddButtonActivity.this, LoginActivity.class));
        }

    }







    private void writeFoodData() {

        food.setEmail(user.getEmail());

        Date date = new Date();
        long timeStamp = date.getTime();
        food.setTimeInMillis(timeStamp);

        food.setUserDisplayName(userName);
        System.out.println("PRINTING USER NAME 1: " + userName);
        System.out.println("PRINTING FROM FOOD OBJ: " + food.getUserDisplayName());



        FirebaseDatabase.getInstance().getReference("Foods")
                .child(foodKey)
                .setValue(food).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SearchAddButtonActivity.this, "Food has been registered Successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SearchAddButtonActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(SearchAddButtonActivity.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    private void readUserName(FirebaseCallback firebaseCallback) {
        uid = user.getUid();
        System.out.println("Printing User ID: " + uid);
        db = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                User userProfile = dataSnapshot.getValue(User.class);
                userName = userProfile.getName();

                firebaseCallback.onCallBack(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        db.addValueEventListener(userListener);
    }

    private interface FirebaseCallback {
        void onCallBack(String Uname);
    }


}