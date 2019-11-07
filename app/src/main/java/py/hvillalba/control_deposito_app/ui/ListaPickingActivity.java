package py.hvillalba.control_deposito_app.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.Response;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.ui.dialog.DialogFullAnimation;
import py.hvillalba.control_deposito_app.ui.recyclerlistpicking.RecyclerViewAdapterListPicking;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;

public class ListaPickingActivity extends AppCompatActivity {

    private List<Reparto> repartoList;
    private RecyclerViewAdapterListPicking adapterListPicking;
    private RecyclerView recyclerView;
    private DepositoService depositoService;
    DialogFullAnimation dialogFullAnimation;
    boolean conteo;
    Realm realm;
    private boolean isScanned;
    CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_picking);
        setTitle("Lista Reparto");
        customPreferenceManager = new CustomPreferenceManager(this);
        realm = Realm.getDefaultInstance();
        conteo = getIntent().getBooleanExtra("conteo",false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        dialogFullAnimation =   new DialogFullAnimation(this);
        dialogFullAnimation.show();
        depositoService = RestAdapter.getClient(customPreferenceManager.getString("ip")).create(DepositoService.class);
        if (!conteo){
            Call<Response<List<Reparto>>> response = depositoService.getListReparto();
                response.enqueue(new Callback<Response<List<Reparto>>>() {
                @Override
                public void onResponse(Call<Response<List<Reparto>>> call, retrofit2.Response<Response<List<Reparto>>> response) {
                    if (response != null && response.body() != null && response.body().getCodigo().equals(200)){
                        if (response.body().getData()!= null && !response.body().getData().isEmpty()){
                            adapterListPicking = new RecyclerViewAdapterListPicking(response.body().getData(), ListaPickingActivity.this, conteo);
                            recyclerView.setAdapter(adapterListPicking);
                        }
                        dialogFullAnimation.dismiss();
                    }else {
                        if (response.body()!= null){
                            Log.e("onResponse", response.message());
                            mostrarMensaje(response.body().getMensaje());
                            dialogFullAnimation.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Response<List<Reparto>>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    mostrarMensaje(t.getMessage());
                    dialogFullAnimation.dismiss();
                }
            });
        }else {
            RealmResults<Reparto> repartoList = realm.where(Reparto.class).equalTo("contado", true).findAll();
            List<Reparto> lista = realm.copyFromRealm(repartoList);
            adapterListPicking = new RecyclerViewAdapterListPicking(lista, ListaPickingActivity.this, conteo);
            recyclerView.setAdapter(adapterListPicking);
            dialogFullAnimation.dismiss();
        }
    }

    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(ListaPickingActivity.this);
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
