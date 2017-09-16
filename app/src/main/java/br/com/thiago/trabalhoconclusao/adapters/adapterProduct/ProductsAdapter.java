package br.com.thiago.trabalhoconclusao.adapters.adapterProduct;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;
import br.com.thiago.trabalhoconclusao.model.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    private List<Product> products;
    private OnItemClickListener viewListener, editListener, deleteListener;

    public ProductsAdapter(List<Product> products, OnItemClickListener viewListener,
                           OnItemClickListener editListener, OnItemClickListener deleteListener) {
        this.products = products;
        this.viewListener = viewListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }


    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myLayout = inflater.inflate(R.layout.view_product, parent, false);

        return new ProductsViewHolder(myLayout);
    }

    @Override
    public void onBindViewHolder(final ProductsViewHolder holder, final int position) {

        holder.tvProductName.setText(products.get(position).getName());
        holder.tvProductDescription.setText(products.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onItemClick(products.get(position));
            }
        });

        holder.btEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onItemClick(products.get(position));
            }
        });

        holder.btDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onItemClick(products.get(position));
                checkPriceExistance(holder.itemView.getContext(), products.get(position).getProduct_Id(), position);
            }
        });

        String ProductImage = products.get(position).getImage_url();

        if (TextUtils.isEmpty(ProductImage))
            ProductImage="a";


        Picasso.with(holder.itemView.getContext())
                .load(ProductImage)
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
                .into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void checkPriceExistance (Context context, int _product_Id, int position){
        PriceDAO priceDAO = new PriceDAO(context);
        if (priceDAO.doesPriceExistByProductId(_product_Id))
        {
            alertDialogPriceExists(context, _product_Id, position);
        }
        else {
            alertDialogDelete(context, _product_Id, position);
        }
    }


    private void alertDialogDelete(final Context context, final int _product_Id, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getString(R.string.confirm_delete))
                .setMessage(context.getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProduct(context, _product_Id);
                                products.remove(position);
                                notifyDataSetChanged();
                            }
                        })

                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteProduct(Context context, int _product_Id) {
        ProductDAO dao = new ProductDAO(context);
        dao.delete(_product_Id);
        Toast.makeText(context, R.string.product_deleted, Toast.LENGTH_SHORT).show();
    }

    private void alertDialogPriceExists(final Context context, final int _product_Id, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getString(R.string.confirm_delete_product_price_exists))
                .setMessage(context.getString(R.string.delete_product_price_exists_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProductWithPrices(context, _product_Id);
                                products.remove(position);
                                notifyDataSetChanged();
                            }
                        })

                .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteProductWithPrices(Context context, int _product_Id) {
        ProductDAO dao = new ProductDAO(context);
        dao.deleteWithPrices(_product_Id);
        Toast.makeText(context, R.string.product_deleted, Toast.LENGTH_SHORT).show();
    }


}
