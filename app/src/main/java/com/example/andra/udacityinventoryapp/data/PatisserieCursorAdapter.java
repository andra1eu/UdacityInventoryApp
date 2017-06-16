package com.example.andra.udacityinventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andra.udacityinventoryapp.MainActivity;
import com.example.andra.udacityinventoryapp.R;

public class PatisserieCursorAdapter extends CursorAdapter {
    public PatisserieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView productImage = (ImageView) view.findViewById(R.id.patisserie_image);
        TextView productName = (TextView) view.findViewById(R.id.product_title);

        int imageColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_IMAGE_PRODUCT);
        String imagePath = cursor.getString(imageColumnIndex);
        Glide.with(context).load(imagePath).into(productImage);

        int nameProdColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_NAME);
        String nameProduct = cursor.getString(nameProdColumnIndex);
        productName.setText(nameProduct);

        int idColumnIndex = cursor.getColumnIndex(PatisserieContract.PatisserieEntry._ID);
        int productId = cursor.getInt(idColumnIndex);
        view.setTag(productId);
    }
}
