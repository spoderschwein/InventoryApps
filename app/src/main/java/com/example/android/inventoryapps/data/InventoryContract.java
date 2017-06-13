package com.example.android.inventoryapps.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class InventoryContract {

    private InventoryContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapps";
    public static final String PATH_PRODUCTS = "inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static Uri buildUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        //set table name
        public final static String TABLE_NAME = "products";

        //set columns names
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_IMAGE_PATH = "imageFileName";
        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "nameSupplier";
        public final static String COLUMN_PRODUCT_SUPPLIER_EMAIL = "emailSupplier";
    }

}