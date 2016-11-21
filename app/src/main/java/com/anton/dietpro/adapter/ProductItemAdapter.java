package com.anton.dietpro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 21.11.16.
 */

public class ProductItemAdapter extends BaseAdapter {
    private List<Product> list;
    private LayoutInflater layoutInflater;

    public ProductItemAdapter(Context contex, List<Product> list) {
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
            view = layoutInflater.inflate(R.layout.item_product_layout,parent,false);
        }
        Product product = getProduct(position);
        TextView productName = (TextView) view.findViewById(R.id.productName);
        TextView amountKkal = (TextView) view.findViewById(R.id.amountKkal);
        ImageView productImg = (ImageView) view.findViewById(R.id.productImg);

        if(product.getUrl() != "") {
            Picasso.with(parent.getContext())
                    .load(product.getUrl())
                    .into(productImg);
        }
        productName.setText(product.getName());
        String template = amountKkal.getText().toString();
        Log.i("TEST", template);
        amountKkal.setText(String.format(template,product.getWeight())); //product.getPfc().getCalories()
        return view;
    }

    private Product getProduct(int position){
        return (Product)getItem(position);
    }

}
