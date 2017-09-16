package br.com.thiago.trabalhoconclusao.adapters.adapterStore;

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
import br.com.thiago.trabalhoconclusao.db.StoreDAO;
import br.com.thiago.trabalhoconclusao.model.Store;

public class StoresAdapter extends RecyclerView.Adapter<StoresViewHolder> {

    private List<Store> stores;
    private OnItemClickListener viewListener, editListener, deleteListener;

    public StoresAdapter(List<Store> stores, OnItemClickListener viewListener,
                         OnItemClickListener editListener, OnItemClickListener deleteListener) {
        this.stores = stores;
        this.viewListener = viewListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }


    @Override
    public StoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View myLayout = inflater.inflate(R.layout.view_store, parent, false);

        return new StoresViewHolder(myLayout);
    }

    @Override
    public void onBindViewHolder(final StoresViewHolder holder, final int position) {

        holder.tvStoreName.setText(stores.get(position).getName());
        holder.tvStoreDescription.setText(stores.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onItemClick(stores.get(position));
            }
        });

        holder.btEditStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onItemClick(stores.get(position));
            }
        });

        holder.btDeleteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onItemClick(stores.get(position));
                checkPriceExistance(holder.itemView.getContext(), stores.get(position).getStore_Id(), position);
            }
        });

        String StoreImage = stores.get(position).getImage_url();

        if (TextUtils.isEmpty(StoreImage))
            StoreImage = "a";


        Picasso.with(holder.itemView.getContext())
                .load(StoreImage)
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
                .into(holder.ivLogo);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public void checkPriceExistance (Context context, int _store_Id, int position){
        PriceDAO priceDAO = new PriceDAO(context);
        if (priceDAO.doesPriceExistByStoreId(_store_Id))
        {
            alertDialogPriceExists(context, _store_Id, position);
        }
        else {
            alertDialogDelete(context, _store_Id, position);
        }
    }


    private void alertDialogDelete(final Context context, final int _store_Id, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getString(R.string.confirm_delete))
                .setMessage(context.getString(R.string.delete_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteStore(context, _store_Id);
                                stores.remove(position);
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

    private void deleteStore(Context context, int _store_Id) {
        StoreDAO dao = new StoreDAO(context);
        dao.delete(_store_Id);
        Toast.makeText(context, R.string.store_deleted, Toast.LENGTH_SHORT).show();
    }

    private void alertDialogPriceExists(final Context context, final int _store_Id, final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getString(R.string.confirm_delete_store_price_exists))
                .setMessage(context.getString(R.string.delete_store_price_exists_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteStoreWithPrices(context, _store_Id);
                                stores.remove(position);
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

    private void deleteStoreWithPrices(Context context, int _store_Id) {
        StoreDAO dao = new StoreDAO(context);
        dao.deleteWithPrices(_store_Id);
        Toast.makeText(context, R.string.store_deleted, Toast.LENGTH_SHORT).show();
    }
}