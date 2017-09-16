package br.com.thiago.trabalhoconclusao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Product;

public class ProductDAO {

    private SQLiteHelper sqliteHelper;

    public ProductDAO(Context context) {
        sqliteHelper = new SQLiteHelper(context);
    }

    public boolean insert(Product product) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Product.KEY_NAME, product.getName());
        values.put(Product.KEY_DESCRIPTION, product.getDescription());
        values.put(Product.KEY_IMAGE_URL, product.getImage_url());

        long product_Id = db.insert(Product.TABLE, null, values);
        db.close();

        if(product_Id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void delete(int product_Id) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.delete(Product.TABLE, Product.KEY_ID + "= ?", new String[] { String.valueOf(product_Id) });
        db.close();
    }

    public void deleteWithPrices(int product_Id) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        String selectQuery =  "SELECT  *"
                + " FROM " + Price.TABLE
                + " WHERE " + Price.KEY_PRODUCT_ID + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(product_Id) } );

        if (cursor.moveToFirst()) {
            do {
                db.delete(Price.TABLE, Price.KEY_ID + "= ?", new String[] { cursor.getString(cursor.getColumnIndex(Price.KEY_ID)) });
            } while (cursor.moveToNext());
        }

        db.delete(Product.TABLE, Product.KEY_ID + "= ?", new String[] { String.valueOf(product_Id) });
        db.close();
        cursor.close();
    }

    public void update(Product product) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Product.KEY_NAME, product.getName());
        values.put(Product.KEY_DESCRIPTION, product.getDescription());
        values.put(Product.KEY_IMAGE_URL, product.getImage_url());

        db.update(Product.TABLE, values, Product.KEY_ID + "= ?", new String[] { String.valueOf(product.getProduct_Id()) });
        db.close();
    }

    public List<Product> getAll() {
        List<Product> products = new LinkedList<>();

        String query = "SELECT  * FROM " + Product.TABLE;

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Product product = null;
        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setProduct_Id(cursor.getInt(cursor.getColumnIndex(Product.KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.KEY_DESCRIPTION)));
                product.setImage_url(cursor.getString(cursor.getColumnIndex(Product.KEY_IMAGE_URL)));

                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    public Product getProductByID(int ID){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Product.KEY_ID + "," +
                Product.KEY_NAME+ "," +
                Product.KEY_DESCRIPTION+ "," +
                Product.KEY_IMAGE_URL+
                " FROM " + Product.TABLE
                + " WHERE " + Product.KEY_ID + "=?";

        Product product = new Product();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                product.setProduct_Id(cursor.getInt(cursor.getColumnIndex(Product.KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.KEY_DESCRIPTION)));
                product.setImage_url(cursor.getString(cursor.getColumnIndex(Product.KEY_IMAGE_URL)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return product;
    }

    public Product getProductByName(String name){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Product.KEY_ID + "," +
                Product.KEY_NAME+ "," +
                Product.KEY_DESCRIPTION+ "," +
                Product.KEY_IMAGE_URL+
                " FROM " + Product.TABLE
                + " WHERE " + Product.KEY_NAME + "=?";

        Product product = new Product();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(name) } );

        if (cursor.moveToFirst()) {
            do {
                product.setProduct_Id(cursor.getInt(cursor.getColumnIndex(Product.KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(Product.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.KEY_DESCRIPTION)));
                product.setImage_url(cursor.getString(cursor.getColumnIndex(Product.KEY_IMAGE_URL)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return product;
    }

    public boolean doesProductExistByName(String product_name){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Product.TABLE
                +" WHERE " + Product.KEY_NAME + " = \"" + product_name +"\";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public boolean doesProductExistByNameOnEdit(String product_name, int product_id){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Product.TABLE
                +" WHERE " + Product.KEY_NAME + " = \"" + product_name +
                "\" AND " + Product.KEY_ID + " != \"" + product_id +"\";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }
    
}
