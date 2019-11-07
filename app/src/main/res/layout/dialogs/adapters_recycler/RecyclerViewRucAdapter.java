package com.artico.delivery.pedidos.ui.dialogs.adapters_recycler;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Ruc;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Hector on 20/11/17.
 */

public class RecyclerViewRucAdapter extends RecyclerView.Adapter<CustomRucViewHolder>{

    // Variables
    Context context;
    List<Ruc> lista;
    private final Realm realm;

    public RecyclerViewRucAdapter(Context context, List<Ruc> listaRuc, Realm realm) {
        this.context = context;
        this.lista = listaRuc;
        this.realm = realm;
    }

    @Override
    public CustomRucViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.item_cart_list_factura, null);
        CustomRucViewHolder cvh = new CustomRucViewHolder(layoutView);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final CustomRucViewHolder holder, int i) {
        final int position = holder.getAdapterPosition();
        final Ruc objeto = lista.get(position);
        final Ruc oldRuc = realm.where(Ruc.class).equalTo("is_default", true).findFirst();

        // Seteamos los textos e imagenes
        holder.etRazonSocial.setText(objeto.getRazonSocial());
        holder.etRuc.setText("RUC: " + objeto.getRuc());

        if (objeto.isIs_default()){
            holder.container.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_button_white_rounded));
            holder.etRuc.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.etRazonSocial.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.imgFacturaList.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.invoice_blue));
        }
        //holder.linea3.setText(objeto.getStreet2());
        //holder.iconAddress.setBackground(UtilsAddress.getAddressMainIcon(context, 5, true));s
        // OnClick
        holder.containerRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Ruc selectedRuc = realm.where(Ruc.class).equalTo("id", objeto.getId()).findFirst();
                // Seteamos el Ruc por default
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final RealmResults<Ruc> rucs = realm.where(Ruc.class).findAll();
                        for(Ruc a : rucs) {
                            a.setIs_default(false);
                        }
                        selectedRuc.setIs_default(true);
                    }
                });


                // Animamos y notificamos la nueva lista
                lista.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, lista.size());

               lista.add(realm.copyFromRealm(selectedRuc));
               notifyItemInserted(lista.size());
               notifyItemRangeChanged(0, lista.size());
               notifyDataSetChanged();
               GlobalBus.getBus().postSticky(new Events.FacturaSeleccionadaEvent(selectedRuc));
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    @UiThread
    protected void dataSetChanged() {
        notifyDataSetChanged();
    }

    // Notificar los cambios
    public void notify(List<Ruc> lista) {
        this.lista = lista;
        this.notifyDataSetChanged();
    }
}
