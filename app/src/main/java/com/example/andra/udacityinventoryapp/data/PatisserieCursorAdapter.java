package com.example.andra.udacityinventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andra.udacityinventoryapp.MainActivity;
import com.example.andra.udacityinventoryapp.R;

public class PatisserieCursorAdapter extends CursorAdapter {

    OnSaleListener listener;

    public PatisserieCursorAdapter(Context context, Cursor cursor, OnSaleListener listener) {
        super(context, cursor, true);
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView productImage = (ImageView) view.findViewById(R.id.patisserie_image);
        TextView productName = (TextView) view.findViewById(R.id.product_title);
        TextView productQuantity = (TextView) view.findViewById(R.id.item_quantity);
        TextView productPrice = (TextView) view.findViewById(R.id.item_price);

        Button sale = (Button) view.findViewById(R.id.sale_button);

        int imageColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_IMAGE_PRODUCT);
        String imagePath = cursor.getString(imageColumnIndex);
        Glide.with(context).load(imagePath).into(productImage);

        int nameProdColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_NAME);
        String nameProduct = cursor.getString(nameProdColumnIndex);
        productName.setText(nameProduct);

        int quantityProdColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_QUANTITY);
        final int quantityProd = cursor.getInt(quantityProdColumnIndex);
        productQuantity.setText(quantityProd + " pieces");

        int priceProdColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_PRICE);
        String priceProd = cursor.getString(priceProdColumnIndex);
        productPrice.setText(priceProd + " $");


        int idColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry._ID);
        final int productId = cursor.getInt(idColumnIndex);
        view.setTag(productId);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityProd > 0 && listener != null) {
                    int newQuantity = quantityProd - 1;
                    listener.onSale(productId, newQuantity);
                }
            }
        });
    }

    public interface OnSaleListener {
        void onSale(int productId, int newQuantity);
    }
}
