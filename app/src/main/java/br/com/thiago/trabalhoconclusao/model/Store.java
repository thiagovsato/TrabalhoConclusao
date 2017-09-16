package br.com.thiago.trabalhoconclusao.model;

public class Store {

    // Labels
    public static final String TABLE = "Store";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE_URL = "image_url";

    private int store_Id;
    private String name;
    private String description;
    private String image_url;

    public int getStore_Id() {
        return store_Id;
    }

    public void setStore_Id(int store_Id) {
        this.store_Id = store_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
