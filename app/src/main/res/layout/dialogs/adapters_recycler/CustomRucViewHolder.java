package com.artico.delivery.pedidos.ui.dialogs.adapters_recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artico.delivery.pedidos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gustavo on 30/08/17.
 */

class CustomRucViewHolder extends RecyclerView.ViewHolder{

    // Inyectamos las vistas al CustomRucViewHolder
    //@BindView(R.id.linearBotonAgregarRuc) LinearLayout linearBotonAgregarRuc;
    @BindView(R.id.linearListCartFactura)
    RelativeLayout container;
    @BindView(R.id.etRazonSocial)
    TextView etRazonSocial;
    @BindView(R.id.etRuc)
    TextView etRuc;
    @BindView(R.id.imgFacturaList)
    ImageView imgFacturaList;
    @BindView(R.id.containerRow)
    RelativeLayout containerRow;

    //@BindView(R.id.btnAgregarFactura) ImageButton btnAgregarFactura;

    CustomRucViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
