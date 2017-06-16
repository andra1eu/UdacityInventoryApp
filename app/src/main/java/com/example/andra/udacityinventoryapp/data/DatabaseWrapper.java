package com.example.andra.udacityinventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.andra.udacityinventoryapp.data.PatisserieContract.PatisserieEntry.COLUMN_IMAGE_PRODUCT;
import static com.example.andra.udacityinventoryapp.data.PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_NAME;
import static com.example.andra.udacityinventoryapp.data.PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_PRICE;
import static com.example.andra.udacityinventoryapp.data.PatisserieContract.PatisserieEntry.COLUMN_PRODUCT_QUANTITY;
import static com.example.andra.udacityinventoryapp.data.PatisserieContract.PatisserieEntry.TABLE_NAME;

public class DatabaseWrapper {

    private static SQLiteDatabase db;

    public DatabaseWrapper(Context context) {
        if (db == null) {
            db = new PatisserieDbHelper(context).getWritableDatabase();
        }
    }

    public Cursor getCursorWithAllData() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteProduct(int id) {
        String whereClause = _ID + "=?";
        String[] whereArgs = new String[]{id + ""};
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public PatisserieModel getProduct(int id) {
        String selection = _ID + "=?";
        String[] selectionArgs = new String[]{id + ""};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String productImage = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PRODUCT));
            String productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE));
            cursor.close();

            return new PatisserieModel(id, productImage, productName, quantity, price);
        }
        return null;
    }

    public void updateProduct(PatisserieModel model) {

        ContentValues values = new ContentValues();
        if (model.getProductImage() != null)
            values.put(COLUMN_IMAGE_PRODUCT, model.getProductImage());
        if (model.getProductName() != null) values.put(COLUMN_PRODUCT_NAME, model.getProductName());
        values.put(COLUMN_PRODUCT_QUANTITY, model.getProductQuantity());
        values.put(COLUMN_PRODUCT_PRICE, model.getProductPrice());

        String whereClause = _ID + "=?";
        String[] whereArgs = new String[]{model.getId() + ""};

        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public void insertNewProduct(PatisserieModel model) {

        ContentValues values = new ContentValues();
        if (model.getProductImage() != null)
            values.put(COLUMN_IMAGE_PRODUCT, model.getProductImage());
        if (model.getProductName() != null) values.put(COLUMN_PRODUCT_NAME, model.getProductName());
        values.put(COLUMN_PRODUCT_QUANTITY, model.getProductQuantity());
        values.put(COLUMN_PRODUCT_PRICE, model.getProductPrice());

        db.insert(TABLE_NAME, null, values);
    }

    private class PatisserieDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "patisserie.db";
        private static final int DATABASE_VERSION = 1;

        PatisserieDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_PATISSERIE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_PRODUCT + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL); ";

            db.execSQL(SQL_CREATE_PATISSERIE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
