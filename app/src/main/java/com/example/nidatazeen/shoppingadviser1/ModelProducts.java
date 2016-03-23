package com.example.nidatazeen.shoppingadviser1;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Len on 06-03-2016.
 */
public class ModelProducts implements Serializable {

    private String productTitle;
    private String productDescription;
    private String productPrice;
    private String productDiscountPrice;
    private int productId;
    private String soldby;
    private String category;
    private String tag;
    private String size;
    private String productSKU;
    private int rating;
    private String productImageUrl;
    private String productDetailedDescription;
    private String productAdditionalInfo;
    private String productSellerInfo;

    public ModelProducts(String productName,String productDesc,String productPrice, String productDiscount, int ratingValue, String soldbySeller, String productCategory, String prodTag, int prodID, String SKU, String productSizes, String imgUrl,String productDetailDesc,String productAddInfo,String prodSeller)
    {
        this.productTitle  = productName;
        this.productDescription  = productDesc;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscount;
        this.category = productCategory;
        this.tag = prodTag;
        this.soldby = soldbySeller;
        this.size = productSizes;
        this.productSKU = SKU;
        this.rating = ratingValue;
        this.productImageUrl = imgUrl;
        this.productId = prodID;
        this.productDetailedDescription=productDetailDesc;
        this.productAdditionalInfo=productAddInfo;
        this.productSellerInfo=prodSeller;
    }


    public String getProductTitle() {

        return productTitle;
    }

    public String getProductDescription() {

        return productDescription;
    }

    public String getProductPrice() {

        return productPrice;
    }

    public String getProductDiscountPrice() {

        return productDiscountPrice;
    }
    public String getProductSKU() {

        return productSKU;
    }
    public String getCategory() {

        return category;
    }
    public String getSize() {

        return size;
    }
    public String getProductImageUrl() {

        return productImageUrl;
    }
    public String getSoldby() {

        return soldby;
    }
    public String getTag() {

        return tag;
    }
    public int getProductId() {

        return productId;
    }

    public String getProductDetailedDescription()
    {
        return productDetailedDescription;
    }

    public String getAdditionalInfo()
    {
        return productAdditionalInfo;
    }
    public String getSellerInfo()
    {
        return productSellerInfo;
    }
}