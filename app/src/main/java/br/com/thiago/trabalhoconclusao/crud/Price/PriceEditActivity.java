package br.com.thiago.trabalhoconclusao.crud.Price;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Price;

public class PriceEditActivity extends AppCompatActivity{

    TextView tvPriceEditProduct, tvPriceEditStore;
    EditText etPriceEditPrice;
    String productName, storeName;
    private int _price_Id;
    private int _product_Id;
    private int _store_Id;
    private int origin; // 1= ProductView, 2= StoreView
    int valuePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_edit);

        tvPriceEditProduct = (TextView) findViewById(R.id.tvPriceEditProduct);
        tvPriceEditStore = (TextView) findViewById(R.id.tvPriceEditStore);
        etPriceEditPrice = (EditText) findViewById(R.id.etPriceEditPrice);

        Intent intent = getIntent();
        _price_Id = intent.getIntExtra("price_Id", 0);
        _product_Id = intent.getIntExtra("product_Id", 0);
        _store_Id = intent.getIntExtra("store_Id", 0);
        origin = intent.getIntExtra("origin", 0);

        PriceDAO dao = new PriceDAO(this);
        Price price = new Price();
        price = dao.getPriceByID(_price_Id);
        DecimalFormat precision = new DecimalFormat("0.00");

        etPriceEditPrice.setText(String.valueOf(precision.format(
                (double)(price.getPrice())/100)));

        ProductDAO productDAO = new ProductDAO(this);
        productName = productDAO.getProductByID(price.getProduct_id()).getName();

        StoreDAO storeDAO = new StoreDAO(this);
        storeName = storeDAO.getStoreById(price.getStore_id()).getName();

        tvPriceEditProduct.setText(productName);
        tvPriceEditStore.setText(storeName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabPriceEditConfirm);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrice(view);
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabPriceEditDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(view);
            }
        });
    }

    public void savePrice(View view) {
        String stprice = etPriceEditPrice.getText().toString().replace(",",".");
        double checkvalue = Double.parseDouble(stprice);
        if (checkvalue > 21474836)
        {
            Toast.makeText(this, getString(R.string.error_price_number_too_high), Toast.LENGTH_LONG).show();
        } else {
            PriceDAO dao = new PriceDAO(this);
            Price oldPrice = dao.getPriceByID(_price_Id);
            Price price = new Price();


            if (etPriceEditPrice.getText().toString().isEmpty() ||
                    etPriceEditPrice.getText().toString().equals(".") ||
                    etPriceEditPrice.getText().toString().equals(","))
            {
                valuePrice = 0;
            } else {
                BigDecimal bigprice = new BigDecimal(stprice);
                bigprice=bigprice.multiply(BigDecimal.valueOf(100));
                valuePrice = bigprice.toBigInteger().intValue();
            }

            price.setPrice_Id(_price_Id);
            price.setPrice(valuePrice);
            price.setProduct_id(oldPrice.getProduct_id());
            price.setStore_id(oldPrice.getStore_id());
            dao.update(price);

            Toast.makeText(this, getString(R.string.edit_saved), Toast.LENGTH_LONG).show();
            close();
        }
    }

    public void alertDialog(final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletePrice(view);
                            }
                        })

                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deletePrice (View view) {
        PriceDAO dao = new PriceDAO(this);
        dao.delete(_price_Id);

        Toast.makeText(this, getString(R.string.price_deleted), Toast.LENGTH_LONG).show();
        close();
    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            close();
        }

        return super.onOptionsItemSelected(item);
    }

    private void close(){
        if (origin == 1) {
            Intent intent = new Intent(this, PriceByProductViewActivity.class);
            intent.putExtra("product_Id", _product_Id);
            this.startActivity(intent);
            finish();
        } else if (origin == 2) {
            Intent intent = new Intent(this, PriceByStoreViewActivity.class);
            intent.putExtra("store_Id", _store_Id);
            this.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}
