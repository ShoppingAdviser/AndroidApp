package com.example.nidatazeen.shoppingadviser1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ShoppingCartActivity extends Activity implements GeneralDialogFragment.OnDialogFragmentClickListener {

    private List<ModelProducts> mCartList;
    private ProductAdapter mProductAdapter;
    DatabaseHandler db;
String qty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart);
        db = new DatabaseHandler(this);

        mCartList = db.getCartproducts(null, null, null);
        if(mCartList == null || mCartList.size() == 0) {
            Toast.makeText(ShoppingCartActivity.this, "No items in cart", Toast.LENGTH_LONG).show();
finish();
        }
        Bundle b = this.getIntent().getExtras();
        if(b!=null)
            qty = b.getSerializable("quantity").toString();
        // Create the list
        final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
        mProductAdapter = new ProductAdapter(mCartList,this, getLayoutInflater(), true);
        listViewCatalog.setAdapter(mProductAdapter);

        listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(ShoppingCartActivity.this, "item clicked", Toast.LENGTH_LONG).show();
                ModelProducts selectedProduct = mCartList.get(position);
                if (selectedProduct.getSelected() == 1)
                    selectedProduct.setSelected(0);
                else
                    selectedProduct.setSelected(1);
                db.updateProduct(selectedProduct);
                mCartList.remove(position);
                mProductAdapter.notifyDataSetInvalidated();
                mProductAdapter.notifyDataSetChanged();

            }
        });


        Button checkoutbutton = (Button) findViewById(R.id.checkoutbtn);
        checkoutbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences shoppingAdviserPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean isLoggedIn = shoppingAdviserPreferences.getBoolean("isLoggedIn", false);
                SharedPreferences.Editor editor = shoppingAdviserPreferences.edit();

                if (isLoggedIn) {
                    final Intent checkoutIntent = new Intent().setClass(ShoppingCartActivity.this, CheckoutActivity.class);

                    Bundle b = new Bundle();
                    b.putSerializable("quantity", qty);
                    checkoutIntent.putExtras(b);
                    startActivity(checkoutIntent);
                } else {
                    final Intent loginIntent = new Intent().setClass(ShoppingCartActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    Toast.makeText(ShoppingCartActivity.this,"You have to login first in order to proceed. If you haven't registered yourself then register first!",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button continueshpngbutton = (Button) findViewById(R.id.cartcontinueButton);
        continueshpngbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent continueshpngIntent = new Intent().setClass(ShoppingCartActivity.this, LandingPageActivity.class);
                startActivity(continueshpngIntent);

            }
        });
        Button removeButton = (Button) findViewById(R.id.ButtonRemoveFromCart);
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralDialogFragment generalDialogFragment =
                        GeneralDialogFragment.newInstance("Would you like to remove all the items from the cart?", "");
                generalDialogFragment.show(getFragmentManager(), "dialog");
            }
        });
    }


    public void removed() {


        for (int i = 0; i <= mCartList.size(); i++) {

            if (mCartList.size() == 1) i=0;

            int selectedValue = mCartList.get(i).getSelected();

            if (selectedValue == 1) {
                mCartList.get(i).setSelected(0);
                mCartList.get(i).setProductqty("1");
                db.updateProduct(mCartList.get(i));
                mCartList.remove(i);
                i=0;
                if (mCartList.size() == 0) break;
            }


        }

        mProductAdapter.notifyDataSetChanged();

    }



    @Override
    public void onOkClicked(GeneralDialogFragment dialog) {
        removed();
        // do your stuff
    }


    @Override
    public void onCancelClicked(GeneralDialogFragment dialog) {
        dialog.dismiss();
        // do your stuff
    }
}