package py.hvillalba.control_deposito_app.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Articulo;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.Response;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.ui.dialog.DialogFullAnimation;
import py.hvillalba.control_deposito_app.ui.qrCamera.QrCameraZxincActivity;
import py.hvillalba.control_deposito_app.ui.recyclerlistpicking.RecyclerViewAdapterListArticulos;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;

public class ArticuloListActivity extends AppCompatActivity {

    DepositoService depositoService;
    RecyclerView recyclerView;
    RecyclerViewAdapterListArticulos adapter;
    Reparto reparto;
    DialogFullAnimation dialogFullAnimation;
    boolean conteo =false;
    Realm realm;
    private boolean isScanned;
    private Integer oid;
    private Button btnConfirmarReparto;
    private CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customPreferenceManager = new CustomPreferenceManager(this);
        setContentView(R.layout.activity_articulo_list);
        setTitle("Articulos");
        btnConfirmarReparto = findViewById(R.id.btnConfirmarReparto);
        realm = Realm.getDefaultInstance();
        oid =  getIntent().getIntExtra("oid",0);
        conteo = getIntent().getBooleanExtra("conteo",false);
        depositoService = RestAdapter.getClient(customPreferenceManager.getString("ip")).create(DepositoService.class);
        recyclerView = findViewById(R.id.recyclerViewListArticulos);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        btnConfirmarReparto.setOnClickListener(e ->{
            Call<Response<Reparto>> responseCall = depositoService.updateReparto(oid);
            responseCall.enqueue(new Callback<Response<Reparto>>() {
                @Override
                public void onResponse(Call<Response<Reparto>> call, retrofit2.Response<Response<Reparto>> response) {
                    if (response.body()!= null){
                        if (response.body().getCodigo().equals(200)){
                            Log.e("onResponse", response.body().getMensaje());
                            mostrarMensaje(response.body().getMensaje());
                            Toast.makeText(ArticuloListActivity.this,"Proceso satisfactorio", Toast.LENGTH_LONG).show();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    //Borramos los items de la tabla RepartoItem...
                                    realm.where(RepartoItem.class).equalTo("reparto.oid", oid).findAll().deleteAllFromRealm();
                                    realm.where(Reparto.class).equalTo("oid", oid).findAll().deleteAllFromRealm();
                                    Log.e("drop","Borramos los items de la tabla RepartoItem");

                                }
                            });
                            ArticuloListActivity.this.finish();
                        }else {
                            mostrarMensaje(response.body().getMensaje());
                            Log.e("onResponse", response.body().getMensaje());
                            mostrarMensaje(response.body().getMensaje());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<Reparto>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    mostrarMensaje(t.getMessage());
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler();
    }

    /**
     * Metodo que carga la lista mediante un servicio http y luego el recyclerView
     */
    private void initRecycler(){
        dialogFullAnimation = new DialogFullAnimation(this);
        dialogFullAnimation.show();
        if (conteo){
            RealmResults<RepartoItem> itemRealmResults = realm.where(RepartoItem.class).equalTo("reparto.oid", oid).findAll();
            List<RepartoItem> repartoItemList = realm.copyFromRealm(itemRealmResults);
            adapter = new RecyclerViewAdapterListArticulos(ArticuloListActivity.this, repartoItemList, conteo);
            recyclerView.setAdapter(adapter);
            Call<Response<List<RepartoItem>>> response = depositoService.getListRepartoItemArticulosAll(oid);
            response.enqueue(new Callback<Response<List<RepartoItem>>>() {
                @Override
                public void onResponse(Call<Response<List<RepartoItem>>> call, retrofit2.Response<Response<List<RepartoItem>>> response) {
                    if (response != null && response.body() != null && response.body().getCodigo() != null
                            && response.body().getCodigo().equals(200)) {
                        if (response.body().getData().size() == repartoItemList.size()){
                            btnConfirmarReparto.setVisibility(View.VISIBLE);
                            for (RepartoItem repartoItem : response.body().getData()){
                                if (!repartoItem.getConfirmado()){
                                    btnConfirmarReparto.setVisibility(View.GONE);
                                }
                            }

                        }
                        dialogFullAnimation.dismiss();
                    } else {
                        if (response.body() != null) {
                            Log.e("onResponse", "Error: " + response.body().getMensaje());
                            mostrarMensaje(response.body().getMensaje());
                            dialogFullAnimation.dismiss();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Response<List<RepartoItem>>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    mostrarMensaje(t.getMessage());
                    dialogFullAnimation.dismiss();
                }
            });
        }else {
            Call<Response<List<RepartoItem>>> response = depositoService.getListRepartoItemArticulos(oid);
            response.enqueue(new Callback<Response<List<RepartoItem>>>() {
                @Override
                public void onResponse(Call<Response<List<RepartoItem>>> call, retrofit2.Response<Response<List<RepartoItem>>> response) {
                    if (response != null && response.body() != null && response.body().getCodigo() != null
                            && response.body().getCodigo().equals(200)) {
                        dialogFullAnimation.dismiss();
                        adapter = new RecyclerViewAdapterListArticulos(ArticuloListActivity.this, response.body().getData(), conteo);
                        recyclerView.setAdapter(adapter);
                        dialogFullAnimation.dismiss();
                    } else {
                        if (response.body() != null) {
                            Log.e("onResponse", "Error: " + response.body().getMensaje());
                            mostrarMensaje(response.body().getMensaje());
                            dialogFullAnimation.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<List<RepartoItem>>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    mostrarMensaje(t.getMessage());
                    dialogFullAnimation.dismiss();
                }
            });
        }
    }

    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(ArticuloListActivity.this);
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
