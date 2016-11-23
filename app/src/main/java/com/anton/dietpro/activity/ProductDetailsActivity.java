package com.anton.dietpro.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Product;
import com.squareup.picasso.Picasso;

import static com.anton.dietpro.R.id.imageView;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName;
    private TextView productDescription;
    private TextView productProtein;
    private TextView productFat;
    private TextView productCarbohydrate;
    private ImageView productUrl;
    private Intent intent;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productName = (TextView) findViewById(R.id.productName);
        productDescription = (TextView) findViewById(R.id.productDescription);
        productProtein = (TextView) findViewById(R.id.productProtein);
        productFat = (TextView) findViewById(R.id.productFat);
        productCarbohydrate = (TextView) findViewById(R.id.productCarbohydrate);
        productUrl = (ImageView) findViewById(R.id.productImg);
        intent = getIntent();
        if (intent.getExtras() != null) {
            productId = Integer.valueOf(intent.getExtras().getString("productId"));
            this.initView(productId);
        }
        else {
            Toast.makeText(this, "Произошла ошибка. Диета не найдена", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView(long id){
        if (id == 0){
            productName.setText("Диета не найдена");
        }
        else{
            Product product = Product.getProductById(id, getApplicationContext());
            productName.setText(product.getName());
            productDescription.setText(Html.fromHtml("Описание продукта"/*String.valueOf(product.getDescription())*/));
            productProtein.setText(String.valueOf(product.getPfc().getProtein()) + " гр.");
            productFat.setText(String.valueOf(product.getPfc().getFat()) + " гр.");
            productCarbohydrate.setText(String.valueOf(product.getPfc().getCarbohydrate()) + " гр.");
            if( product.getUrl() != "") {
                Picasso.with(getApplicationContext())
                        .load(product.getUrl())
                        .into(productUrl);
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(this,ProductActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
