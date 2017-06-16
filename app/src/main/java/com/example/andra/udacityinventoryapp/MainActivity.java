package com.example.andra.udacityinventoryapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.andra.udacityinventoryapp.data.DatabaseWrapper;
import com.example.andra.udacityinventoryapp.data.PatisserieCursorAdapter;

public class MainActivity extends AppCompatActivity implements PatisserieCursorAdapter.OnSaleListener{

    public static final String TAG = "MainActivity";
    public static final String PRODUCT_ID_EXTRA = "id";

    ListView listView;
    PatisserieCursorAdapter adapter;
    DatabaseWrapper wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);


        wrapper = new DatabaseWrapper(this);
        Cursor cursor = wrapper.getCursorWithAllData();
        adapter = new PatisserieCursorAdapter(this, cursor, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");
                int productId = (int) view.getTag();
                Intent i = new Intent(MainActivity.this, EditProductActivity.class);
                i.putExtra(PRODUCT_ID_EXTRA, productId);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.changeCursor(wrapper.getCursorWithAllData());
    }

    @Override
    public void onSale(int productId, int newQuantity) {
        wrapper.decrementQuantity(productId, newQuantity);
        adapter.changeCursor(wrapper.getCursorWithAllData());
    }
}
