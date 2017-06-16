package com.example.andra.udacityinventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andra.udacityinventoryapp.data.DatabaseWrapper;
import com.example.andra.udacityinventoryapp.data.PatisserieModel;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private int PICK_IMAGE_REQUEST = 1;

    private ImageView productImageView;
    private EditText productNameView;
    private TextView productQuantityView;
    private EditText productPriceView;
    private Button add;
    private Button minus;
    private int quantity = 0;
    private Uri uri;
    private String uriPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImageView = (ImageView) findViewById(R.id.product_image);
        productNameView = (EditText) findViewById(R.id.product_name);
        productQuantityView = (TextView) findViewById(R.id.quantity_text_view);
        productPriceView = (EditText) findViewById(R.id.price_edit_text);
        add = (Button) findViewById(R.id.add_button);
        minus = (Button) findViewById(R.id.minus_button);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);
    }

    public void onImageClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null && intent.getData() != null) {
            uri = intent.getData();
            uriPath = getRealPathFromURI(uri);
            productImageView.setImageURI(uri);
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, projection, null, null, null);
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                if (quantity < 20) quantity++;
                break;
            case R.id.minus_button:
                if (quantity > 0) quantity--;
                break;
        }
        productQuantityView.setText(quantity + "");
    }

    public void savedProduct(View view){
        if (uriPath == null){
            Toast.makeText(this, "Please add an image!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (productNameView.getText().toString().isEmpty()){
            Toast.makeText(this, "Please write a name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity == 0){
            Toast.makeText(this, "Please add the quantity!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (productPriceView.getText().toString().isEmpty()){
            Toast.makeText(this, "Please write the price!", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseWrapper wrapper = new DatabaseWrapper(this);
        PatisserieModel model = new PatisserieModel(0, uriPath, productNameView.getText().toString(),
                quantity, Integer.parseInt(productPriceView.getText().toString()));
        wrapper.insertNewProduct(model);
        finish();
    }


}