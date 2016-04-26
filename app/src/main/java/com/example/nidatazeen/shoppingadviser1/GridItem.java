package com.example.nidatazeen.shoppingadviser1;

import android.widget.Button;

public class GridItem {
    private String image;
    private String title;
    private String discprice;
    private String price;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDiscountPrice()
    {
        return discprice;
    }
    public void setDiscountPrice(String discprice)
    {
        this.discprice=discprice;
    }
    public String getPrice()
    {
        return price;
    }
    public void setPrice(String price)
    {
        this.price=price;
    }

}
