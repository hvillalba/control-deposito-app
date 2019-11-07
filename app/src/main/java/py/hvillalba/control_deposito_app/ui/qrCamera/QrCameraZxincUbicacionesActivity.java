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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.http.RestAdapter;
import py.hvillalba.control_deposito_app.http.service.DepositoService;
import py.hvillalba.control_deposito_app.ui.VerificarArticuloActivity;
import py.hvillalba.control_deposito_app.ui.dialog.DialogFullAnimation;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCameraZxincUbicacionesActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

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
    private Integer falta;
    CustomPreferenceManager customPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_camera_zxinc_ubi);
        customPreferenceManager = new CustomPreferenceManager(this);
        try {
            repartoItem = (RepartoItem) getIntent().getSerializableExtra("repartoItem");
        }catch (ClassCastException e){
            Log.e("Error", e.getMessage());
        }
        ubicaciones = (Ubicaciones) getIntent().getSerializableExtra("ubicacion");
        repartoItemOid = getIntent().getIntExtra("repartoItemOid",0);
        cantReparto = getIntent().getDoubleExtra("cantReparto",0.0);
        falta = getIntent().getIntExtra("falta",0);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        //setContentView(mScannerView);
        context = this;
        toolbar = findViewById(R.id.toolbarUbi);
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
        if (ActivityCompat.checkSelfPermission(QrCameraZxincUbicacionesActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(QrCameraZxincUbicacionesActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
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

    }


    private void mostrarMensaje(String error ){
        // Obtenemos el error recibido desde el backend y mostramos como mensaje
        AlertDialog.Builder builder = new AlertDialog.Builder(QrCameraZxincUbicacionesActivity.this);
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
        // Mostramos el dialogo de espera
        final DialogFullAnimation dialogFullAnimation =   new DialogFullAnimation(context);
        dialogFullAnimation.show();
        if (result.getText() != null && !result.equals("")){
            isScanned = true;
            playSound(context, R.raw.vouchersound);
            QrCameraZxincUbicacionesActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                   Integer codigoUbicacion = Integer.valueOf(result.getText());
                    if (codigoUbicacion != null && codigoUbicacion.equals(ubicaciones.getOid())){
                        QrCameraZxincUbicacionesActivity.this.finish();
                        dialogFullAnimation.dismiss();
                        Intent intent = new Intent(QrCameraZxincUbicacionesActivity.this, VerificarArticuloActivity.class);
                        intent.putExtra("repartoItemOid", repartoItemOid);
                        intent.putExtra("ubicacion", ubicaciones);
                        intent.putExtra("cantReparto", cantReparto);
                        intent.putExtra("falta", falta);
                        if (repartoItem !=null){
                            intent.putExtra("repartoItem", repartoItem);
                        }
                        startActivity(intent);
                    }else {
                        Log.e("onResponse", "El producto o ubicación no coinciden");
                        mostrarMensaje("El producto o ubicación no coinciden");
                        dialogFullAnimation.dismiss();
                    }

                   //prepareRequest();
                }
            });
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(QrCameraZxincUbicacionesActivity.this);
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
