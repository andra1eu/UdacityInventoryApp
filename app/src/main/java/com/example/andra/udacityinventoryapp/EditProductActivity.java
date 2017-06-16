package com.example.andra.udacityinventoryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andra.udacityinventoryapp.data.DatabaseWrapper;
import com.example.andra.udacityinventoryapp.data.PatisserieModel;

import static com.example.andra.udacityinventoryapp.MainActivity.PRODUCT_ID_EXTRA;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener {
    private int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private Button save;
    private Button add;
    private Button minus;
    private ImageView productImageView;
    private EditText productNameView;
    private TextView productQuantityView;
    private EditText productPriceView;
    private PatisserieModel model;
    private String finalProductName;
    private int finalProductPrice;
    private String uriPath;

    private int quantity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent i = getIntent();
        int id = i.getIntExtra(PRODUCT_ID_EXTRA, 0);

        DatabaseWrapper wrapper = new DatabaseWrapper(this);
        model = wrapper.getProduct(id);
        if (model == null){
            finish();
            return;
        }

        productImageView = (ImageView) findViewById(R.id.product_image);
        productNameView = (EditText) findViewById(R.id.product_name);
        productQuantityView = (TextView) findViewById(R.id.quantity_text_view);
        productPriceView = (EditText) findViewById(R.id.price_edit_text);

        Glide.with(this).load(model.getProductImage()).into(productImageView);
        productNameView.setText(model.getProductName());
        productQuantityView.setText(model.getProductQuantity() + "");
        quantity = model.getProductQuantity();
        productPriceView.setText(model.getProductPrice() + "");

        save = (Button) findViewById(R.id.save_product);
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
        productQuantityView.setText(String.valueOf(quantity));
    }

    public void savedProduct(View view) {
        finalProductName = productNameView.getText().toString();
        finalProductPrice = Integer.parseInt(productPriceView.getText().toString());

        model.setProductImage(uriPath);
        model.setProductName(finalProductName);
        model.setProductQuantity(quantity);
        model.setProductPrice(finalProductPrice);

        DatabaseWrapper wrapper = new DatabaseWrapper(this);
        wrapper.updateProduct(model);
        finish();
    }
}
