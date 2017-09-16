package br.com.thiago.trabalhoconclusao.crud.Price;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.adapters.adapterPrice.OnItemClickListener;
import br.com.thiago.trabalhoconclusao.adapters.adapterPrice.PricesByStoreAdapter;
import br.com.thiago.trabalhoconclusao.crud.Store.StoreEditActivity;
import br.com.thiago.trabalhoconclusao.crud.Store.StoreViewActivity;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Store;

public class PriceByStoreViewActivity extends AppCompatActivity{

    private RecyclerView rvPrices;
    private PricesByStoreAdapter mAdapter;
    private int _store_Id;
    private int origin = 2;
    ImageView ivStoreLogo;
    TextView tvStoreName, tvStoreDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricebystore_view);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPriceByStoreViewAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addPrice(); }});

        rvPrices = (RecyclerView) findViewById(R.id.rvPrices);
        ivStoreLogo = (ImageView) findViewById(R.id.ivStoreLogo);
        tvStoreName = (TextView) findViewById(R.id.tvStoreName);
        tvStoreDescription = (TextView) findViewById(R.id.tvStoreDescription);

        PriceDAO priceDAO = new PriceDAO(this);

        Intent intent = getIntent();
        _store_Id =intent.getIntExtra("store_Id", 0);

        Store store = new Store();
        StoreDAO storeDAO = new StoreDAO(this);
        store = storeDAO.getStoreById(_store_Id);

        String StoreImage = store.getImage_url();

        if (TextUtils.isEmpty(StoreImage))
            StoreImage="a";

        Picasso.with(this)
                .load(StoreImage)
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
                .into(ivStoreLogo);

        tvStoreName.setText(store.getName());
        tvStoreDescription.setText(store.getDescription());

        mAdapter = new PricesByStoreAdapter(priceDAO.getPricesByStoreId(_store_Id),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Price item) {
                        Intent intent = new Intent(getApplicationContext(), PriceEditActivity.class);
                        intent.putExtra("price_Id", item.getPrice_Id());
                        intent.putExtra("store_Id", _store_Id);
                        intent.putExtra("origin", 2);
                        startActivity(intent);
                        finish();
                    }},
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Price item) {

                        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                    }}
        );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPrices.setLayoutManager(layoutManager);
        rvPrices.setAdapter(mAdapter);
        rvPrices.setHasFixedSize(true);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPrices.getContext(),
//                LinearLayoutManager.VERTICAL);
//        rvPrices.addItemDecoration(dividerItemDecoration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    public void addPrice(){
        Intent intent = new Intent(this, PriceInsertActivity.class);
        intent.putExtra("store_Id", _store_Id);
        this.startActivity(intent);
        finish();
    }

    public void alertDialogDelete(final int _price_Id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletePrice(_price_Id);
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

    private void deletePrice(int _price_Id) {
        PriceDAO dao = new PriceDAO(this);
        dao.delete(_price_Id);
        Toast.makeText(this, R.string.price_deleted, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed(){ close(); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            close();
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(getApplicationContext(), StoreEditActivity.class);
            intent.putExtra("store_Id", _store_Id);
            intent.putExtra("origin", origin);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void close(){
        Intent intent = new Intent(this, StoreViewActivity.class);
        this.startActivity(intent);
        finish();
    }
}