package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.artico.delivery.pedidos.R;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogNoInternet extends Dialog {

    public DialogNoInternet(Context context) {
        super(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_no_internet);

        RelativeLayout view = findViewById(R.id.custom_toast_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
