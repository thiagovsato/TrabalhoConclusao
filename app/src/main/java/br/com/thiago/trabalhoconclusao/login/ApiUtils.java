package br.com.thiago.trabalhoconclusao.login;

public class ApiUtils {

    public static final String BASE_URL = "http://www.mocky.io";

    public static LoginAPI getLoginAPI() {
        return RetrofitClient.getClient(BASE_URL).create(LoginAPI.class);
    }
}