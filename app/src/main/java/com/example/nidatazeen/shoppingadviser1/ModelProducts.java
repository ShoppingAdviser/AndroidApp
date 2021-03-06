package com.example.nidatazeen.shoppingadviser1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Len on 06-03-2016.
 */
public class ModelProducts implements Serializable {

    private String productTitle;
    private String productDescription;
    private String productPrice;
    private String productDiscountPrice;
    private int productId;
    private int productIdentifier;
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
    private String productGridImages;
    private int selected;
    private String productqty;


    public ModelProducts(String productName,String productDesc,String productPrice, String productDiscount, int prodID,int ratingValue, String soldbySeller, String prodCategory, String prodTag, String productSizes,String SKU, String imgUrl,String productDetailDesc,String productAddInfo,String prodSeller,String prodImages, int prductIdentifier, int isSelected, String qty)
    {
        this.productTitle  = productName;
        this.productDescription  = productDesc;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscount;
        this.category = prodCategory;
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
        this.productGridImages=prodImages;
        this.productIdentifier = prductIdentifier;
        this.selected = isSelected;
        this.productqty = qty;
    }
public ModelProducts () {

}


    public String  getProductqty() {
        return productqty;
    }
    public void setProductqty(String pqty) { this.productqty = pqty; }
public int getSelected() {
        return selected;
    }
    public void setSelected(int isselected) { this.selected = isselected; }

    public String getProductTitle() {

        return productTitle;
    }

    public void setProductTitle(String productTitle){
        this.productTitle = productTitle;
    }

    public String getProductDescription() {

        return productDescription;
    }
    public void setProductDescription(String productDescription){
        this.productDescription = productDescription;
    }

    public String getProductPrice() {

        return productPrice;
    }
    public void setProductPrice(String productPrice){
        this.productPrice = productPrice;
    }
    public String getProductDiscountPrice() {

        return productDiscountPrice;
    }
    public void setProductDiscountPrice(String productDiscountPrice){
        this.productDiscountPrice = productDiscountPrice;
    }

    public String getProductSKU() {

        return productSKU;
    }
    public void setProductSKU(String productSKU){
        this.productSKU = productSKU;
    }

    public int getRating(){

        return rating;
    }
    public void setRating(int rating){
        this.rating = rating;
    }

    public String getCategory() {

        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getSize() {

        return size;
    }
    public void setSize(String size){
        this.size = size;
    }

    public String getProductImageUrl() {

        return productImageUrl;
    }
    public void setProductImageUrl(String productImageUrl){
        this.productImageUrl = productImageUrl;
    }

    public String getSoldby() {

        return soldby;
    }
    public void setSoldby(String soldby){
        this.soldby = soldby;
    }

    public String getTag() {

        return tag;
    }
    public void setTag(String tag){
        this.tag = tag;
    }

    public int getProductId() {

        return productId;
    }

    public void setProductId(int id)
    {
        this.productId = id;
    }

    public int getProductIdentifier() {

        return productIdentifier;
    }

    public void setProductIdentifier(int id)
    {
        this.productIdentifier = id;
    }

    public String getProductDetailedDescription()
    {

        return productDetailedDescription;
    }
    public void setProductDetailedDescription(String productDetailedDescription){
        this.productDetailedDescription = productDetailedDescription;
    }

    public String getAdditionalInfo()
    {
        return productAdditionalInfo;
    }
    public void setProductAdditionalInfo(String productAdditionalInfo){
        this.productAdditionalInfo = productAdditionalInfo;
    }

    public String getSellerInfo()
    {
        return productSellerInfo;
    }
    public void setProductSellerInfo(String productSellerInfo){
        this.productSellerInfo = productSellerInfo;
    }
   public String getProductGridImages()
   {
       return productGridImages;
   }

    public void setProductGridImages(String productGridImages)
    {
        this.productGridImages = productGridImages;
    }
}