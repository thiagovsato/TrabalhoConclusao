package br.com.thiago.trabalhoconclusao.crud.Price;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Product;
import br.com.thiago.trabalhoconclusao.model.Store;

public class PriceInsertActivity extends AppCompatActivity{

    EditText etPrice, etProduct, etStore;
    TextInputLayout tilInsertPriceProduct, tilInsertPriceStore, tilInsertPricePrice;
    private int _price_Id=0;
    private int _product_Id=0;
    private int _store_Id=0;
    int valuePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_insert);

        etPrice = (EditText)findViewById(R.id.etPrice);
        etProduct = (EditText)findViewById(R.id.etProduct);
        etStore = (EditText)findViewById(R.id.etStore);

        tilInsertPriceProduct = (TextInputLayout) findViewById(R.id.tilInsertPriceProduct);
        tilInsertPriceStore = (TextInputLayout) findViewById(R.id.tilInsertPriceStore);
        tilInsertPricePrice = (TextInputLayout) findViewById(R.id.tilInsertPricePrice);

        Intent intent = getIntent();
        _product_Id = intent.getIntExtra("product_Id", 0);
        _store_Id = intent.getIntExtra("store_Id", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPriceInsertAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etProduct.getText().toString().isEmpty() && !etStore.getText().toString().isEmpty()){
//                    Snackbar.make(view, getString(R.string.error_product_name_empty), Snackbar.LENGTH_LONG)
//                            .show();
                    tilInsertPriceProduct.setError(getString(R.string.error_product_name_empty));
                    tilInsertPriceStore.setErrorEnabled(false);
                } else if (!etProduct.getText().toString().isEmpty() && etStore.getText().toString().isEmpty()) {
//                    Snackbar.make(view, getString(R.string.error_store_name_empty), Snackbar.LENGTH_LONG)
//                            .show();
                    tilInsertPriceProduct.setErrorEnabled(false);
                    tilInsertPriceStore.setError(getString(R.string.error_store_name_empty));
                } else if (etProduct.getText().toString().isEmpty() && etStore.getText().toString().isEmpty()) {
//                    Snackbar.make(view, getString(R.string.error_store_name_empty), Snackbar.LENGTH_LONG)
//                            .show();
                    tilInsertPriceProduct.setError(getString(R.string.error_product_name_empty));
                    tilInsertPriceStore.setError(getString(R.string.error_store_name_empty));
                } else {
                    tilInsertPriceProduct.setErrorEnabled(false);
                    tilInsertPriceStore.setErrorEnabled(false);
                    addNewPrice(view);
                }
            }
        });

        if(_product_Id!=0)
        {
            ProductDAO productDAO = new ProductDAO(this);
            Product product = productDAO.getProductByID(_product_Id);
            etProduct.setText(product.getName());
        }
        if(_store_Id!=0)
        {
            StoreDAO storeDAO = new StoreDAO(this);
            Store store = storeDAO.getStoreById(_store_Id);
            etStore.setText(store.getName());
        }

    }

    public void addNewPrice(View view){
        if (etPrice.getText().toString().isEmpty()
                || etPrice.getText().toString().equals(".")
                || etPrice.getText().toString().equals(",")
                || etPrice.getText().toString().equals("-")) {
            valuePrice = 0;
        } else {
            String stprice = etPrice.getText().toString().replace(",", ".");
            stprice.replace("-","");
            double checkvalue = Double.parseDouble(stprice);
            if (checkvalue > 21474836) {
                Snackbar.make(view, getString(R.string.error_price_number_too_high), Snackbar.LENGTH_LONG)
                        .show();
                return;
            } else {
                BigDecimal bigprice = new BigDecimal(stprice);
                bigprice = bigprice.multiply(BigDecimal.valueOf(100));
                valuePrice = bigprice.toBigInteger().intValue();
            }
        }

        PriceDAO priceDAO = new PriceDAO(this);
        Price price = new Price();
        ProductDAO productDAO = new ProductDAO(this);
        StoreDAO storeDAO = new StoreDAO(this);

        if (!productDAO.doesProductExistByName(etProduct.getText().toString())) {
            Product product = new Product();
            product.setName(etProduct.getText().toString());
            product.setDescription("");
            product.setImage_url("");
            product.setProduct_Id(0);

            productDAO.insert(product);
        }

        price.setProduct_id(
                productDAO.getProductByName(etProduct.getText().toString()).getProduct_Id());

        if (!storeDAO.doesStoreExistByName(etStore.getText().toString())) {
            Store store = new Store();
            store.setName(etStore.getText().toString());
            store.setDescription("");
            store.setImage_url("");
            store.setStore_Id(0);

            storeDAO.insert(store);
        }

        price.setStore_id(
                storeDAO.getStoreByName(etStore.getText().toString()).getStore_Id());

        price.setPrice(valuePrice);
        price.setPrice_Id(_price_Id);

        if (priceDAO.insert(price)) {
            Snackbar.make(view, getString(R.string.insert_success), Snackbar.LENGTH_LONG)
                    //.setAction("Undo", null)
                    .show();
        } else {
            Snackbar.make(view, getString(R.string.insert_error), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            back();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (_store_Id == 0 && _product_Id != 0) {
            Intent intent = new Intent(this, PriceByProductViewActivity.class);
            intent.putExtra("product_Id", _product_Id);
            this.startActivity(intent);
            finish();
        }
        if (_product_Id == 0 && _store_Id != 0) {
            Intent intent = new Intent(this, PriceByStoreViewActivity.class);
            intent.putExtra("store_Id", _store_Id);
            this.startActivity(intent);
            finish();
        } else {
            PriceInsertActivity.this.finish();
        }
    }
}
