package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Cart;
import com.artico.delivery.pedidos.data.models.Ruc;
import com.artico.delivery.pedidos.ui.dialogs.adapters_recycler.RecyclerViewRucAdapter;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by Gustavo on 9/28/17.
 */
public class DialogCustomCartListFactura extends Dialog {

    private Context context;
    private Realm realm;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private RecyclerViewRucAdapter rcAdapter;
    private ImageButton imageButton;
    private List<Ruc> rucs;
    private Cart cart;

    public DialogCustomCartListFactura(Context context, Realm realm, Cart cart) {
        super(context);
        this.context = context;
        this.realm = realm;
        this.cart = cart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cart_list_factura);
        loadDataFactura();
        initRecycler();
        imageButton = findViewById(R.id.btnAgregarFactura);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalBus.getBus().postSticky(new Events.AgregarFacturaEvent(cart));
            }
        });
    }

    /**
     * Inicializamos el RecyclerView
     */
    private void initRecycler() {
        // Recycler View
        recyclerView = findViewById(R.id.recyclerRucFactura);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rcAdapter = new RecyclerViewRucAdapter(context, rucs, realm);
        recyclerView.setAdapter(rcAdapter);
    }

    /**
     * Metodo para cargar las facturas del cliente
     *
     */
    private void loadDataFactura(){
        // Primero seteamos como false todos los "isDefault"
        rucs = realm.copyFromRealm(realm.where(Ruc.class).sort("is_default", Sort.DESCENDING).findAll());
    }

    public void updateInvoiceList(Ruc ruc){
        rucs = realm.copyFromRealm(realm.where(Ruc.class).sort("is_default", Sort.DESCENDING).findAll());
        rcAdapter.notify(rucs);
        realm.beginTransaction();
        cart.setInvoice_ruc(ruc.getRuc());
        cart.setInvoice_name(ruc.getRazonSocial());
        cart.setInvoice_required(true);
        realm.copyToRealmOrUpdate(cart);
        realm.commitTransaction();
    }

    public void listChanged(){
        rucs = realm.copyFromRealm(realm.where(Ruc.class).sort("is_default", Sort.DESCENDING).findAll());
        rcAdapter.notify(rucs);
    }


}
