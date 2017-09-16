package br.com.thiago.trabalhoconclusao.model;

public class Price {

    // Labels
    public static final String TABLE = "Price";
    public static final String KEY_ID = "id";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_STORE_ID = "store_id";
    public static final String KEY_PRICE = "price";

    private int price_Id;
    private int product_id;
    private int store_id;
    private int price;

    public int getPrice_Id() {
        return price_Id;
    }

    public void setPrice_Id(int price_Id) {
        this.price_Id = price_Id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
