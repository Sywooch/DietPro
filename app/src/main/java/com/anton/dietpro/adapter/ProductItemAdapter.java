package com.anton.dietpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.anton.dietpro.R;
import com.anton.dietpro.models.CircleTransform;
import com.anton.dietpro.models.Diary;
import com.anton.dietpro.models.ExpandedListView;
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
        this.layoutInflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Product getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.list.get(position).getNutritionId();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = this.layoutInflater.inflate(R.layout.item_product_layout,parent,false);
        }
        final Product product = getProduct(position);
        TextView productName = (TextView) view.findViewById(R.id.productName);
        TextView amountKkal = (TextView) view.findViewById(R.id.amountKkal);
        ImageView productImg = (ImageView) view.findViewById(R.id.productImg);
        final ToggleButton productComplete = (ToggleButton) view.findViewById(R.id.productComplete);
        if(Diary.isCompleteNutrition(view.getContext(), product.getNutritionId())){
            productComplete.setChecked(true);
        }
        if(product.getUrl() != "" &&  product.getUrl() != null) {
            if (product.getUrl().trim().length() > 0) {
                Picasso.with(parent.getContext())
                        .load(product.getUrl())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.image_not_load)
                        .into(productImg);

            }
        }
        productName.setText(product.getName());
        amountKkal.setText(String.format(view.getResources().getString(R.string.amoutKkalItem),product.getWeight()));
        productComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productComplete.isChecked()) {
                    productComplete.setChecked(false);
                }
                else{
                    productComplete.setChecked(true);
                }
                ExpandedListView exList = (ExpandedListView) ((View) (view.getParent().getParent().getParent())).findViewById(R.id.listProductNutrition);
                exList.performItemClick((View) view.getParent(), position, product.getNutritionId());
            }
        });
        return view;
    }

    private Product getProduct(int position){
        return (Product)getItem(position);
    }

}
