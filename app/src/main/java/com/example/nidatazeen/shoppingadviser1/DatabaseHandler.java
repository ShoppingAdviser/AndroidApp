package com.example.nidatazeen.shoppingadviser1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.IntegerRes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements Serializable {
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHandler instance;
    private static Context context;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "dbManager";
    private static final String TABLE_CONTACTS = "contact";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";


    private static final String TABLE_PRODUCTS = "product";


    private static final String KEY_PRODUCT_TITLE = "prodtitle";
    private static final String KEY_PRODUCT_DESCRIPTION = "proddescr";
    private static final String KEY_PRODUCT_PRICE = "productprice";
    private static final String KEY_PRODUCT_DISCOUNTPRICE = "proddiscprice";
    private static final String KEY_PRODUCT_ID = "productid";
    private static final String KEY_PRODUCT_SOLDBY = "prodsoldby";
    private static final String KEY_PRODUCT_CATEGORY = "prodcategry";
    private static final String KEY_PRODUCT_TAG = "prodtag";
    private static final String KEY_PRODUCT_SIZE = "prodsize";
    private static final String KEY_PRODUCT_SKU = "prodsku";
    private static final String KEY_PRODUCT_RATING = "prodrating";
    private static final String KEY_PRODUCT_IMAGEURL = "prodimgurl";
    private static final String KEY_PRODUCT_DETAILED_DESCRIPTION = "proddetaildescr";
    private static final String KEY_PRODUCT_ADDITIONAL_INFO = "prodaddinfo";
    private static final String KEY_PRODUCT_SELLER_INFO = "prodsellerinfo";
    private static final String KEY_PRODUCT_GRID_IMAGES = "prodgridimage";

    //    private DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        //3rd argument to be passed is CursorFactory ins+
//        // tance
//    }
    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory ins+
        // tance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" + KEY_PRODUCT_TITLE + " TEXT," + KEY_PRODUCT_DESCRIPTION + " TEXT,"
                + KEY_PRODUCT_PRICE + " TEXT," + KEY_PRODUCT_DISCOUNTPRICE + " TEXT," + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_RATING + " INTEGER," + KEY_PRODUCT_SOLDBY + " TEXT,"
                + KEY_PRODUCT_CATEGORY + " TEXT," + KEY_PRODUCT_TAG + " TEXT," + KEY_PRODUCT_SIZE + " TEXT," + KEY_PRODUCT_SKU + " TEXT,"
                + KEY_PRODUCT_IMAGEURL + " TEXT," + KEY_PRODUCT_DETAILED_DESCRIPTION + " TEXT,"
                + KEY_PRODUCT_ADDITIONAL_INFO + " TEXT," + KEY_PRODUCT_SELLER_INFO + " TEXT," + KEY_PRODUCT_GRID_IMAGES + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PRODUCTS);
        onCreate(db);
    }

    // code to add the new contact

    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                //    contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contact contact) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    //PRODUCTS DETAILS BELOW

    // code to add the new product

    long addProduct(ModelProducts product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_TITLE, product.getProductTitle()); //Product title
        values.put(KEY_PRODUCT_DESCRIPTION, product.getProductDescription()); //Product description
        values.put(KEY_PRODUCT_PRICE, product.getProductPrice()); //Product price
        values.put(KEY_PRODUCT_DISCOUNTPRICE, product.getProductDiscountPrice()); //Product discount price
        values.put(KEY_PRODUCT_ID, product.getProductId());
        values.put(KEY_PRODUCT_RATING, product.getRating()); //Product rating
        values.put(KEY_PRODUCT_SOLDBY, product.getSoldby()); //Product soldby
        values.put(KEY_PRODUCT_CATEGORY, product.getCategory()); //Product category
        values.put(KEY_PRODUCT_TAG, product.getTag()); //Product tag
        values.put(KEY_PRODUCT_SIZE, product.getSize()); //Product size
        values.put(KEY_PRODUCT_SKU, product.getProductSKU()); //Product sku
        values.put(KEY_PRODUCT_IMAGEURL, product.getProductImageUrl()); //Product imageurl
        values.put(KEY_PRODUCT_DETAILED_DESCRIPTION, product.getProductDetailedDescription()); //Product detaileddescrp
        values.put(KEY_PRODUCT_ADDITIONAL_INFO, product.getAdditionalInfo()); //Product additionalinfo
        values.put(KEY_PRODUCT_SELLER_INFO, product.getSellerInfo()); //Product sellerinfo
        values.put(KEY_PRODUCT_GRID_IMAGES, product.getProductGridImages());
        // Inserting Row
        long val = db.insert(TABLE_PRODUCTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return val;
    }


    // code to get the single product
    ModelProducts getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[]{KEY_PRODUCT_TITLE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_PRICE,
                        KEY_PRODUCT_DISCOUNTPRICE, KEY_PRODUCT_ID, KEY_PRODUCT_RATING, KEY_PRODUCT_SOLDBY, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_TAG,
                        KEY_PRODUCT_SIZE, KEY_PRODUCT_SKU, KEY_PRODUCT_IMAGEURL, KEY_PRODUCT_DETAILED_DESCRIPTION
                        , KEY_PRODUCT_ADDITIONAL_INFO, KEY_PRODUCT_SELLER_INFO, KEY_PRODUCT_GRID_IMAGES
                }, KEY_PRODUCT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ModelProducts products = new ModelProducts((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15));
        // return product
        return products;
    }

    // code to get all contacts in a list view
    public List<ModelProducts> getAllProducts() {
        List<ModelProducts> productList = new ArrayList<ModelProducts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
//                ModelProducts products = new ModelProducts(cursor.getString(0),
//                        cursor.getString(1), cursor.getString(2),
//                        cursor.getString(3),Integer.parseInt(cursor.getString(4)),
//                        Integer.parseInt(cursor.getString(5)),cursor.getString(6),cursor.getString(7),cursor.getString(8),
//                        cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12), cursor.getString(13), cursor.getString(14));

                ModelProducts products = new ModelProducts();
                products.setProductTitle(cursor.getString(0));
                products.setProductDescription(cursor.getString(1));
                products.setProductPrice(cursor.getString(2));
                products.setProductDiscountPrice(cursor.getString(3));
                products.setProductId(Integer.parseInt(cursor.getString(4)));
                products.setRating(Integer.parseInt(cursor.getString(5)));
                products.setSoldby(cursor.getString(6));
                products.setCategory(cursor.getString(7));
                products.setTag(cursor.getString(8));
                products.setProductSKU(cursor.getString(9));
                products.setSize(cursor.getString(10));
                products.setProductImageUrl(cursor.getString(11));
                products.setProductDetailedDescription(cursor.getString(12));
                products.setProductAdditionalInfo(cursor.getString(13));
                products.setProductSellerInfo(cursor.getString(14));
                products.setProductGridImages(cursor.getString(15));

                // Adding contact to list
                productList.add(products);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productList;
    }

    // code to update the single contact
    public int updateProduct(ModelProducts product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_TITLE, product.getProductTitle());
        values.put(KEY_PRODUCT_DESCRIPTION, product.getProductDescription()); //Product description
        values.put(KEY_PRODUCT_PRICE, product.getProductPrice()); //Product price
        values.put(KEY_PRODUCT_DISCOUNTPRICE, product.getProductDiscountPrice()); //Product discount price
        values.put(KEY_PRODUCT_ID, product.getProductId());
        values.put(KEY_PRODUCT_RATING, product.getRating()); //Product rating
        values.put(KEY_PRODUCT_SOLDBY, product.getSoldby()); //Product soldby
        values.put(KEY_PRODUCT_CATEGORY, product.getCategory()); //Product category
        values.put(KEY_PRODUCT_TAG, product.getTag()); //Product tag
        values.put(KEY_PRODUCT_SIZE, product.getSize()); //Product size
        values.put(KEY_PRODUCT_SKU, product.getProductSKU()); //Product sku
        values.put(KEY_PRODUCT_IMAGEURL, product.getProductImageUrl()); //Product imageurl
        values.put(KEY_PRODUCT_DETAILED_DESCRIPTION, product.getProductDetailedDescription()); //Product detaileddescrp
        values.put(KEY_PRODUCT_ADDITIONAL_INFO, product.getAdditionalInfo()); //Product additionalinfo
        values.put(KEY_PRODUCT_SELLER_INFO, product.getSellerInfo()); //Product sellerinfo
        values.put(KEY_PRODUCT_GRID_IMAGES, product.getProductGridImages());
        // updating row
        return db.update(TABLE_PRODUCTS, values, KEY_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProductId())});
    }

    // Deleting single contact
    public void deleteProduct(ModelProducts product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProductId())});
        db.close();
    }

    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        cursor.close();

        // return count
        return cnt;
    }
   /* @Override
   public synchronized void close() {
       if (instance != null)
           db.close();
   }

    private static synchronized DatabaseHandler getInstance(Context context) {
        SQLiteDatabase db;
        private static Context context;

       if (instance == null) {
            instance = new DatabaseHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
            db = instance.getWritableDatabase();
        }

        return instance;
   }*/


    public List<ModelProducts> getWordMatches(String query, String[] columns) {
        String selection = query;//KEY_PRODUCT_TITLE + " MATCH ?";
        String[] selectionArgs = null;//new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private List<ModelProducts> query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE_PRODUCTS);
        List<ModelProducts> productList = new ArrayList<ModelProducts>();

//        Cursor cursor = builder.query(this.getReadableDatabase(),
//                columns, selection, selectionArgs, null, null, null);
        Cursor cursor = builder.query(this.getReadableDatabase(), null, KEY_PRODUCT_TITLE + " LIKE ?", new String[]{"%" + selection + "%"},
                null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
//                ModelProducts products = new ModelProducts(cursor.getString(0),
//                        cursor.getString(1), cursor.getString(2),
//                        cursor.getString(3),Integer.parseInt(cursor.getString(4)),
//                        Integer.parseInt(cursor.getString(5)),cursor.getString(6),cursor.getString(7),cursor.getString(8),
//                        cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12), cursor.getString(13), cursor.getString(14));

                ModelProducts products = new ModelProducts();
                products.setProductTitle(cursor.getString(0));
                products.setProductDescription(cursor.getString(1));
                products.setProductPrice(cursor.getString(2));
                products.setProductDiscountPrice(cursor.getString(3));
                products.setProductId(Integer.parseInt(cursor.getString(4)));
                products.setRating(Integer.parseInt(cursor.getString(5)));
                products.setSoldby(cursor.getString(6));
                products.setCategory(cursor.getString(7));
                products.setTag(cursor.getString(8));
                products.setProductSKU(cursor.getString(9));
                products.setSize(cursor.getString(10));
                products.setProductImageUrl(cursor.getString(11));
                products.setProductDetailedDescription(cursor.getString(12));
                products.setProductAdditionalInfo(cursor.getString(13));
                products.setProductSellerInfo(cursor.getString(14));
                products.setProductGridImages(cursor.getString(15));


                // Adding contact to list
                productList.add(products);
            } while (cursor.moveToNext());
        }

        // return contact list
        return productList;
//        return cursor;
    }
}