package com.example.nidatazeen.shoppingadviser1;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductAdapter extends BaseAdapter {

    private List<ModelProducts> mProductList;
    private LayoutInflater mInflater;
    private boolean mShowCheckbox;
    private Context mContext;

    public ProductAdapter(List<ModelProducts> list, Context ctx,LayoutInflater inflater, boolean showCheckbox) {
        mProductList = list;
        mInflater = inflater;
        mShowCheckbox = showCheckbox;
        mContext = ctx;
    }

    @Override
    public int getCount() {
        if (mProductList == null) return 1;
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_cart, null);
            item = new ViewItem();
            item.productImageView = (ImageView) convertView.findViewById(R.id.cartproductImage);
            item.productTitle = (TextView) convertView.findViewById(R.id.carttitletextview);
//            item.productCheckbox = (CheckBox) convertView.findViewById(R.id.CheckBoxSelected);
            item.productsku = (TextView) convertView.findViewById(R.id.cartsku);
            item.productQuantity = (TextView) convertView.findViewById(R.id.quantitytext);
item.productQuantityValue = (EditText)convertView.findViewById(R.id.editqtyText);
            convertView.setTag(item);
            } else {
            item = (ViewItem) convertView.getTag();
        }
        ModelProducts curProduct = mProductList.get(position);
        Picasso.with(mContext).load(curProduct.getProductImageUrl()).into(item.productImageView);
        item.productTitle.setText(curProduct.getProductTitle());
        item.productsku.setText("SKU : "+ curProduct.getProductSKU());
item.productQuantityValue.setText(curProduct.getProductqty());
//        if(!mShowCheckbox) {
//            item.productCheckbox.setVisibility(View.GONE);
//        } else {
//            if(curProduct.getSelected() == 1)
//            item.productCheckbox.setChecked(true);
//            else
//            item.productCheckbox.setChecked(false);
//        }
        return convertView;
        }

    private class ViewItem {
        ImageView productImageView;
        TextView productTitle;
        TextView productsku;
        TextView productQuantity;
        TextView productQuantityValue;
//        CheckBox productCheckbox;
    }
}
