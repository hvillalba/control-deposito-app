package py.hvillalba.control_deposito_app.ui.qrCamera;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.airbnb.lottie.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.dto.Vw_stock;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.ui.UbicacionActivity;
import py.hvillalba.control_deposito_app.ui.VerificarArticuloActivity;
import py.hvillalba.control_deposito_app.ui.dialog.DialogFullAnimation;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import py.hvillalba.control_deposito_app.util.eventbus.Events;
import py.hvillalba.control_deposito_app.util.eventbus.GlobalBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCameraZxincActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    private String codigo;
    private DepositoService commonInterface;
//
//    // Datos básicos
//    private Realm realm;
//    private CustomPreferenceManager customPreferenceManager;
    private Context context;
    Integer repartoItemOid;

    String[] perms = { Manifest.permission.CAMERA };
    private String TAG = "QrCamera";
    private android.support.v7.widget.Toolbar toolbar;
    private SurfaceView surfaceView;
    private boolean isScanned = false;
    //CameraSource cameraSource = null;
    private Gson gson = new GsonBuilder().create();
    private String idArticulo = "";
    private String lote = "";
    private Ubicaciones ubicaciones;
    private Double cantReparto;
    private RepartoItem repartoItem;
    private boolean conteo;
    private Integer falta;
    CustomPreferenceManager customPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_camera_zxinc);
        customPreferenceManager = new CustomPreferenceManager(this);
        try {
            repartoItem = (RepartoItem) getIntent().getSerializableExtra("repartoItem");
        }catch (ClassCastException e){
            Log.e("Error", e.getMessage());
        }
        conteo = getIntent().getBooleanExtra("conteo",false);
        ubicaciones = (Ubicaciones) getIntent().getSerializableExtra("vw");
        repartoItemOid = getIntent().getIntExtra("repartoItemOid",0);
        cantReparto = getIntent().getDoubleExtra("cantReparto",0.0);
        falta = getIntent().getIntExtra("falta",0);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        //setContentView(mScannerView);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        initActivity();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setAspectTolerance(0.5f);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);

//        realm = Realm.getDefaultInstance();
//        customPreferenceManager = new CustomPreferenceManager(context);
        commonInterface = RestAdapter.getClient(customPreferenceManager.getString("ip")).create(DepositoService.class);
    }

    /**
     * Método para inicializar el activity. De ser necesario cargamos y configuramos las vistas
     */
    private void initActivity() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(QrCameraZxincActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QrCameraZxincActivity.this, new String[]{android.Manifest.permission.CAMERA}, 0);
            return;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        requestPermission();
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * Método para enviar el backend el código
     */
    private void prepareRequest() {
        // Mostramos el dialogo de espera
        final DialogFullAnimation dialogFullAnimation =   new DialogFullAnimation(context);
        dialogFullAnimation.show();

        //Utils.showLoading(context);
        Call<py.hvillalba.control_deposito_app.dto.Response<List<Ubicaciones>>> response = commonInterface.getUbicacion(idArticulo,lote);
        response.enqueue(new Callback<py.hvillalba.control_deposito_app.dto.Response<List<Ubicaciones>>>() {
            @Override
            public void onResponse(Call<py.hvillalba.control_deposito_app.dto.Response<List<Ubicaciones>>> call, Response<py.hvillalba.control_deposito_app.dto.Response<List<Ubicaciones>>> response) {
                if (response.body() != null && response.body().getCodigo().equals(200)){
                    if (!response.body().getData().isEmpty()){
                        if (ubicaciones.getCodigoArticulo().equals(response.body().getData().get(0).getCodigoArticulo()) &&
                                ubicaciones.getCalle().getCodigo().equals(response.body().getData().get(0).getCalle().getCodigo()) &&
                                ubicaciones.getOid().equals(response.body().getData().get(0).getOid())){
                            if (!conteo){
                                QrCameraZxincActivity.this.finish();
                                dialogFullAnimation.dismiss();
                                Intent intent = new Intent(QrCameraZxincActivity.this, QrCameraZxincUbicacionesActivity.class);
                                intent.putExtra("repartoItemOid", repartoItemOid);
                                intent.putExtra("ubicacion", ubicaciones);
                                intent.putExtra("cantReparto", cantReparto);
                                intent.putExtra("falta", falta);
                                if (repartoItem !=null){
                                    intent.putExtra("repartoItem", repartoItem);
                                }
                                startActivity(intent);
                            }else {
                                QrCameraZxincActivity.this.finish();
                                dialogFullAnimation.dismiss();
                                Intent intent = new Intent(QrCameraZxincActivity.this, VerificarArticuloActivity.class);
                                intent.putExtra("repartoItemOid", repartoItemOid);
                                intent.putExtra("ubicacion", ubicaciones);
                                intent.putExtra("cantReparto", cantReparto);
                                intent.putExtra("falta", falta);
                                if (repartoItem !=null){
                                    intent.putExtra("repartoItem", repartoItem);
                                }
                                startActivity(intent);
                            }
                        }else {
                            Log.e("onResponse", "El producto o ubicación no coinciden");
                            mostrarMensaje("El producto o ubicación no coinciden");
                            dialogFullAnimation.dismiss();
                        }
                    }else {
                        Log.e("onResponse", "El producto o ubicación no coinciden");
                        mostrarMensaje("El producto o ubicación no coinciden");
                        dialogFullAnimation.dismiss();
                    }
                }else {
                    if (response.body()!= null){
                        Log.e("onResponse", response.body().getMensaje());
                    }else {
                        mostrarMensaje(response.message());
                        try {
                            Log.e("onResponse",response.errorBody().string());
                            dialogFullAnimation.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<py.hvillalba.control_deposito_app.dto.Response<List<Ubicaciones>>> call, Throwable t) {
                    Log.e("onFailure", t.getMessage());
                    mostrarMensaje(t.getMessage());
                    dialogFullAnimation.dismiss();
            }
        });
    }


    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(QrCameraZxincActivity.this);
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



    @Override
    public void handleResult(final Result result) {
        if (result.getText() != null && !result.equals("")){
            isScanned = true;
            playSound(context, R.raw.vouchersound);
            QrCameraZxincActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (result.getText().contains("|")){
                        String[] var = String.valueOf(result.getText()).split("\\|");
                        idArticulo = var[0];
                        lote = var[1];
                        prepareRequest();
                    }else {
                        mostrarMensaje("Codigo invalido");
                    }

                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(QrCameraZxincActivity.this);
                }
            }, 2000);
        }
    }


    /**
     * Método para ejecutar un sonido en el caso de que se tenga configurado como true
     * @param context
     * @param fileId
     */
    public static void playSound(Context context, int fileId){
            // Sonidos
            try{
                MediaPlayer.create(context, fileId).start();
            }catch (Exception e){
                Log.e("sonido", e.toString(), e);
            }

    }
}
