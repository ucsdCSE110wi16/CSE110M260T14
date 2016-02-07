package com.example.cse110mb260t14.ffs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends AppCompatActivity {

    String[] categoriesArray = {"Vehicles", "Electronics", "Appliances"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.category_list_item, categoriesArray);

        ListView listView = (ListView) findViewById(R.id.categories_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                ListingsActivity.resetCategories();
                ListingsActivity.addCategory((String) adapter.getItemAtPosition(position));
                Intent intent = new Intent(CategoriesActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });
    }

}
