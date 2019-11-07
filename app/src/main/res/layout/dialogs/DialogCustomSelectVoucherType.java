package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.RestAdapter;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.request.VoucherInterface;
import com.artico.delivery.pedidos.utils.Constants;
import com.artico.delivery.pedidos.utils.Utils;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogCustomSelectVoucherType extends Dialog {

    Context context;
    private VoucherInterface voucherInterface;
    private String qrCode;
    private boolean isDelivery;
    private boolean isPickup;

    public DialogCustomSelectVoucherType(Context context, String qrCode, boolean isDelivery, boolean isPickup) {
        super(context);
        this.context = context;
        this.qrCode = qrCode;
        this.isDelivery = isDelivery;
        this.isPickup = isPickup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_voucher_select);
        voucherInterface = RestAdapter.getClient(context).create(VoucherInterface.class);

        // Obtenemos los views
        Button btnDelivery = findViewById(R.id.btnDelivery);
        Button btnPickup = findViewById(R.id.btnPasarBuscar);
        if (!isDelivery){
            btnDelivery.setVisibility(View.GONE);
        }
        if (!isPickup){
            btnPickup.setVisibility(View.GONE);
        }

        // Seteamos los eventos
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidateVoucher();
                GlobalBus.getBus().post(new Events.DialogValeSelectDeliveryType(Constants.TYPE_DELIVERY));
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomSelectVoucherType.this.dismiss();
            }
        });

        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //invalidateVoucher();
                GlobalBus.getBus().post(new Events.DialogValeSelectDeliveryType(Constants.TYPE_PICKUP));
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomSelectVoucherType.this.dismiss();
            }
        });
    }

    /**
     * Método para invalidar el voucher
     */
    private void invalidateVoucher() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("voucher", qrCode);

        Call<JsonObject> response = voucherInterface.mark_unavailable(jsonObject);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject1 = response.body();
                if(response.isSuccessful()){
                    // Obtenemos el response desde el backend y lo guardamos
                    Log.e("Voucer", "Voucher invalidado correctamente");
                }else{
                    try {
                        Log.e("Voucer", "No se pudo invalidar el voucher: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.showMessage(context, "¡Lo sentimos!", "No se pudo invalidar el voucher");
            }
        });
    }


}
