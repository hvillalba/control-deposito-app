package py.hvillalba.control_deposito_app.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.Response;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.dto.Vw_stock;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.ui.dialog.DialogFullAnimation;
import py.hvillalba.control_deposito_app.ui.recyclerlistpicking.RecyclerViewAdapterListArticulos;
import py.hvillalba.control_deposito_app.ui.recyclerlistpicking.RecyclerViewAdapterUbicacion;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import py.hvillalba.control_deposito_app.util.eventbus.Events;
import py.hvillalba.control_deposito_app.util.eventbus.GlobalBus;
import retrofit2.Call;
import retrofit2.Callback;

public class UbicacionActivity extends AppCompatActivity {

    DepositoService depositoService;
    RecyclerView recyclerView;
    RecyclerViewAdapterUbicacion adapter;
    private String idArticulo;
    private String lote;
    DialogFullAnimation dialogFullAnimation;
    Integer repartoItemOid;
    Double cantReparto;
    RepartoItem repartoItem;
    private boolean isScanned;
    private boolean conteo;
    private Integer falta;
    private CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        customPreferenceManager = new CustomPreferenceManager(this);
        setTitle("Ubicaciones");
        dialogFullAnimation = new DialogFullAnimation(this);
        try {
            repartoItem = (RepartoItem) getIntent().getSerializableExtra("repartoItem");
        }catch (ClassCastException e){
            Log.e("Error", e.getMessage());
        }
        conteo = getIntent().getBooleanExtra("conteo",false);
        idArticulo = getIntent().getStringExtra("idArticulo");
        lote = getIntent().getStringExtra("lote");
        cantReparto = getIntent().getDoubleExtra("cantReparto", 0.0);
        repartoItemOid = getIntent().getIntExtra("repartoItemOid", 0);
        falta = getIntent().getIntExtra("falta",0);
        recyclerView = findViewById(R.id.recyclerViewUbicaciones);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        initRecycler();
    }

    /**
     * Metodo que carga la lista mediante un servicio http y luego el recyclerView
     */
    private void initRecycler(){
        dialogFullAnimation.show();
        depositoService = RestAdapter.getClient(customPreferenceManager.getString("ip")).create(DepositoService.class);
        Call<Response<List<Ubicaciones>>> response = depositoService.getUbicacion(idArticulo,lote);
        response.enqueue(new Callback<Response<List<Ubicaciones>>>() {
            @Override
            public void onResponse(Call<Response<List<Ubicaciones>>> call, retrofit2.Response<Response<List<Ubicaciones>>> response) {
                if (response!= null && response.body()!= null && response.body().getCodigo() != null
                        && response.body().getCodigo().equals(200)){
                    adapter = new RecyclerViewAdapterUbicacion(UbicacionActivity.this, response.body().getData(), repartoItemOid, cantReparto, repartoItem, conteo,falta);
                    recyclerView.setAdapter(adapter);
                    dialogFullAnimation.dismiss();
                }else {
                    if (response.body() != null){
                        mostrarMensaje(response.message());
                        dialogFullAnimation.dismiss();
                        Log.e("onResponse", "Error: " + response.body().getMensaje());
                    }else {
                        Log.e("onResponse", "Error: " + response.errorBody());
                        dialogFullAnimation.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<Ubicaciones>>> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                mostrarMensaje(t.getMessage());
            }
        });
    }

    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(UbicacionActivity.this);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cerrarVentana(Events.CerrarVentana mensage){
        UbicacionActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        GlobalBus.getBus().unregister(this);
        super.onStop();

    }
}
