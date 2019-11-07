package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Cart;
import com.artico.delivery.pedidos.data.models.Ruc;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Gustavo on 9/28/17.
 */
public class DialogCustomCartFactura extends Dialog {

    private Context context;
    private Realm realm;
    private Ruc r;
    private Cart cart;

    public DialogCustomCartFactura(Context context, Realm realm, Cart cart) {
        super(context);
        this.context = context;
        this.realm = realm;
        this.cart = cart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cart_factura);

        // TextView para obtener el código desde el dialog
        final EditText etRazonSocial = findViewById(R.id.etRazonSocial);
        final EditText etRuc = findViewById(R.id.etRuc);
        final Button btnEnviar = findViewById(R.id.btnEnviar);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si los campos son válidos guardamos en la BD local
                if(isFieldValid(etRuc) && isFieldValid(etRazonSocial)){
                    saveRucData(etRuc.getText().toString(), etRazonSocial.getText().toString());
                    GlobalBus.getBus().post(new Events.FacturaAgregadaEvent(r));
                    GlobalBus.getBus().postSticky(new Events.AgregarRucPorDefaultEvent());
                    com.artico.delivery.pedidos.ui.dialogs.DialogCustomCartFactura.this.dismiss();
                }
            }
        });
    }

    /**
     * Método para guardar los datos del ruc y la razon social a la base de datos local (también seteamos como backup en el user)
     * @param ruc
     * @param razonSocial
     */
    private void saveRucData(String ruc, String razonSocial){
        realm.beginTransaction();
        // Primero seteamos como false todos los "isDefault"
        RealmResults<Ruc> rucs = realm.where(Ruc.class).findAll();
        for(Ruc r : rucs){
            r.setIs_default(false);
        }

        // creamos un nuevo objeto RUC y lo guardamos
        r = new Ruc();
        r.setRuc(ruc);
        r.setRazonSocial(razonSocial);
        r.setIs_default(true);

        realm.copyToRealmOrUpdate(r);
        realm.commitTransaction();
        updateCart(ruc, razonSocial);
    }

    private void updateCart(String ruc, String razonSocial){
        if(cart != null){
            realm.beginTransaction();
            cart.setInvoice_name(razonSocial);
            cart.setInvoice_ruc(ruc);
            cart.setInvoice_required(true);
            realm.copyToRealmOrUpdate(r);
            realm.copyToRealmOrUpdate(cart);
            realm.commitTransaction();
        }
    }

    /**
     * Método para validar el campo password
     * @param view
     * @return
     */
    private boolean isFieldValid(EditText view){
        String text = view.getText().toString();
        // Si esta vacio
        if (TextUtils.isEmpty(text) ){
            view.setError("Este campo no puede estar vacio");
            view.requestFocus();
            return false;
        }

        // Si tiene menos de 3 caracteres
        if(text.length()<=3){
            view.setError("Este campo debe tener mas de 3 carácteres");
            view.requestFocus();
            return false;
        }
        return true;
    }
}
