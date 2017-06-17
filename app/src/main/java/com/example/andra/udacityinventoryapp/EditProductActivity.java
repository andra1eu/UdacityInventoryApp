package com.example.andra.udacityinventoryapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    int id;
    DatabaseWrapper wrapper;
    private int quantity;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Intent i = getIntent();
        id = i.getIntExtra(PRODUCT_ID_EXTRA, 0);
        wrapper = new DatabaseWrapper(this);
        model = wrapper.getProduct(id);
        if (model == null) {
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

        add = (Button) findViewById(R.id.add_button);
        minus = (Button) findViewById(R.id.minus_button);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                savedProduct();
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void savedProduct() {
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

    public void deleteProduct(View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int productId = id;
                        wrapper.deleteProduct(productId);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void orderProduct(View view) {
        String emailProductName = model.getProductName();
        int emailProductQuantityInt = model.getProductQuantity();
        String emailProductQuantity = emailProductQuantityInt + "";
        int emailProductPriceInt = model.getProductPrice();
        String emailProductPrice = emailProductPriceInt + "";
        String message = "Product Name: " + emailProductName + "\n" +
                        "Product Quantity: " + emailProductQuantity + "\n" +
                        "Product Price: " + emailProductPrice + " $";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: andralung@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
