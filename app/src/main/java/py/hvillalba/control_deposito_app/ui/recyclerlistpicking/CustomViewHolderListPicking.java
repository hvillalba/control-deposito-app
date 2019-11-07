package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import py.hvillalba.control_deposito_app.R;


public class CustomViewHolderListPicking extends RecyclerView.ViewHolder {

    TextView tvOid;
    TextView tvRegistroOrigen;
    TextView tvChofer;
    ImageView imageView;

    public CustomViewHolderListPicking(@NonNull View itemView) {
        super(itemView);
        tvOid = itemView.findViewById(R.id.tvOid);
        tvRegistroOrigen = itemView.findViewById(R.id.tvRegistroOrigen);
        tvChofer = itemView.findViewById(R.id.tvChofer);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
