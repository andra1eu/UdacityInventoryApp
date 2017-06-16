package com.example.andra.udacityinventoryapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class PatisserieModel implements Parcelable {

    private int id;
    private String productImage;
    private String productName;
    private int productQuantity;
    private int productPrice;

    public PatisserieModel(int id, String productImage, String productName, int productQuantity, int productPrice) {
        this.id = id;
        this.productImage = productImage;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    private PatisserieModel(Parcel in) {
        id = in.readInt();
        productImage = in.readString();
        productName = in.readString();
        productQuantity = in.readInt();
        productPrice = in.readInt();
    }

    public static final Creator<PatisserieModel> CREATOR = new Creator<PatisserieModel>() {
        @Override
        public PatisserieModel createFromParcel(Parcel in) {
            return new PatisserieModel(in);
        }

        @Override
        public PatisserieModel[] newArray(int size) {
            return new PatisserieModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(productImage);
        dest.writeString(productName);
        dest.writeInt(productQuantity);
        dest.writeInt(productPrice);
    }
}
