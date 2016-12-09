package com.anton.dietpro.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.anton.dietpro.R;
import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.adapter.ProductAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Product;
import com.anton.dietpro.providers.SuggestionProvider;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private ListView listView;
    private ProductAdapter adapterProduct;
    private boolean orderBy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listViewProduct);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            //Сохраняем запрос
            suggestions.saveRecentQuery(query, null);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            doSearch(query);
        }else {
            ArrayList<Product> products;
            products = Product.getProductList(getApplicationContext());
            adapterProduct = new ProductAdapter(this, products);
            listView.setAdapter(adapterProduct);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ProductDetailsActivity.class);
                intent.putExtra("productId",id + "");
                startActivityForResult(intent,1);
            }
        });
    }

    private void doSearch(String query) {
        ArrayList<Product> products = Product.getSearchProductList(getApplicationContext(), query);
        adapterProduct = new ProductAdapter(this, products);
        listView.setAdapter(adapterProduct);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.getQuery();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(intent);

                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                doSearch(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.search_menu_group,true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_search) {
            //onSearchRequested();
            return true;
        }

        if (id == R.id.action_sort){
            ArrayList<Product> products = Product.getDietListSorted(getApplicationContext(),orderBy);
            ProductAdapter adapter = new ProductAdapter(this,products);
            listView.setAdapter(adapter);
            orderBy = !orderBy;
        }
        return super.onOptionsItemSelected(item);
    }

}
