package py.hvillalba.control_deposito_app.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.util.CustomPreferenceManager;

public class OptionActivity extends AppCompatActivity {

    EditText edIp;
    Button btnGuardad;

    CustomPreferenceManager customPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        edIp = findViewById(R.id.ip);
        customPreferenceManager = new CustomPreferenceManager(this);
        btnGuardad = findViewById(R.id.btnGuardar);
        btnGuardad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPreferenceManager.setValue("ip", edIp.getText().toString());
                OptionActivity.this.finish();
            }
        });
    }
}
