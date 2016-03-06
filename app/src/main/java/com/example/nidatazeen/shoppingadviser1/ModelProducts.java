package com.example.nidatazeen.shoppingadviser1;

/**
 * Created by Len on 06-03-2016.
 */
public class ModelProducts {
    private String productTitle;
    private String productDescription;
    private float productPrice;
    private float productDiscountPrice;
    private int productId;
    private String soldby;
    private String category;
    private String tag;
    private String size;
    private String productSKU;
    private int rating;
    private String productImageUrl;

    public ModelProducts(String productName,String productDesc,float productPrice, float productDiscount, int ratingValue, String soldbySeller, String productCategory, String prodTag, int prodID, String SKU, String productSizes, String imgUrl)
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
    }

    public String getProductTitle() {

        return productTitle;
    }

    public String getProductDescription() {

        return productDescription;
    }

    public float getProductPrice() {

        return productPrice;
    }

    public float getProductDiscountPrice() {

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
}
