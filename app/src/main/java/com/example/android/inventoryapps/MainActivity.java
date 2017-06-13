package com.example.android.inventoryapps;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.inventoryapps.data.InventoryContract;
import static com.example.android.inventoryapps.data.InventoryContract.ProductEntry.buildUri;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, InventoryCursorAdapter.onButtonPressedListener {


    private static final int PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// declare save button
        Button saveItem = (Button) findViewById(R.id.saveButton);
//        set an onclicklistener on the button
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set intent for editorActivity
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the product data
        ListView productListView = (ListView) findViewById(R.id.list);

//      set Adapter
        mCursorAdapter = new InventoryCursorAdapter(this, null, this);
        productListView.setAdapter(mCursorAdapter);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

//  set an onclicklistener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//          set intent
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });
        //Kick off the Loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.ProductEntry._ID,
                InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE};

        //This loader will execute the ContentProvider`s query method on a background thread
        return new CursorLoader(this, //Parent activity context
                InventoryContract.ProductEntry.CONTENT_URI, // provider content URI to query
                projection, // Columns to include in the resulting Cursor
                null, //No selection clause
                null, //No selection arguments
                null); //Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onButtonPressed(int id, String productQuantity) {

        ContentValues values = new ContentValues();
        int quantity = 0;
        if (!TextUtils.isEmpty(productQuantity)) {
            int actualQuantity = Integer.parseInt(productQuantity);
            if (actualQuantity == 0) {
                Toast.makeText(this, getString(R.string.editor_quantity_below_zero), Toast.LENGTH_SHORT).show();
                return;
            }
            quantity = (actualQuantity - 1);
        }
        values.put(InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

        mCurrentProductUri = buildUri(String.valueOf(id));

        int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.editor_update_item_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_update_item_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}


