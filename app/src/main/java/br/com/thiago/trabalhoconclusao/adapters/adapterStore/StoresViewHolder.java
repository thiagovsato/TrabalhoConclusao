package br.com.thiago.trabalhoconclusao.adapters.adapterStore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.thiago.trabalhoconclusao.R;

public class StoresViewHolder extends RecyclerView.ViewHolder{

    public TextView tvStoreName;
    public TextView tvStoreDescription;
    public ImageView ivLogo;
    public ImageButton btEditStore;
    public ImageButton btDeleteStore;

    public StoresViewHolder(View itemView) {
        super(itemView);
        tvStoreName = (TextView) itemView.findViewById(R.id.tvStoreName);
        tvStoreDescription = (TextView) itemView.findViewById(R.id.tvStoreDescription);
        ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        btEditStore = (ImageButton) itemView.findViewById(R.id.btEditStore);
        btDeleteStore = (ImageButton) itemView.findViewById(R.id.btDeleteStore);
    }
}