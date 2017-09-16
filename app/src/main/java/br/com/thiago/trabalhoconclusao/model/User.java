package br.com.thiago.trabalhoconclusao.model;

import com.google.gson.annotations.SerializedName;

public class User {

    // Labels
    public static final String TABLE = "user";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    public int user_id;

    @SerializedName("usuario")
    public String username;

    @SerializedName("senha")
    public String password;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
