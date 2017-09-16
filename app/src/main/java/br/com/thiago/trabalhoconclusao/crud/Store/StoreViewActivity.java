package br.com.thiago.trabalhoconclusao.crud.Store;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.adapters.adapterStore.OnItemClickListener;
import br.com.thiago.trabalhoconclusao.adapters.adapterStore.StoresAdapter;
import br.com.thiago.trabalhoconclusao.crud.Price.PriceByStoreViewActivity;
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Store;

public class StoreViewActivity extends AppCompatActivity{

    private RecyclerView rvStores;
    private StoresAdapter mAdapter;
    private int origin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_view);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabStoreViewAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addStore(); }});

        rvStores = (RecyclerView) findViewById(R.id.rvStores);

        StoreDAO dao = new StoreDAO(this);

        mAdapter = new StoresAdapter(dao.getAll(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Store item) {
                        viewPrices(item.getStore_Id());
                    }},
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Store item) {
                        editProduct(item.getStore_Id());
                    }},
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Store item) {
                        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                    }}
        );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvStores.setLayoutManager(layoutManager);
        rvStores.setAdapter(mAdapter);
        rvStores.setHasFixedSize(true);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvStores.getContext(),
//                LinearLayoutManager.VERTICAL);
//        rvStores.addItemDecoration(dividerItemDecoration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void addStore(){
        Intent intent = new Intent(this, StoreInsertActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void viewPrices (int _store_Id){
        Intent intent = new Intent(getApplicationContext(), PriceByStoreViewActivity.class);
        intent.putExtra("store_Id", _store_Id);
        startActivity(intent);
        finish();
    }

    public void editProduct (int _store_Id) {
        Intent intent = new Intent(getApplicationContext(), StoreEditActivity.class);
        intent.putExtra("store_Id", _store_Id);
        intent.putExtra("origin", origin);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            StoreViewActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}