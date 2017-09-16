package br.com.thiago.trabalhoconclusao.crud.Store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.crud.Price.PriceByStoreViewActivity;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Store;

public class StoreEditActivity extends AppCompatActivity{

    EditText etEditName, etEditDescription, etEditImage;
    TextInputLayout tilEditName, tilEditDescription, tilEditImage;
    private int _store_Id;
    private int origin; // 1- StoreView, 2- PriceByStoreView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit);

        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditDescription = (EditText) findViewById(R.id.etEditDescription);
        etEditImage = (EditText) findViewById(R.id.etEditImage);
        tilEditName = (TextInputLayout) findViewById(R.id.tilEditName);
        tilEditDescription = (TextInputLayout) findViewById(R.id.tilEditDescription);
        tilEditImage = (TextInputLayout) findViewById(R.id.tilEditImage);

        Intent intent = getIntent();
        _store_Id =intent.getIntExtra("store_Id", 0);
        origin = intent.getIntExtra("origin", 0);

        StoreDAO dao = new StoreDAO(this);
        Store store = new Store();
        store = dao.getStoreById(_store_Id);

        etEditName.setText(store.getName());
        etEditDescription.setText(store.getDescription());
        etEditImage.setText(store.getImage_url());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabStoreEditConfirm);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEditName.getText().toString().isEmpty()){
//                    Snackbar.make(view, getString(R.string.error_store_name_empty), Snackbar.LENGTH_LONG)
//                            .show();
                    tilEditName.setError(getString(R.string.error_store_name_empty));
                } else {
                    saveStore(view);
                }
            }
        });

        FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabStoreEditDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPriceExistance(view);
            }
        });
    }

    public void saveStore(View view){
        StoreDAO dao = new StoreDAO(this);
        if (dao.doesStoreExistByNameOnEdit(etEditName.getText().toString(), _store_Id)){
//            Snackbar.make(view, getString(R.string.error_store_name_exists), Snackbar.LENGTH_LONG)
//                    .show();
            tilEditName.setError(getString(R.string.error_store_name_exists));
        } else {
            tilEditName.setErrorEnabled(false);
            Store store = new Store();
            store.setName(etEditName.getText().toString());
            store.setDescription(etEditDescription.getText().toString());
            store.setImage_url(etEditImage.getText().toString());
            store.setStore_Id(_store_Id);
            dao.update(store);

//            Snackbar.make(view, getString(R.string.edit_saved), Snackbar.LENGTH_LONG)
//                    //.setAction("Undo", null)
//                    .show();
            Toast.makeText(this, getString(R.string.edit_saved), Toast.LENGTH_LONG).show();
            close();
        }
    }

    public void checkPriceExistance (final View view){
        PriceDAO priceDAO = new PriceDAO(this);
        if (priceDAO.doesPriceExistByStoreId(_store_Id))
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
                .setTitle(getString(R.string.confirm_delete_store_price_exists))
                .setMessage(getString(R.string.delete_store_price_exists_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteStoreWithPrices(view);
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
                                deleteStore(view);
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

    public void deleteStore(View view){
        StoreDAO dao = new StoreDAO(this);
        dao.delete(_store_Id);

        Snackbar.make(view, getString(R.string.store_deleted), Snackbar.LENGTH_LONG)
                //.setAction("Undo", null)
                .show();

        Intent intent = new Intent(this, StoreViewActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void deleteStoreWithPrices(View view) {
        StoreDAO dao = new StoreDAO(this);
        dao.deleteWithPrices(_store_Id);
        Snackbar.make(view, getString(R.string.store_deleted), Snackbar.LENGTH_LONG)
                //.setAction("Undo", null)
                .show();

        Intent intent = new Intent(this, StoreViewActivity.class);
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
            Intent intent = new Intent(this, StoreViewActivity.class);
            this.startActivity(intent);
            finish();
        }
        if (origin == 2) {
            Intent intent = new Intent(this, PriceByStoreViewActivity.class);
            intent.putExtra("store_Id", _store_Id);
            this.startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}