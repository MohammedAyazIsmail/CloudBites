package com.example.cloudbites.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cloudbites.R;
import com.example.cloudbites.models.NavCategoryDetailedModel;
import com.example.cloudbites.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg,addItem,removeItem;
    TextView name,price,description,productId,quantity;
    Button addToCart;
    int totalQuantity = 1;
    int totalPrice = 0;

    Toolbar toolbar;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    //ViewAll Products
    ViewAllModel viewAllModel = null;

    //NavCategoryDetailed Products
    NavCategoryDetailedModel navCategoryDetailedModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }
        else if (object instanceof NavCategoryDetailedModel) {
            navCategoryDetailedModel = (NavCategoryDetailedModel) object;
        }

        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailed_img);

        addToCart = findViewById(R.id.add_to_cart);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        price = findViewById(R.id.detailed_price);
        productId = findViewById(R.id.productId);
        description = findViewById(R.id.description);
        name = findViewById(R.id.detailed_name);

        //ViewAll Products
        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            name.setText(viewAllModel.getName());
            price.setText("R"+viewAllModel.getPrice()+".00");

            totalPrice = viewAllModel.getPrice() * totalQuantity;

            description.setText(viewAllModel.getDescription());
            productId.setText(viewAllModel.getProduct_id());

        }

        //NavCategoryDetailed Products
        if (navCategoryDetailedModel != null){
            Glide.with(getApplicationContext()).load(navCategoryDetailedModel.getImg_url()).into(detailedImg);
            name.setText(navCategoryDetailedModel.getName());
            price.setText("R"+navCategoryDetailedModel.getPrice()+".00");

            totalPrice = navCategoryDetailedModel.getPrice() * totalQuantity;

            description.setText(navCategoryDetailedModel.getDescription());
            productId.setText(navCategoryDetailedModel.getProduct_id());

        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (viewAllModel != null){
                        totalPrice = viewAllModel.getPrice() * totalQuantity;
                    }
                    if (navCategoryDetailedModel != null){
                        totalPrice = navCategoryDetailedModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (viewAllModel !=null){
                        totalPrice = viewAllModel.getPrice() * totalQuantity;
                    }
                    if (navCategoryDetailedModel !=null){
                        totalPrice = navCategoryDetailedModel.getPrice() * totalQuantity;
                    }
                }
            }
        });
    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added To The Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}