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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.nutrition_layout,parent,false);
        }
        final Nutrition nutrition = getNutrition(position);
        ArrayList<Product> prods = Nutrition.getNutritionProducts(nutrition.getDay(),position + 1,parent.getContext());
        if (prods == null){
            prods = new ArrayList<>();
            prods.add(new Product("Продукты не найдены."));
        }
        nutrition.setProducts(prods);

        final ToggleButton ingestionComplete = (ToggleButton) view.findViewById(R.id.ingestionCompelete);

        if(Diary.isCompleteMenu(view.getContext(), getItemId(position))) {
            ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorGreen));
        }
        TextView ingestionName = (TextView) view.findViewById(R.id.ingestionName);
        ingestionName.setText(nutrition.getName());

        TextView amountKkal = (TextView) view.findViewById(R.id.amountKkal);
        amountKkal.setText( String.format(amountKkal.getText().toString() , nutrition.getAmountKkal().intValue() ));

        ListView productList =(ListView) view.findViewById(R.id.listProductNutrition);
        ProductItemAdapter productItems = new ProductItemAdapter(parent.getContext(), prods);
        productList.setAdapter(productItems);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long nutrition_id) {

                ToggleButton productComplete = (ToggleButton) view.findViewById(R.id.productComplete);
                productComplete.performClick();
                if (productComplete.isChecked()) {
                    if (!Diary.insertProductComplete(view.getContext(), nutrition_id)) { // сохраняем в БД отчет о употребленном продукте(id - nutrition_id)
                        Toast.makeText(view.getContext(), "Ошибка. Попробуйте снова.", Toast.LENGTH_LONG).show();
                        productComplete.performClick();
                    }
                    else{
                        Nutrition n = Nutrition.getNutritionById(nutrition_id,view.getContext());
                        if(Diary.isCompleteMenu(view.getContext(), n.getId())) {
                            ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorGreen));
                        }
                        Log.d("INGESTION2","DONT SET COMPLETE");
                    }

                }
                else{
                    if (!Diary.removeNutrition(view.getContext(), nutrition_id)){
                        Toast.makeText(view.getContext(), "Ошибка удаления. Попробуйте снова.", Toast.LENGTH_LONG).show();
                        productComplete.performClick();
                    }
                    else{
                        ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(view.getContext(), "Отменено.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        ingestionComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewParent = (View) view.getParent();
                ListView productList = (ListView) viewParent.findViewById(R.id.listProductNutrition);
                TextView test = (TextView) viewParent.findViewById(R.id.ingestionName);
                if (productList != null) {
                    int firstPos = productList.getFirstVisiblePosition();
                    int lastPos = productList.getHeaderViewsCount();
                    int count = productList.getChildCount();
                    // заполнение/отмена всех приемов пищи
                    if (ingestionComplete.isChecked()) {

                        ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorGreen));
                        for(int i = 0; i<count; i++) {
                            View itemView = productList.getChildAt(i);
                            ToggleButton productComplete = (ToggleButton) itemView.findViewById(R.id.productComplete);
                            long nutrition_id = productList.getAdapter().getItemId(i);
                            if (!Diary.insertProductComplete(viewParent.getContext(), nutrition_id)) { // сохраняем в БД отчет о употребленном продукте(id - nutrition_id)
                                Toast.makeText(view.getContext(), "Ошибка. Попробуйте снова.", Toast.LENGTH_LONG).show();
                            }
                            else {
                                productComplete.performClick();
                            }

                        }
                    }
                    else{
                        ingestionComplete.setBackgroundColor(view.getResources().getColor(R.color.colorPrimary));
                        for(int i = 0; i<count; i++) {
                            View itemView = productList.getChildAt(i);
                            ToggleButton productComplete = (ToggleButton) itemView.findViewById(R.id.productComplete);
                            long nutrition_id = productList.getAdapter().getItemId(i); //getView(i,null,null);
                            if (!Diary.removeNutrition(viewParent.getContext(), nutrition_id)) {
                                Toast.makeText(viewParent.getContext(), "Ошибка удаления. Попробуйте снова.", Toast.LENGTH_LONG).show();
                            } else {
                                productComplete.performClick();
                                Toast.makeText(viewParent.getContext(), "Отменено.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(viewParent.getContext(), "Список продуктов не найден.", Toast.LENGTH_SHORT).show();
                }

                Diary.printDiary(view.getContext());
            }
        });
        return view;
    }

    private Nutrition getNutrition(int position){
        return (Nutrition)getItem(position);
    }
}
