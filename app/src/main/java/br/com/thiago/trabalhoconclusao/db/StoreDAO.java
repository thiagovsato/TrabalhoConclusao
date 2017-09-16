package br.com.thiago.trabalhoconclusao.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Store;

public class StoreDAO {

    private SQLiteHelper sqliteHelper;

    public StoreDAO(Context context) {
        sqliteHelper = new SQLiteHelper(context);
    }

    public boolean insert(Store store) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Store.KEY_NAME, store.getName());
        values.put(Store.KEY_DESCRIPTION, store.getDescription());
        values.put(Store.KEY_IMAGE_URL, store.getImage_url());

        long store_Id = db.insert(Store.TABLE, null, values);
        db.close();

        if(store_Id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void delete(int store_Id) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        db.delete(Store.TABLE, Store.KEY_ID + "= ?", new String[] { String.valueOf(store_Id) });
        db.close();
    }

    public void deleteWithPrices(int store_Id) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        String selectQuery =  "SELECT  *"
                + " FROM " + Price.TABLE
                + " WHERE " + Price.KEY_STORE_ID + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(store_Id) } );

        if (cursor.moveToFirst()) {
            do {
                db.delete(Price.TABLE, Price.KEY_ID + "= ?", new String[] { cursor.getString(cursor.getColumnIndex(Price.KEY_ID)) });
            } while (cursor.moveToNext());
        }

        db.delete(Store.TABLE, Store.KEY_ID + "= ?", new String[] { String.valueOf(store_Id) });
        db.close();
        cursor.close();
    }

    public void update(Store store) {

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Store.KEY_NAME, store.getName());
        values.put(Store.KEY_DESCRIPTION, store.getDescription());
        values.put(Store.KEY_IMAGE_URL, store.getImage_url());

        db.update(Store.TABLE, values, Store.KEY_ID + "= ?", new String[] { String.valueOf(store.getStore_Id()) });
        db.close();
    }

    public List<Store> getAll() {
        List<Store> stores = new LinkedList<>();

        String query = "SELECT  * FROM " + Store.TABLE;

        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Store store = null;
        if (cursor.moveToFirst()) {
            do {
                store = new Store();
                store.setStore_Id(cursor.getInt(cursor.getColumnIndex(Store.KEY_ID)));
                store.setName(cursor.getString(cursor.getColumnIndex(Store.KEY_NAME)));
                store.setDescription(cursor.getString(cursor.getColumnIndex(Store.KEY_DESCRIPTION)));
                store.setImage_url(cursor.getString(cursor.getColumnIndex(Store.KEY_IMAGE_URL)));

                stores.add(store);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stores;
    }

    public long getStoresCount() {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        long count  = DatabaseUtils.queryNumEntries(db, Store.TABLE);
        db.close();
        return count;
    }

    public Store getStoreById(int ID){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Store.KEY_ID + "," +
                Store.KEY_NAME+ "," +
                Store.KEY_DESCRIPTION+ "," +
                Store.KEY_IMAGE_URL+
                " FROM " + Store.TABLE
                + " WHERE " + Store.KEY_ID + "=?";

        Store store = new Store();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(ID) } );

        if (cursor.moveToFirst()) {
            do {
                store.setStore_Id(cursor.getInt(cursor.getColumnIndex(Store.KEY_ID)));
                store.setName(cursor.getString(cursor.getColumnIndex(Store.KEY_NAME)));
                store.setDescription(cursor.getString(cursor.getColumnIndex(Store.KEY_DESCRIPTION)));
                store.setImage_url(cursor.getString(cursor.getColumnIndex(Store.KEY_IMAGE_URL)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return store;
    }

    public Store getStoreByName(String store_name){
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Store.KEY_ID + "," +
                Store.KEY_NAME+ "," +
                Store.KEY_DESCRIPTION+ "," +
                Store.KEY_IMAGE_URL+
                " FROM " + Store.TABLE
                + " WHERE " + Store.KEY_NAME + "=?";

        Store store = new Store();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(store_name) } );

        if (cursor.moveToFirst()) {
            do {
                store.setStore_Id(cursor.getInt(cursor.getColumnIndex(Store.KEY_ID)));
                store.setName(cursor.getString(cursor.getColumnIndex(Store.KEY_NAME)));
                store.setDescription(cursor.getString(cursor.getColumnIndex(Store.KEY_DESCRIPTION)));
                store.setImage_url(cursor.getString(cursor.getColumnIndex(Store.KEY_IMAGE_URL)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return store;
    }

    public boolean doesStoreExistByName(String store_name){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Store.TABLE
                +" WHERE " + Store.KEY_NAME + " = \"" + store_name +"\";";
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

    public boolean doesStoreExistByNameOnEdit(String store_name, int store_id){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + Store.TABLE
                +" WHERE " + Store.KEY_NAME + " = \"" + store_name +
                "\" AND " + Store.KEY_ID + " != \"" + store_id +"\";";
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
