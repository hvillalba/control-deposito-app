package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.models.Cart;
import com.artico.delivery.pedidos.utils.Utils;

import io.realm.Realm;

/**
 * Created by Gustavo on 9/28/17.
 */
public class DialogCustomCartAmount extends Dialog {

    private Context context;
    private Cart objeto;
    private Realm realm;
    private double total = 0;

    // Inicializamos los datos de éste dialogo
    public DialogCustomCartAmount(Context context, Cart objeto, Realm realm) {
        super(context);
        this.context = context;
        this.objeto = objeto;
        this.realm = realm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cart_amount);

        // Obtenemos los campos del popup
        final Button btnExactAmount = findViewById(R.id.btnExactAmount);
        final Button btnEnviar = findViewById(R.id.btnEnviar);
        final EditText etMonto = findViewById(R.id.etMonto);

        // Seteamos en una variable el monto total
        if(Utils.getDeliveryTypeBoolean(objeto.getDelivery_type())){
            // Si es delivery sumamos el delivery
            total = objeto.getTotal_amount() + objeto.getDelivery_price();
        }else{
            total = objeto.getTotal_amount();
        }

        // Seteamos el monto de ejemplo en el EditText
        btnExactAmount.setText("Monto Exacto (" + Utils.miles(String.valueOf(total)) +" Gs.)");
        roundUp(etMonto);

        // Botón para setear el monto exacto
        btnExactAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                objeto.setClient_cash_amount(total);
                objeto.setPayment_type("CASH");
                realm.commitTransaction();
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomCartAmount.this.dismiss();
            }
        });

        // Botón para setear un monto personalizado
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientAmount = etMonto.getText().toString();

                if(clientAmount.isEmpty()){
                    etMonto.setError("Debe escribir el monto o seleccionar la opción de 'Monto Exacto'");
                    etMonto.requestFocus();
                }else{
                    // Si el monto
                    if(Integer.valueOf(clientAmount) < total){
                        Utils.showMessage(context, "¡Atención!", "El monto ingresado no puede ser menor al total del pedido.");
                    }else{
                        realm.beginTransaction();
                        objeto.setClient_cash_amount(Integer.valueOf(clientAmount));
                        objeto.setPayment_type("CASH");
                        realm.commitTransaction();
                        com.artico.delivery.pedidos.ui.dialogs.DialogCustomCartAmount.this.dismiss();
                    }
                }
            }
        });
    }

    /**
     * Método para redondear hacia arriba, agregando un monto a 50 o 100
     * @param etMonto
     * @return
     */
   private void roundUp(EditText etMonto){
       Double montoTotal = Double.valueOf(total + objeto.getDelivery_price());

       // montoTotal = Integer.valueOf(String.valueOf(100 * Math.round(montoTotal/100.0))); // Redondeamos a la centena mas cercana
       //etMonto.setText(String.valueOf(montoTotal));

//       if(monto%50){
//
//       }
//
//       return monto + ( 50 - ( monto % 50) ? monto : 50 ) )
   }
}