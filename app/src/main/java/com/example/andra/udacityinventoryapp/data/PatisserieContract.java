package com.example.andra.udacityinventoryapp.data;

import android.provider.BaseColumns;

public class PatisserieContract {

    private PatisserieContract(){}

    public static final class PatisserieEntry implements BaseColumns {

        public static final String TABLE_NAME = "patisserie";

        public static final String COLUMN_IMAGE_PRODUCT = "image";
        public static final String COLUMN_PRODUCT_NAME = "product";
        public static final String COLUMN_PRODUCT_QUANTITY  = "quantity";
        public static final String COLUMN_PRODUCT_PRICE  = "price";

    }
}
