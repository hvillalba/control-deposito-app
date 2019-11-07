package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.ResponseData;
import com.artico.delivery.pedidos.data.helpers.RestAdapter;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;
import com.artico.delivery.pedidos.data.models.Branch;
import com.artico.delivery.pedidos.data.models.History;
import com.artico.delivery.pedidos.data.request.CommonInterface;
import com.artico.delivery.pedidos.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gustavo on 9/28/17.
 */
public class DialogCustomRateBranch extends Dialog {

    private Context context;
    private Realm realm;
    private CommonInterface commonInterface;
    private HashMap<Integer, String> map;
    private final int branchId;
    private final int orderId;
    private Branch branch;
    private ImageView imgLocalLogo;

    // Variables para calificar
    private JsonArray feedback_reasons;
    private int calificacionCarita = 5;
    private String comments = "";

    public DialogCustomRateBranch(Context context, int branchId, int orderId, Realm realm) {
        super(context);
        this.realm = realm;
        this.context = context;
        this.branchId = branchId;
        this.orderId = orderId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rate_branch);

        branch = Branch.findOne(branchId, realm);

        // Views
        imgLocalLogo = findViewById(R.id.imgLocal);
        Button btnAceptar = findViewById(R.id.btnAceptar);
        feedback_reasons = new JsonArray();

        // Icono del Franchise
        if (branch.getFranchise().getIcon_attachment() != null) {
            Utils.loadImage(context, imgLocalLogo, branch.getFranchise().getIcon_attachment(), "120", branch.getFranchise().getUpdated_at());
        }

        // TextView para obtener el código desde el dialog
        final EditText etCodigo = findViewById(R.id.etCodigo);

        SmileRating smileRating = findViewById(R.id.smile_rating);
        // Seleccionamos por default
        smileRating.setSelectedSmile(BaseRating.GREAT);

        smileRating.setTextSelectedColor(context.getResources().getColor(R.color.white));
        smileRating.setTextNonSelectedColor(context.getResources().getColor(R.color.grayToolbar));

        // Traducimos el texto de las caritas al español
        smileRating.setNameForSmile(BaseRating.GREAT, "Excelente");
        smileRating.setNameForSmile(BaseRating.GOOD, "Bueno");
        smileRating.setNameForSmile(BaseRating.OKAY, "Normal");
        smileRating.setNameForSmile(BaseRating.BAD, "Malo");
        smileRating.setNameForSmile(BaseRating.TERRIBLE, "Terrible");
        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                calificacionCarita = level;
            }
        });

        map = new HashMap<Integer, String>();
        commonInterface = RestAdapter.getClient(context).create(CommonInterface.class);
        // TODO: Hacer que guarde las razones localmente
        prepareRequest();


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedbackToBackend();
            }
        });
    }

    /**
     * Método para enviar el backend el código
     */
    private void prepareRequest() {
        Call<ResponseData<JsonArray>> response = commonInterface.get_reasons();
        response.enqueue(new Callback<ResponseData<JsonArray>>() {
            @Override
            public void onResponse(Call<ResponseData<JsonArray>> call, Response<ResponseData<JsonArray>> response) {
                if(response.isSuccessful()){
                    // Obtenemos el response desde el backend y lo guardamos
                    JsonArray data = response.body().getData();
                    for(JsonElement e : data){
                        JsonObject o = e.getAsJsonObject();
                        map.put(o.get("id").getAsInt(), o.get("reason_text").getAsString());
                    }
                    setListViewData();
                }else{
                    Utils.showRetrofitError(context, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseData<JsonArray>> call, Throwable t) {
                Utils.showMessage(context, "¡Atención!", "Por favor verifica tus datos y vuelve a intentar.");
            }
        });
    }

    /**
     * Método para setear los datos de calificación a la vista
     */
    private void setListViewData(){
//        ArrayList<HashMap<Integer, String>> detailss = new ArrayList<HashMap<Integer, String>>();
        LinearLayout LisItemList = findViewById(R.id.listViewItems);
//        detailss.add(map);

        for (final Map.Entry<Integer, String> entry : map.entrySet()) {
            View vi = LayoutInflater.from(context).inflate(R.layout.listview_rating, null);
            TextView title = vi.findViewById(R.id.more_item);
            CheckBox checkbox = vi.findViewById(R.id.checkbox);

            // Agregamos al array los checks
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        feedback_reasons.add(entry.getKey());
                    } else{
                        // Iteramos por todos los elementos para eliminar del array si es que se encuentra
                        for (int i=0;i<feedback_reasons.size();i++){
                            if(entry.getKey() == feedback_reasons.get(i).getAsInt()){
                                feedback_reasons.remove(i);
                            }
                        }
                    }
                    Utils.imprimir("lista", feedback_reasons.toString());
                }
            });

            title.setText(entry.getValue());
            LisItemList.addView(vi);
        }
    }

    /**
     * Método para enviar el backend el código
     */
    private void sendFeedbackToBackend() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("order_id", orderId);
        jsonObject.add("feedback_reasons", feedback_reasons);
        jsonObject.addProperty("feedback_stars", calificacionCarita);
        jsonObject.addProperty("feedback_comments", comments);

        Call<JsonObject> response = commonInterface.save_reasons(jsonObject);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    // Obtenemos el response desde el backend y lo guardamos
                    JsonObject data = response.body();
                    if(data.get("success").getAsBoolean()){
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {

                                History h = realm.where(History.class).equalTo("id", orderId).findFirst();
                                realm.beginTransaction();
                                h.setFeedbackComments(comments);
                                h.setFeedbackReasons(feedback_reasons.toString());
                                h.setFeedbackStars(String.valueOf(calificacionCarita));
                                realm.copyToRealmOrUpdate(h);
                                realm.commitTransaction();
//                            }
//                        });

                        com.artico.delivery.pedidos.ui.dialogs.DialogCustomRateBranch.this.dismiss();
                        GlobalBus.getBus().postSticky(new Events.UpdateModelHistory());
                    }
                }else{
                    Utils.showRetrofitError(context, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.showErrorMessage(context, t);
            }
        });
    }
}
