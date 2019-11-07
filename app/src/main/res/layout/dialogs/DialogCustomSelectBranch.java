package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.RestAdapter;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Branch;
import com.artico.delivery.pedidos.data.request.VoucherInterface;
import com.artico.delivery.pedidos.ui.branches.BranchActivity;
import com.artico.delivery.pedidos.ui.categorias.map.BranchMapsActivity;
import com.artico.delivery.pedidos.utils.Constants;
import com.artico.delivery.pedidos.utils.Utils;
import com.google.gson.JsonObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogCustomSelectBranch extends Dialog {

    Context context;
    private final Realm realm;
    private final int franchiseId;
    private boolean isSpecialProduct;
    private String qrCode;
    private VoucherInterface voucherInterface;

    public DialogCustomSelectBranch(Context context, Realm realm, int franchiseId, boolean isSpecialProduct, String qrCode) {
        super(context);
        this.context = context;
        this.realm = realm;
        this.franchiseId = franchiseId;
        this.isSpecialProduct = isSpecialProduct;
        this.qrCode = qrCode;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_branch);
        voucherInterface = RestAdapter.getClient(context).create(VoucherInterface.class);

        // Obtenemos todos los branch del franchise seleccionado
        RealmResults<Branch> listBranches = realm.where(Branch.class)
                .equalTo("franchise_id", franchiseId)
                .equalTo("is_pickup_enabled", true)
                .equalTo("deleted", false)
                .equalTo("enabled", true)
                .equalTo("franchise.enabled", true)
//                    .lessThan("filter_distance", Constants.FILTRO_DISTANCIA_PICKUP)
                .equalTo("franchise.deleted", false).findAll().where()
                .sort("filter_distance")
                .findAll();

        LinearLayout llBranchList = findViewById(R.id.llBranches);
        // Iteramos por cada branch asociado al franchise
        for(final Branch b: listBranches) {
            // Obtenemos los views del view(?)
            View listViewSucursales = LayoutInflater.from(context).inflate(R.layout.dialog_select_branch_simple_list, null);
            TextView tvDireccionUno = listViewSucursales.findViewById(R.id.tvDireccionUno);
            TextView tvDireccionDos = listViewSucursales.findViewById(R.id.tvDireccionDos);
            TextView tvTituloLocal = listViewSucursales.findViewById(R.id.tvTituloLocal);
            TextView tvDistancia = listViewSucursales.findViewById(R.id.tvDistancia);
            RelativeLayout rlItem = listViewSucursales.findViewById(R.id.rlItem);
            ImageView ivMapa = listViewSucursales.findViewById(R.id.ivMapa);

            // Seteamos los textos
            tvDireccionUno.setText(b.getStreet1());
            tvDireccionDos.setText(b.getStreet2());
            tvTituloLocal.setText(b.getName());
            tvDistancia.setText(Utils.getTwoDecimalPoints(b.getFilter_distance()));
            // Seteamos el evento OnClick
            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (qrCode!= null && !qrCode.equals("")){
                        invalidateVoucher();
                    }
                    // Verificamos si esta la bandera activa
                    if(isSpecialProduct){
                        GlobalBus.getBus().post(new Events.DialogValeSetBranchId(b.getId()));
                        com.artico.delivery.pedidos.ui.dialogs.DialogCustomSelectBranch.this.dismiss();
                        return;
                    }



                    Intent intent = new Intent(context, BranchActivity.class);
                    // Put extras
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.FRANCHISE_ID, b.getFranchise_id());
                    bundle.putInt(Constants.BRANCH_ID, b.getId());
                    bundle.putBoolean(Constants.ACTIVITY_IS_DELIVERY, false);
                    intent.putExtras(bundle);
                    // Launch the activity, of course ... -_-
                    context.startActivity(intent);
                    com.artico.delivery.pedidos.ui.dialogs.DialogCustomSelectBranch.this.dismiss();
                }
            });

            // Evento OnClick del Mapa
            ivMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, BranchMapsActivity.class);
                    // Put extras
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.BRANCH_ID, b.getId());
                    i.putExtras(bundle);
                    context.startActivity(i);
                }
            });

            llBranchList.addView(listViewSucursales);
        }
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
