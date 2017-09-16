package br.com.thiago.trabalhoconclusao.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.thiago.trabalhoconclusao.model.User;

public class UserDAO {

    private SQLiteHelper sqliteHelper;

    public UserDAO(Context context) {
        sqliteHelper = new SQLiteHelper(context);
    }

    public boolean insert(User user) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_USERNAME, user.username);
        values.put(User.KEY_PASSWORD, user.password);

        long user_Id = db.insert(User.TABLE, null, values);
        db.close();

        if(user_Id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean doesItExist(User user){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        String query = "SELECT  * FROM " + User.TABLE
                +" WHERE " + User.KEY_USERNAME + " = \"" + user.getUsername() +"\";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        else{
            cursor.close();
            return true;
        }
    }

    public boolean isValidLogin(String username, String password) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selectQuery = "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_USERNAME + "," +
                User.KEY_PASSWORD +
                " FROM " + User.TABLE +
                " WHERE " + User.KEY_USERNAME + "=?" +
                " AND " + User.KEY_PASSWORD + "=?";

        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{username, password});

/*        if (cursor.moveToFirst()) {
            do {
                user.setUser_id(cursor.getInt(cursor.getColumnIndex(User.KEY_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(User.KEY_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.KEY_PASSWORD)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (user.getPassword().equals(password)){
            return true;
        }
        else{
            return false;
        }
    */
        if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            return false;
        } else {
            cursor.close();
            db.close();
            return true;
        }
    }
}
