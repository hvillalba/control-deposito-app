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

public class DialogCheckAnimate extends Dialog {

    public DialogCheckAnimate(Context context) {
        super(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_check);

        RelativeLayout view = findViewById(R.id.container);
        RelativeLayout rlAnimate = findViewById(R.id.rlContentAnimation);

        // Make the object width 50%
//        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_to_middle);
//        anim.setInterpolator((new AccelerateDecelerateInterpolator()));
//        anim.setFillAfter(true);
//        rlAnimate.setAnimation(anim);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
