package com.anton.dietpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Diet;

import java.util.List;

/**
 * Created by admin on 07.11.16.
 */

public class DietAdapter extends BaseAdapter {
    private List<Diet> list;
    private LayoutInflater layoutInflater;

    public DietAdapter(Context contex, List<Diet> list) {
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
            view = layoutInflater.inflate(R.layout.item_layout,parent,false);
        }
        Diet diet = getDiet(position);

        ((ImageView)view.findViewById(R.id.productImg)).setVisibility(View.GONE);
        TextView textView = (TextView) view.findViewById(R.id.textViewItem);
        textView.setText(diet.getName());
        TextView textViewSub = (TextView) view.findViewById(R.id.textViewItemSub);
        textViewSub.setText(String.format(view.getResources().getString(R.string.infoDiet),diet.getLength(),diet.getEffect()));
        // "Продолжительность " +  + " дней. " +"Эффективность: " + " -5кг");
        return view;
    }

    private Diet getDiet(int position){
        return (Diet)getItem(position);
    }

}
