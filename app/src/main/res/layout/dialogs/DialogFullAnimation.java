package com.artico.delivery.pedidos.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.artico.delivery.pedidos.R;
import com.artico.delivery.pedidos.data.helpers.eventbus.Events;
import com.artico.delivery.pedidos.data.helpers.eventbus.GlobalBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Gustavo on 9/28/17.
 */

public class DialogFullAnimation extends Dialog {

    public DialogFullAnimation(Context context) {
        super(context, R.style.dialogFull);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_full_animation);
    }

    /**
     * Cerramos el dialogo
     */
    private void dismissDialog(){
        com.artico.delivery.pedidos.ui.dialogs.DialogFullAnimation.this.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModelChange(Events.showAnimationVale event){
        if(event.status == 1){
            // animamos el fadeout
        }else if(event.status == 2) {
            dismissDialog();
        }
    }
}
