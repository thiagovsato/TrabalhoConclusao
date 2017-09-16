package br.com.thiago.trabalhoconclusao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.com.thiago.trabalhoconclusao.model.Price;

public class PriceDAO {

    private SQLiteHelper sqliteHelper;

    public PriceDAO(Context context) {
        sqliteHelper = new SQLiteHelper(context);
    }

    public boolean insert(Price price) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Price.KEY_PRODUCT_ID, price.getProduct_id());
        values.put(Price.KEY_STORE_ID, price.getStore_id());
        values.put(Price.KEY_PRICE, price.getPrice());

        long price_Id = db.insert(Price.TABLE, null, values);
        db.close();

        if(price_Id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void delete(int price_Id) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.delete(Price.TABLE, Price.KEY_ID + "= ?", new String[] { String.valueOf(price_Id) });
        db.close();
    }

    public void update(Price price) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Price.KEY_PRODUCT_ID, price.getProduct_id());
        values.put(Price.KEY_STORE_ID, price.getStore_id());
        values.put(Price.KEY_PRICE, price.getPrice());

        db.update(Price.TABLE, values, Price.KEY_ID + "= ?", new String[] { String.valueOf(price.getPrice_Id()) });
        db.close();
    }

    public List<Price> getPricesByProductId(int ID){
        List<Price> prices = new LinkedList<>();

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        String selectQuery =  "SELECT  *"
                + " FROM " + Price.TABLE
                + " WHERE " + Price.KEY_PRODUCT_ID + "=?";

        Price price = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                price = new Price();
                price.setPrice_Id(cursor.getInt(cursor.getColumnIndex(Price.KEY_ID)));
                price.setProduct_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRODUCT_ID)));
                price.setStore_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_STORE_ID)));
                price.setPrice(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRICE)));
                prices.add(price);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return prices;
    }

    public List<Price> getPricesByStoreId(int ID){
        List<Price> prices = new LinkedList<>();

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        String selectQuery =  "SELECT  *"
                + " FROM " + Price.TABLE
                + " WHERE " + Price.KEY_STORE_ID + "=?";

        Price price = null;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                price = new Price();
                price.setPrice_Id(cursor.getInt(cursor.getColumnIndex(Price.KEY_ID)));
                price.setProduct_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRODUCT_ID)));
                price.setStore_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_STORE_ID)));
                price.setPrice(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRICE)));
                prices.add(price);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return prices;
    }

    public Price getPriceByID(int ID){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Price.KEY_ID + "," +
                Price.KEY_PRODUCT_ID+ "," +
                Price.KEY_STORE_ID+ "," +
                Price.KEY_PRICE+
                " FROM " + Price.TABLE
                + " WHERE " + Price.KEY_ID + "=?";

        Price price = new Price();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                price.setPrice_Id(cursor.getInt(cursor.getColumnIndex(Price.KEY_ID)));
                price.setProduct_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRODUCT_ID)));
                price.setStore_id(cursor.getInt(cursor.getColumnIndex(Price.KEY_STORE_ID)));
                price.setPrice(cursor.getInt(cursor.getColumnIndex(Price.KEY_PRICE)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return price;
    }

    public boolean doesPriceExistByProductId(int _product_Id){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Price.TABLE
                +" WHERE " + Price.KEY_PRODUCT_ID + " = \"" + _product_Id +"\";";
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

    public boolean doesPriceExistByStoreId(int _store_Id){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Price.TABLE
                +" WHERE " + Price.KEY_STORE_ID + " = \"" + _store_Id +"\";";
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