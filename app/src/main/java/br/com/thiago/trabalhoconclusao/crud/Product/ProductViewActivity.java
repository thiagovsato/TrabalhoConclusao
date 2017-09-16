package br.com.thiago.trabalhoconclusao.crud.Product;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.adapters.adapterProduct.OnItemClickListener;
import br.com.thiago.trabalhoconclusao.adapters.adapterProduct.ProductsAdapter;
import br.com.thiago.trabalhoconclusao.crud.Price.PriceByProductViewActivity;
import br.com.thiago.trabalhoconclusao.model.Product;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;

public class ProductViewActivity extends AppCompatActivity{

    private RecyclerView rvProducts;
    private ProductsAdapter mAdapter;
    private int origin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_view);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabProdViewAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addProduct(); }});


        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);

        ProductDAO dao = new ProductDAO(this);

        mAdapter = new ProductsAdapter(dao.getAll(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        viewPrices(item.getProduct_Id());
                    }},
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        editProduct(item.getProduct_Id());
                    }},
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Product item) {
                        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                    }}
        );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setAdapter(mAdapter);
        rvProducts.setHasFixedSize(true);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvProducts.getContext(),
//                LinearLayoutManager.VERTICAL);
//        rvProducts.addItemDecoration(dividerItemDecoration);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    public void addProduct(){
        Intent intent = new Intent(this, ProductInsertActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void viewPrices (int _product_Id){
        Intent intent = new Intent(getApplicationContext(), PriceByProductViewActivity.class);
        intent.putExtra("product_Id", _product_Id);
        startActivity(intent);
        finish();
    }

    public void editProduct (int _product_Id) {
        Intent intent = new Intent(getApplicationContext(), ProductEditActivity.class);
        intent.putExtra("product_Id", _product_Id);
        intent.putExtra("origin", origin);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            ProductViewActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}