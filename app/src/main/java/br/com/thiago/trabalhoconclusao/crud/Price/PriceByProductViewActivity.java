package br.com.thiago.trabalhoconclusao.crud.Price;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.squareup.picasso.Picasso;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.adapters.adapterPrice.OnItemClickListener;
import br.com.thiago.trabalhoconclusao.adapters.adapterPrice.PricesByProductAdapter;
import br.com.thiago.trabalhoconclusao.crud.Product.ProductEditActivity;
import br.com.thiago.trabalhoconclusao.crud.Product.ProductViewActivity;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;
import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Product;

public class PriceByProductViewActivity extends AppCompatActivity{

    private RecyclerView rvPrices;
    private PricesByProductAdapter mAdapter;
    private int _product_Id;
    private int origin = 2;
    ImageView ivProductLogo;
    TextView tvProductName, tvProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricebyproduct_view);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPriceByProductViewAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addPrice(); }});


        rvPrices = (RecyclerView) findViewById(R.id.rvPrices);
        ivProductLogo = (ImageView) findViewById(R.id.ivProductLogo);
        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvProductDescription = (TextView) findViewById(R.id.tvProductDescription);

        PriceDAO priceDAO = new PriceDAO(this);

        Intent intent = getIntent();
        _product_Id =intent.getIntExtra("product_Id", 0);

        Product product = new Product();
        ProductDAO productDAO = new ProductDAO(this);
        product = productDAO.getProductByID(_product_Id);

        String ProductImage = product.getImage_url();

        if (TextUtils.isEmpty(ProductImage))
            ProductImage="a";

        Picasso.with(this)
                .load(ProductImage)
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
                .into(ivProductLogo);

        tvProductName.setText(product.getName());
        tvProductDescription.setText(product.getDescription());

        mAdapter = new PricesByProductAdapter(priceDAO.getPricesByProductId(_product_Id),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Price item) {
                        Intent intent = new Intent(getApplicationContext(), PriceEditActivity.class);
                        intent.putExtra("price_Id", item.getPrice_Id());
                        intent.putExtra("product_Id", _product_Id);
                        intent.putExtra("origin", 1);
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
        intent.putExtra("product_Id", _product_Id);
        this.startActivity(intent);
        finish();
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
            Intent intent = new Intent(getApplicationContext(), ProductEditActivity.class);
            intent.putExtra("product_Id", _product_Id);
            intent.putExtra("origin", origin);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void close(){
        Intent intent = new Intent(this, ProductViewActivity.class);
        this.startActivity(intent);
        finish();
    }
}