package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.request.CommonInterface;
import com.artico.delivery.pedidos.utils.Constants;
import com.artico.delivery.pedidos.utils.CustomPreferenceManager;

import io.realm.Realm;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogCustomDeliveryPickup extends Dialog {

    private String codigo;
    private CommonInterface commonInterface;

    // Datos básicos
    private String TAG = "PromoActivity";
    private Realm realm;
    private CustomPreferenceManager customPreferenceManager;
    private Context context;

    public DialogCustomDeliveryPickup(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delivery_pickup);

        // Obtenemos los views
        ImageView btnDelivery = findViewById(R.id.ivDelivery);
        ImageView btnPickup = findViewById(R.id.ivPickup);

        // Seteamos los eventos
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalBus.getBus().post(new Events.DialogValeSelectDeliveryType(Constants.TYPE_DELIVERY));
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomDeliveryPickup.this.dismiss();
            }
        });

        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalBus.getBus().post(new Events.DialogValeSelectDeliveryType(Constants.TYPE_PICKUP));
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomDeliveryPickup.this.dismiss();
            }
        });


    }


}
