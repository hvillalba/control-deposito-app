package py.hvillalba.control_deposito_app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomPreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public CustomPreferenceManager(Context context){
        this.sharedPreferences  = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    // Set string
    public void setValue(String prefsKey, String prefsValue) {
        editor.putString(prefsKey, prefsValue);
        editor.apply();
    }

    // Set int
    public void setValue(String prefsKey, int prefsValue) {
        editor.putInt(prefsKey, prefsValue);
        editor.apply();
    }

    // Set boolean
    public void setValue(String prefsKey, boolean prefsValue) {
        editor.putBoolean(prefsKey, prefsValue);
        editor.apply();
    }

    // Get String
    public String getString(String prefsKey){
        String value = sharedPreferences.getString(prefsKey, "");
        return value;
    }

    // Get int
    public int getInt(String prefsKey){
        int value = sharedPreferences.getInt(prefsKey, 0);
        return value;
    }

    // Get boolean
    public Boolean getBoolean(String prefsKey){
        Boolean value = sharedPreferences.getBoolean(prefsKey, false);
        return value;
    }

    public Boolean getBooleanTrue(String prefsKey){
        Boolean value = sharedPreferences.getBoolean(prefsKey, true);
        return value;
    }
}