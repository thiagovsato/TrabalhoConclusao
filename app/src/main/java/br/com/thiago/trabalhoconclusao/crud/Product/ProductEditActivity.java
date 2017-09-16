package br.com.thiago.trabalhoconclusao.crud.Product;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.crud.Price.PriceByProductViewActivity;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.model.Product;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;

public class ProductEditActivity extends AppCompatActivity{

    EditText etEditName, etEditDescription, etEditImage;
    TextInputLayout tilEditName, tilEditDescription, tilEditImage;
    private int _product_Id;
    private int origin; // 1= ProductView, 2= PriceByProductView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);


        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditDescription = (EditText) findViewById(R.id.etEditDescription);
        etEditImage = (EditText) findViewById(R.id.etEditImage);
        tilEditName = (TextInputLayout) findViewById(R.id.tilEditName);
        tilEditDescription = (TextInputLayout) findViewById(R.id.tilEditDescription);
        tilEditImage = (TextInputLayout) findViewById(R.id.tilEditImage);

        Intent intent = getIntent();
        _product_Id =intent.getIntExtra("product_Id", 0);
        origin = intent.getIntExtra("origin", 0);

        ProductDAO dao = new ProductDAO(this);
        Product product = new Product();
        product = dao.getProductByID(_product_Id);

        etEditName.setText(product.getName());
        etEditDescription.setText(product.getDescription());
        etEditImage.setText(product.getImage_url());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabProdEditConfirm);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEditName.getText().toString().isEmpty()){
//                    Snackbar.make(view, getString(R.string.error_product_name_empty), Snackbar.LENGTH_LONG)
//                            .show();
                    tilEditName.setError(getString(R.string.error_product_name_empty));
                } else {
                    saveProduct(view);
                }
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabProdEditDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPriceExistance(view);
            }
        });
    }


    public void saveProduct(View view) {
        ProductDAO dao = new ProductDAO(this);
        if (dao.doesProductExistByNameOnEdit(etEditName.getText().toString(), _product_Id)){
//            Snackbar.make(view, getString(R.string.error_product_name_exists), Snackbar.LENGTH_LONG)
//                    .show();
            tilEditName.setError(getString(R.string.error_product_name_exists));
        } else {
            tilEditName.setErrorEnabled(false);
            Product product = new Product();
            product.setName(etEditName.getText().toString());
            product.setDescription(etEditDescription.getText().toString());
            product.setImage_url(etEditImage.getText().toString());
            product.setProduct_Id(_product_Id);
            dao.update(product);

            Toast.makeText(this, getString(R.string.edit_saved), Toast.LENGTH_LONG).show();
            close();
        }
    }

    public void checkPriceExistance (final View view){
        PriceDAO priceDAO = new PriceDAO(this);
        if (priceDAO.doesPriceExistByProductId(_product_Id))
        {
            alertDialogPriceExist(view);
        }
        else {
            alertDialogDelete(view);
        }
    }

    public void alertDialogPriceExist(final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.confirm_delete_product_price_exists))
                .setMessage(getString(R.string.delete_product_price_exists_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProductWithPrices(view);
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

    public void alertDialogDelete(final View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProduct(view);
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

    public void deleteProduct(View view) {
        ProductDAO dao = new ProductDAO(this);
        dao.delete(_product_Id);

        Toast.makeText(this, getString(R.string.product_deleted), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ProductViewActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void deleteProductWithPrices(View view) {
        ProductDAO dao = new ProductDAO(this);
        dao.deleteWithPrices(_product_Id);

        Toast.makeText(this, getString(R.string.product_deleted), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ProductViewActivity.class);
        this.startActivity(intent);
        finish();
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
            Intent intent = new Intent(this, ProductViewActivity.class);
            this.startActivity(intent);
            finish();
        }
        if (origin == 2) {
            Intent intent = new Intent(this, PriceByProductViewActivity.class);
            intent.putExtra("product_Id", _product_Id);
            this.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}
