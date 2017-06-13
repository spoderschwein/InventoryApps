package com.example.android.inventoryapps;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapps.data.InventoryContract;


public class InventoryCursorAdapter extends CursorAdapter {

    private final onButtonPressedListener mButtonListener;
    private Context context;

    public InventoryCursorAdapter(Context context, Cursor c, onButtonPressedListener buttonListener) {
        super(context, c, 0 /* flags */);
        this.mButtonListener = buttonListener;
    }

    // set new View
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    //set binView
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.text_view_product);
        TextView quantityTextView = (TextView) view.findViewById(R.id.text_view_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.text_view_price);
        Button buttonSellListView = (Button) view.findViewById(R.id.button_sell_listview);

        //Find the columns of pet attributes that we´re interested in
        int idColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE);

        //Read the product attributes from the Cursor for the current product
        final int id = cursor.getInt(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        final String priceProduct = cursor.getString(priceColumnIndex);

        //Update the TextView with the attributes for the current product
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(priceProduct);

        buttonSellListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonListener.onButtonPressed(id, productQuantity);
            }
        });
    }

    interface onButtonPressedListener {
        void onButtonPressed(int id, String productQuantity);
    }
}