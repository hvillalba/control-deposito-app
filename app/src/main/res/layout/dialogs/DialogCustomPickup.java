package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;

/**
 * Created by hectorvillalba on 4/11/18.
 */

public class DialogCustomPickup extends Dialog {

    private Context context;
    private Button btnPickup;
    private Button btnDelivery;

    public DialogCustomPickup(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom_pickup);

        btnPickup = (Button) findViewById(R.id.btnPickup);
        btnDelivery = (Button) findViewById(R.id.btnDelivery);

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalBus.getBus().post(new Events.PickupEvent(true));
            }
        });
        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalBus.getBus().post(new Events.PickupEvent(false));
            }
        });
    }
}
