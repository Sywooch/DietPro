package com.anton.dietpro.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.anton.dietpro.R;
import com.anton.dietpro.models.Diary;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Nutrition;
import com.anton.dietpro.models.Product;

import java.text.SimpleDateFormat;
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
        this.layoutInflater = (LayoutInflater) contex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.list.get(position).getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = this.layoutInflater.inflate(R.layout.nutrition_layout,parent,false);
        }
        final Nutrition nutrition = getNutrition(position);

        final ToggleButton ingestionComplete = (ToggleButton) view.findViewById(R.id.ingestionCompelete);

        if(Diary.isCompleteMenu(view.getContext(), getItemId(position))) {
            ingestionComplete.setChecked(true); //ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorGreen));
        }
        else{
            ingestionComplete.setChecked(false);
        }
        TextView ingestionName = (TextView) view.findViewById(R.id.ingestionName);
        ingestionName.setText(nutrition.getName());
        TextView amountKkal = (TextView) view.findViewById(R.id.amountKkal);
        amountKkal.setText( String.format(view.getResources().getString(R.string.amountKkal) , nutrition.getAmountKkal().intValue() ));
        TextView ingestionTime = (TextView) view.findViewById(R.id.ingestionTime);
        ingestionTime.setText( String.format(view.getResources().getString(R.string.time) , (new SimpleDateFormat("kk:mm")).format(nutrition.getDatetime()) ));

        ListView productList =(ListView) view.findViewById(R.id.listProductNutrition);
        ProductItemAdapter productItems = new ProductItemAdapter(parent.getContext(), nutrition.getProducts());
        productList.setAdapter(productItems);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long nutrition_id) {

                ToggleButton productComplete = (ToggleButton) view.findViewById(R.id.productComplete);
                productComplete.setChecked(!productComplete.isChecked());
                if (productComplete.isChecked()) {
                    // сохраняем в БД отчет о употребленном продукте(id - nutrition_id)
                    if (!Diary.insertProductComplete(view.getContext(), nutrition_id)) {
                        Toast.makeText(view.getContext(), view.getResources().getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
                        productComplete.setChecked(false);
                    }
                    else{
                        Nutrition n = Nutrition.getNutritionById(nutrition_id,view.getContext());
                        if(Diary.isCompleteMenu(view.getContext(), n.getId())) {
                            ingestionComplete.setChecked(true);
                        }
                    }
                }
                else{
                    if (!Diary.removeNutrition(view.getContext(), nutrition_id)){
                        Toast.makeText(view.getContext(), view.getResources().getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
                        productComplete.setChecked(true);
                    }
                    else{
                        ingestionComplete.setChecked(false);
                        Toast.makeText(view.getContext(), view.getResources().getString(R.string.canceled), Toast.LENGTH_LONG).show();
                    }
                }

                View parent = (View)view.getParent().getParent().getParent().getParent().getParent();
                updateAmountCall(parent,nutrition.getDay());
            }
        });
        ingestionComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewParent = (View) view.getParent();
                ListView productList = (ListView) viewParent.findViewById(R.id.listProductNutrition);
                if (productList != null) {
                    int count = productList.getChildCount();
                    // заполнение/отмена всех приемов пищи
                    if (ingestionComplete.isChecked()) {
                        ingestionComplete.setChecked(true);
                        for(int i = 0; i<count; i++) {
                            View itemView = productList.getChildAt(i);
                            ToggleButton productComplete = (ToggleButton) itemView.findViewById(R.id.productComplete);
                            long nutrition_id = productList.getAdapter().getItemId(i);
                            // сохраняем в БД отчет о употребленном продукте(id - nutrition_id)
                            if (!Diary.insertProductComplete(viewParent.getContext(), nutrition_id)) {
                                Toast.makeText(view.getContext(), view.getResources().getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
                            }
                            else {
                                productComplete.setChecked(true);
                            }
                        }
                    }
                    else{
                        ingestionComplete.setChecked(false);
                        for(int i = 0; i<count; i++) {
                            View itemView = productList.getChildAt(i);
                            ToggleButton productComplete = (ToggleButton) itemView.findViewById(R.id.productComplete);
                            long nutrition_id = productList.getAdapter().getItemId(i);
                            if (!Diary.removeNutrition(viewParent.getContext(), nutrition_id)) {
                                Toast.makeText(viewParent.getContext(), view.getResources().getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
                            } else {
                                productComplete.setChecked(false);
                                Toast.makeText(viewParent.getContext(), view.getResources().getString(R.string.canceled), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(viewParent.getContext(), view.getResources().getString(R.string.listProductsNotFound), Toast.LENGTH_SHORT).show();
                }
                View parent = (View)view.getParent().getParent().getParent();
                updateAmountCall(parent,nutrition.getDay());
            }
        });
        return view;
    }

    private void updateAmountCall(View parent, long day) {
        if(parent != null){
            TextView headerDayCallories = (TextView) parent.findViewById(R.id.headerDayCallories);
            if(headerDayCallories != null) {
                headerDayCallories.setText(String.format(parent.getResources().getString(R.string.headerDayCallories)
                        , Diary.getCurrentCallories(day, parent.getContext())
                        , Nutrition.getAmountCallories(day, parent.getContext())));
            }
        }
    }

    private Nutrition getNutrition(int position){
        return (Nutrition)getItem(position);
    }
}
