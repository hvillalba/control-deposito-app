package py.hvillalba.control_deposito_app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ControlDepositoApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // BD Local
        MultiDex.install(this);
        Realm.init(getApplicationContext());

        // Configuramos Realm para la aplicación
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("control-deposito.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();

        // Establecemos esta BD de Realm como backup
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.compactRealm(realmConfiguration);
        Log.e("py.hvillalba.control_deposito_app.ControlDepositoApp", "Seteando configuration Realm");
    }
}
