package br.com.thiago.trabalhoconclusao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.thiago.trabalhoconclusao.model.*;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "pricelog.db";

    public SQLiteHelper(Context context ) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + User.TABLE  + "("
            + User.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + User.KEY_USERNAME + " TEXT, "
            + User.KEY_PASSWORD + " TEXT )";

    private static final String CREATE_TABLE_PRODUCT = "CREATE TABLE " + Product.TABLE  + "("
                + Product.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Product.KEY_NAME + " TEXT, "
                + Product.KEY_DESCRIPTION + " TEXT, "
                + Product.KEY_IMAGE_URL + " TEXT )";

    private static final String CREATE_TABLE_STORE = "CREATE TABLE " + Store.TABLE  + "("
            + Store.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Store.KEY_NAME + " TEXT, "
            + Store.KEY_DESCRIPTION + " TEXT, "
            + Store.KEY_IMAGE_URL + " TEXT )";

    private static final String CREATE_TABLE_PRICE = "CREATE TABLE " + Price.TABLE  + "("
            + Price.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + Price.KEY_PRODUCT_ID + " INTEGER, "
            + Price.KEY_STORE_ID + " INTEGER, "
            + Price.KEY_PRICE + " INTEGER )";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_STORE);
        db.execSQL(CREATE_TABLE_PRICE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Store.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Price.TABLE);

        onCreate(db);
    }

}
