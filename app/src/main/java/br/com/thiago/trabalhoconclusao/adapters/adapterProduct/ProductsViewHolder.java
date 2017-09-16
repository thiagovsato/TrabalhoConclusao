package br.com.thiago.trabalhoconclusao.adapters.adapterProduct;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.thiago.trabalhoconclusao.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder{

    public TextView tvProductName;
    public TextView tvProductDescription;
    public ImageView ivLogo;
    public ImageButton btEditProduct;
    public ImageButton btDeleteProduct;

    public ProductsViewHolder(View itemView) {
        super(itemView);
        tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
        tvProductDescription = (TextView) itemView.findViewById(R.id.tvProductDescription);
        ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        btEditProduct = (ImageButton) itemView.findViewById(R.id.btEditProduct);
        btDeleteProduct = (ImageButton) itemView.findViewById(R.id.btDeleteProduct);
    }
}