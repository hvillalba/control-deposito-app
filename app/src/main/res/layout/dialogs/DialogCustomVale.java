package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.dto.ProductDto;
import com.artico.delivery.pedidos.data.helpers.ResponseData;
import com.artico.delivery.pedidos.data.helpers.RestAdapter;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Branch;
import com.artico.delivery.pedidos.data.models.Product;
import com.artico.delivery.pedidos.data.request.CommonInterface;
import com.artico.delivery.pedidos.data.request.VoucherInterface;
import com.artico.delivery.pedidos.data.singleton.CacheProduct;
import com.artico.delivery.pedidos.ui.main.MainActivity;
import com.artico.delivery.pedidos.ui.productPreview.ProductPreviewCustomData;
import com.artico.delivery.pedidos.utils.Constants;
import com.artico.delivery.pedidos.utils.CustomPreferenceManager;
import com.artico.delivery.pedidos.utils.Utils;
import com.artico.delivery.pedidos.utils.UtilsFilter;
import com.artico.delivery.pedidos.utils.qrCamera.QrCameraZxincActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogCustomVale extends Dialog {

    private String codigo;
    private CommonInterface commonInterface;
    private VoucherInterface voucherInterface;
    private Gson gson = new GsonBuilder().create();

    // Datos básicos
    private String TAG = "PromoActivity";
    private Realm realm;
    private CustomPreferenceManager customPreferenceManager;
    private Context context;

    public DialogCustomVale(Context context, Realm realm) {
        super(context);
        this.realm = realm;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_vales);

        customPreferenceManager = new CustomPreferenceManager(context);

        // TextView para obtener el código desde el dialog
        final EditText etCodigo = findViewById(R.id.etCodigo);
//        etCodigo.setText("b7f204");

        commonInterface = RestAdapter.getClient(context).create(CommonInterface.class);

        // Botón para abrir la cámara
        Button btnCodigoQR = findViewById(R.id.btnCodigoQR);
        btnCodigoQR.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, QrCameraZxincActivity.class);
                context.startActivity(i);
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomVale.this.dismiss();
            }
        });

        // Botón para enviar el código al endpoint
        Button btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String codstr = etCodigo.getText().toString();
                if(codstr.isEmpty()){
                    Utils.showShortToast(context, "Debe ingresar el código");
                }else{
                    com.artico.delivery.pedidos.ui.dialogs.DialogCustomVale.this.dismiss();
                    // Mostramos el dialogo de espera
                    new com.artico.delivery.pedidos.ui.dialogs.DialogFullAnimation(context).show();

                    codigo = etCodigo.getText().toString();
                    prepareRequest();
                }
            }
        });
    }

    /**
     * Método para enviar el backend el código
     */
    private void prepareRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("voucher", codigo);

        Call<ResponseData<JsonObject>> response = commonInterface.validate_vale(jsonObject);
        response.enqueue(new Callback<ResponseData<JsonObject>>() {
            @Override
            public void onResponse(Call<ResponseData<JsonObject>> call, Response<ResponseData<JsonObject>> response) {
                if(response.isSuccessful()){
                    // Obtenemos el response desde el backend y lo guardamos
                    JsonObject data = response.body().getData().get("product").getAsJsonObject();
                    Type listType = new TypeToken<ProductDto>(){}.getType();
                    Log.e("productJson", data.toString());
                    ProductDto productDto = gson.fromJson(data.toString(), listType);
                    int franchisId = data.get("franchise_id").getAsInt();
                    //Branch branch = UtilsFilter.getBranchByFranchise(franchisId, realm);
                    Branch branch = UtilsFilter.getBranchByFranchise(franchisId, realm);
                    if (branch != null){
                        List<ProductDto> lista = new ArrayList<>();
                        lista.add(productDto);
                        List<Product> listaPruct = new ArrayList<>();
                        listaPruct = Product.cargarProductos(lista, realm);
                        //Product p = Product.createOrUpdate(data.get("id").getAsInt(), branch.getId(), data, realm);
                        if(!branch.isEnabled()){
                            GlobalBus.getBus().post(new Events.showAnimationVale(2));
                            Utils.showMessage(context, "¡Lo sentimos!","Este local no se encuentra disponible o esta cerrado en este momento");
                            return;
                        }
//                        if(branch.isFilter_is_close_today() ||
//                                branch.isFilter_is_recently_closed()||
//                                !branch.isFilter_is_open()) {
//                            GlobalBus.getBus().post(new Events.showAnimationVale(2));
//                            Utils.showMessage(context, "¡Lo sentimos!","Este local no se encuentra disponible o esta cerrado en este momento");
//                            return;
//                        }
                        if(!listaPruct.get(0).getFranchise().isEnabled()){
                            GlobalBus.getBus().post(new Events.showAnimationVale(2));
                            Utils.showMessage(context, "¡Lo sentimos!","La Franquicia se encuentra deshabilitada en este momento");
                            return;
                        }
                        openProductDetails(listaPruct.get(0));
                    }else {
                        GlobalBus.getBus().post(new Events.showAnimationVale(2));
                        Utils.showMessage(context, "¡Lo sentimos!","Este local no se encuentra disponible para tu dirección.");
                    }
                }else{
                    GlobalBus.getBus().post(new Events.showAnimationVale(2));
                    Utils.showRetrofitError(context, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<JsonObject>> call, Throwable t) {
                GlobalBus.getBus().post(new Events.showAnimationVale(2));
                Utils.showMessage(context, "¡Lo sentimos!", "Por favor verifica tus datos y vuelve a intentar.");
            }
        });
    }

    /**
     * Mostramos los detalles del producto
     * @param objeto
     */
    private void openProductDetails(final Product objeto) {
        GlobalBus.getBus().post(new Events.showAnimationVale(1));
        CacheProduct cacheProduct = CacheProduct.getInstance();
        cacheProduct.setProduct(objeto);
        Handler handler = new Handler();
        Runnable startActivity = new Runnable() {
            public void run() {
                Intent intent = new Intent(context, ProductPreviewCustomData.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.PRODUCT_ID, objeto.getId());
                bundle.putBoolean(Constants.ACTIVITY_IS_DELIVERY, true);
                bundle.putString("qrCode",codigo);
                intent.putExtras(bundle);
                context.startActivity(intent);
                ((MainActivity)context).overridePendingTransition(R.anim.slide_in_scale, android.R.anim.fade_out);
                com.artico.delivery.pedidos.ui.dialogs.DialogCustomVale.this.dismiss();
            }
        };

        handler.postDelayed(startActivity, 1000);
    }



}
