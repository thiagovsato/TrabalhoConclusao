package br.com.thiago.trabalhoconclusao.adapters.adapterPrice;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.thiago.trabalhoconclusao.R;

public class PricesViewHolder extends RecyclerView.ViewHolder{

    public TextView tvName;
    public TextView tvPrice;
    public ImageView ivLogo;
    public ImageButton btDeletePrice;

    public PricesViewHolder(View itemView) {
        super(itemView);
        ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        btDeletePrice = (ImageButton) itemView.findViewById(R.id.btDeletePrice);
    }
}