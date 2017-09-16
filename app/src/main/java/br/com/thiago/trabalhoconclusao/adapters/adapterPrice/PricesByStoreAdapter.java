package br.com.thiago.trabalhoconclusao.adapters.adapterPrice;

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

import java.text.DecimalFormat;
import java.util.List;

import br.com.thiago.trabalhoconclusao.R;
import br.com.thiago.trabalhoconclusao.db.PriceDAO;
import br.com.thiago.trabalhoconclusao.db.ProductDAO;
import br.com.thiago.trabalhoconclusao.model.Price;
import br.com.thiago.trabalhoconclusao.model.Product;

public class PricesByStoreAdapter extends RecyclerView.Adapter<PricesViewHolder> {

    private List<Price> prices;
    private OnItemClickListener viewListener, deleteListener;

    public PricesByStoreAdapter(List<Price> prices, OnItemClickListener viewListener, OnItemClickListener deleteListener) {
        this.prices = prices;
        this.viewListener = viewListener;
        this.deleteListener = deleteListener;
    }


    @Override
    public PricesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myLayout = inflater.inflate(R.layout.view_price, parent, false);

        return new PricesViewHolder(myLayout);
    }

    @Override
    public void onBindViewHolder(final PricesViewHolder holder, final int position) {

        ProductDAO dao = new ProductDAO(holder.itemView.getContext());
        Product product = dao.getProductByID(prices.get(position).getProduct_id());
        holder.tvName.setText(product.getName());
        DecimalFormat precision = new DecimalFormat("0.00");

        String price = holder.itemView.getContext().getString(R.string.money)+String.valueOf(precision.format(
                (double)(prices.get(position).getPrice())/100));

        holder.tvPrice.setText(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onItemClick(prices.get(position));
            }
        });

        holder.btDeletePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onItemClick(prices.get(position));
                alertDialogDelete(holder.itemView.getContext(), prices.get(position).getPrice_Id(), position);
            }
        });

        String PriceImage = product.getImage_url();

        if (TextUtils.isEmpty(PriceImage))
            PriceImage="a";


        Picasso.with(holder.itemView.getContext())
                .load(PriceImage)
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
                .into(holder.ivLogo);
    }

    private void alertDialogDelete(final Context context, final int _price_Id, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getString(R.string.confirm_delete))
                .setMessage(context.getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletePrice(context, _price_Id);
                                prices.remove(position);
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

    private void deletePrice(Context context, int _price_Id) {
        PriceDAO dao = new PriceDAO(context);
        dao.delete(_price_Id);
        Toast.makeText(context, R.string.price_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return prices.size();
    }


}
