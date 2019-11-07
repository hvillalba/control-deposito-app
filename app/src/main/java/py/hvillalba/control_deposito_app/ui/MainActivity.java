package py.hvillalba.control_deposito_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

import io.realm.Realm;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;

public class MainActivity extends AppCompatActivity {


    private Button btnConfirmarConteo;
    private Button btnRealizarConteo;
    CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customPreferenceManager = new CustomPreferenceManager(this);
        btnConfirmarConteo = findViewById(R.id.btnConfirmarConteo);
        btnRealizarConteo = findViewById(R.id.btnRealizarConteo);
        // Verificamos si es la primera carga
        btnRealizarConteo.setOnClickListener(e ->{
            Intent intent = new Intent(MainActivity.this, ListaPickingActivity.class);
            intent.putExtra("conteo", false);
            startActivity(intent);
        });
        btnConfirmarConteo.setOnClickListener(e ->{
            Intent intent = new Intent(MainActivity.this, ListaPickingActivity.class);
            intent.putExtra("conteo",true);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrarSesion:
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
