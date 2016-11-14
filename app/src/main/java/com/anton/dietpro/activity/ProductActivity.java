package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.adapter.ProductAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.listViewProduct);
        ArrayList<Product> products;
        products = Product.getProductList(getApplicationContext());
        ProductAdapter adapterProduct = new ProductAdapter(this,products);
        listView.setAdapter(adapterProduct);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Выбран продукт с id:" + id, Toast.LENGTH_SHORT).show();
               /*
                Intent intent = new Intent(getApplicationContext(),DietDetailsActivity.class);
                intent.putExtra("productId",id + "");
                startActivityForResult(intent,1);
                */
            }
        });
    }

}
