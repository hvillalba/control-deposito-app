package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.artico.delivery.pedidos.R;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogFavorite extends Dialog {

    public DialogFavorite(Context context) {
        super(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_favorite);

        RelativeLayout view = findViewById(R.id.custom_toast_container);
        RelativeLayout rlAnimate = findViewById(R.id.rlContentAnimation);

        // Make the object width 50%
        Animation anim = new ScaleAnimation(
                0.3f, 1f, // Start and end values for the X axis scaling
                0.3f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(700);
        anim.setInterpolator(new AnticipateOvershootInterpolator());
        rlAnimate.startAnimation(anim);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
