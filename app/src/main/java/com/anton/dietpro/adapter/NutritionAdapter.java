package com.anton.dietpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Nutrition;
import com.anton.dietpro.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 18.11.16.
 */

public class NutritionAdapter extends BaseAdapter {
    private List<Nutrition> list;
    private LayoutInflater layoutInflater;

    public NutritionAdapter(Context contex, List<Nutrition> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.nutrition_layout,parent,false);
        }
        Nutrition nutrition = getNutrition(position);
        TextView ingestionName = (TextView) view.findViewById(R.id.ingestionName);
        ingestionName.setText(nutrition.getName() + " День " + nutrition.getDay());
        TextView amountKkal = (TextView) view.findViewById(R.id.amountKkal);
        amountKkal.setText( String.valueOf( nutrition.getAmountKkal() ) );
        ListView productList =(ListView) view.findViewById(R.id.listProductNutrition);
        ArrayList<Product> prods = Nutrition.getNutritionProducts(1,position + 1,parent.getContext());
        if (prods == null){
            prods = new ArrayList<Product>();
            prods.add(new Product("Продукты не найдены."));
        }
        ProductItemAdapter productItems = new ProductItemAdapter(parent.getContext(), prods);
        //ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(parent.getContext(),android.R.layout.simple_list_item_1,products);
        productList.setAdapter(productItems);
        return view;
    }

    private Nutrition getNutrition(int position){
        return (Nutrition)getItem(position);
    }
}
