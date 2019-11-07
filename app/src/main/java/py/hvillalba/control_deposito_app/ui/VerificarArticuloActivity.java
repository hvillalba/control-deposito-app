package py.hvillalba.control_deposito_app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.RepartoPicking;
import py.hvillalba.control_deposito_app.dto.Response;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;

public class VerificarArticuloActivity extends AppCompatActivity {

    DepositoService depositoService;
    Ubicaciones ubicaciones;
    EditText tvCantidad;
    TextView tvCantidadReparto, tvRequerido, tvFaltante,tvCantidadFaltante;
    Button btnAceptar;
    Integer repartoItemOid;
    Double cantReparto;
    RepartoItem repartoItem;
    Realm realm;
    private boolean isScanned;
    private Integer falta;
    CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_articulo);
        customPreferenceManager = new CustomPreferenceManager(this);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        realm = Realm.getDefaultInstance();
        ubicaciones = (Ubicaciones) getIntent().getSerializableExtra("ubicacion");
        repartoItemOid = getIntent().getIntExtra("repartoItemOid",0);
        cantReparto = getIntent().getDoubleExtra("cantReparto", 0.0);
        falta = getIntent().getIntExtra("falta",0);
        try {
            repartoItem = (RepartoItem) getIntent().getSerializableExtra("repartoItem");
        }catch (ClassCastException e){
            Log.e("Error", e.getMessage());
        }
        if (repartoItem == null){
            repartoItem = realm.where(RepartoItem.class).equalTo("oid", repartoItemOid).findFirst();
        }
        depositoService = RestAdapter.getClient(customPreferenceManager.getString("ip")).create(DepositoService.class);
        DecimalFormat df = new DecimalFormat("###,###,###.##");
        btnAceptar = findViewById(R.id.btnAceptar);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvCantidadFaltante = findViewById(R.id.tvCantidadFaltante);
        tvFaltante = findViewById(R.id.tvFaltante);
        tvCantidadReparto = findViewById(R.id.tvCantidadReparto);
        tvCantidadReparto.setText(df.format(cantReparto));
        tvRequerido = findViewById(R.id.tvRequerido);
        if (repartoItem.isContado()){
            tvCantidadReparto.setVisibility(View.GONE);
            tvRequerido.setText("Por favor ingrese cuantos items del producto hay en la jaula...!!!");
        }else {
            tvFaltante.setVisibility(View.VISIBLE);
            tvCantidadFaltante.setVisibility(View.VISIBLE);
            tvCantidadFaltante.setText(falta.toString());
        }
        btnAceptar.setOnClickListener(e -> {
            if (tvCantidad.getText().toString() == null || tvCantidad.getText().toString().equals("")){
                tvCantidad.setError("El campo no puede quedar vacio");
                tvCantidad.requestFocus();
                return;
            }
            Integer cantidad = Integer.parseInt(tvCantidad.getText().toString());
            if (cantidad <= 0){
                tvCantidad.setError("La cantidad debe ser mayor que Cero");
                tvCantidad.requestFocus();
                return;
            }

            if (Double.parseDouble(tvCantidad.getText().toString())> cantReparto){
                tvCantidad.setError("No puede superar la cantidad requerida");
                tvCantidad.requestFocus();
                return;
            }
            cantidad += repartoItem.getCantidadIngresada()== null ? 0 : repartoItem.getCantidadIngresada();
            if (!repartoItem.isContado()){
                Call<Response<RepartoPicking>> responseCall = depositoService
                        .updateRepartoPicking(ubicaciones.getOid(), repartoItem.getOid(), Double.parseDouble(tvCantidad.getText().toString()));
                Integer finalCantidad = cantidad;
                responseCall.enqueue(new Callback<Response<RepartoPicking>>() {
                    @Override
                    public void onResponse(Call<Response<RepartoPicking>> call, retrofit2.Response<Response<RepartoPicking>> response) {
                        if (response.body()!= null){
                            if (response.body().getCodigo().equals(200)){
                                if (finalCantidad.equals(repartoItem.getCantidad().intValue())){
                                    realm.beginTransaction();
                                    Reparto reparto = repartoItem.getReparto();
                                    RealmList<RepartoItem> lista = new RealmList<>();
                                    repartoItem.setContado(true);
                                    reparto.setContado(true);
                                    lista.add(repartoItem);
                                    if (reparto.getRepartoItemList()!= null && reparto.getRepartoItemList().isEmpty()){
                                        reparto.getRepartoItemList().add(repartoItem);
                                    }else {
                                        reparto.setRepartoItemList(lista);
                                    }
                                    realm.copyToRealmOrUpdate(reparto);
                                    realm.commitTransaction();
                                }
                                Toast.makeText(VerificarArticuloActivity.this,"Proceso satisfactorio", Toast.LENGTH_LONG).show();
                                Log.e("onResponse", response.body().getMensaje());
                                VerificarArticuloActivity.this.finish();
                            }else {
                                Log.e("onResponse", response.body().getMensaje());
                                mostrarMensaje(response.body().getMensaje());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<RepartoPicking>> call, Throwable t) {
                        Log.e("onFailure", t.getMessage());
                        mostrarMensaje(t.getMessage());
                    }
                });
            }else {
                if (Double.parseDouble(tvCantidad.getText().toString()) < cantReparto){
                    tvCantidad.setError("La cantidad del producto debe coincidir a lo solicitado en el Reparto");
                    tvCantidad.requestFocus();
                    return;
                }
                Call<Response<RepartoItem>> responseCall = depositoService.updateRepartoPicking(repartoItem.getOid());
                responseCall.enqueue(new Callback<Response<RepartoItem>>() {
                    @Override
                    public void onResponse(Call<Response<RepartoItem>> call, retrofit2.Response<Response<RepartoItem>> response) {
                        if (response.body()!= null){
                            if (response.body().getCodigo().equals(200)){
                                Log.e("onResponse", response.body().getMensaje());
                                RepartoItem repartoItem = response.body().getData();
                                Reparto reparto = repartoItem.getReparto();
                                realm.beginTransaction();
                                repartoItem.setConfirmado(true);
                                realm.copyToRealmOrUpdate(repartoItem);
                                realm.commitTransaction();
                                realm.beginTransaction();
                                reparto.setContado(true);
                                realm.copyToRealmOrUpdate(reparto);
                                realm.commitTransaction();
                                Toast.makeText(VerificarArticuloActivity.this,"Proceso satisfactorio", Toast.LENGTH_LONG).show();
                                VerificarArticuloActivity.this.finish();
                            }else {
                                mostrarMensaje(response.body().getMensaje());
                                Log.e("onResponse", response.body().getMensaje());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<RepartoItem>> call, Throwable t) {
                        Log.e("onFailure", t.getMessage());
                        mostrarMensaje(t.getMessage());
                    }
                });
            }

        });
    }

    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificarArticuloActivity.this);
        builder.setTitle("¡Lo sentimos!");
        builder.setMessage(error);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                isScanned = false;
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
