package br.com.thiago.trabalhoconclusao.crud.Store;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Store;

public class StoreInsertActivity extends AppCompatActivity{

    EditText etInsertName, etInsertDescription, etInsertImage;
    TextInputLayout tilInsertName, tilInsertDescription, tilInsertImage;
    private int _store_Id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_insert);

        etInsertName = (EditText) findViewById(R.id.etInsertName);
        etInsertDescription = (EditText) findViewById(R.id.etInsertDescription);
        etInsertImage = (EditText) findViewById(R.id.etInsertImage);
        tilInsertName = (TextInputLayout) findViewById(R.id.tilInsertName);
        tilInsertDescription = (TextInputLayout) findViewById(R.id.tilInsertDescription);
        tilInsertImage = (TextInputLayout) findViewById(R.id.tilInsertImage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabStoreInsertAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etInsertName.getText().toString().isEmpty()){
                    //Snackbar.make(view, getString(R.string.error_store_name_empty), Snackbar.LENGTH_LONG)
                    //        .show();
                    tilInsertName.setError(getString(R.string.error_store_name_empty));
                } else {
                    addNewStore(view);
                }
            }
        });
    }

    public void addNewStore(View view){
        StoreDAO dao = new StoreDAO(this);
        if (dao.doesStoreExistByName(etInsertName.getText().toString())){
            //Snackbar.make(view, getString(R.string.error_store_name_exists), Snackbar.LENGTH_LONG)
            //       .show();
            tilInsertName.setError(getString(R.string.error_store_name_exists));

        } else {
            tilInsertName.setErrorEnabled(false);
            Store store = new Store();
            store.setName(etInsertName.getText().toString());
            store.setDescription(etInsertDescription.getText().toString());
            store.setImage_url(etInsertImage.getText().toString());
            store.setStore_Id(_store_Id);

            if (dao.insert(store)) {
                Snackbar.make(view, getString(R.string.insert_success), Snackbar.LENGTH_LONG)
                        //.setAction("Undo", null)
                        .show();
            } else {
                Snackbar.make(view, getString(R.string.insert_error), Snackbar.LENGTH_LONG)
                        .show();
            }
        }
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
        Intent intent = new Intent(this, StoreViewActivity.class);
        this.startActivity(intent);
        finish();
    }

}