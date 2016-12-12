package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Product;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName;
    private TextView productDescription;
    private TextView productProtein;
    private TextView productFat;
    private TextView productCarbohydrate;
    private ImageView productUrl;

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
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            long productId = Integer.valueOf(intent.getExtras().getString("productId"));
            this.initView(productId);
        }
        else {
            Toast.makeText(this, getString(R.string.productsNotFound), Toast.LENGTH_SHORT).show();
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

    private void initView(long id){
        if (id == 0){
            productName.setText(getString(R.string.productsNotFound));
        }
        else{
            Product product = Product.getProductById(id, getApplicationContext());
            productName.setText(product.getName());
            productDescription.setText(Html.fromHtml(getResources().getString(R.string.productDescription))); /*String.valueOf(product.getDescription())*/
            productProtein.setText(String.format(productProtein.getText().toString(), (float)product.getPfc().getProtein()));
            productFat.setText(String.format(productFat.getText().toString(), ((float)product.getPfc().getFat())));
            productCarbohydrate.setText(String.format(productCarbohydrate.getText().toString(), (float)product.getPfc().getCarbohydrate()));
            if( !product.getUrl().equals("")) {
                Picasso.with(getApplicationContext())
                        .load(product.getUrl())
                        // .resize(350,350)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.image_not_load)
                        .into(productUrl);
            }

        }
    }

}
