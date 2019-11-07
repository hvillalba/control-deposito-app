package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import py.hvillalba.control_deposito_app.R;

public class CustomViewHolderUbicacion  extends RecyclerView.ViewHolder {

    TextView tvCodArticulo;
    TextView tvLote;
    TextView tvSucursal;
    TextView tvDeposito;
    TextView tvCalle;
    TextView tvEstante;
    TextView tvFila;
    TextView tvCantidad;
    Button btnEscanearProducto;

    public CustomViewHolderUbicacion(@NonNull View itemView) {
        super(itemView);
        tvCodArticulo = itemView.findViewById(R.id.tvCodArticulo);
        tvLote = itemView.findViewById(R.id.tvLote);
        tvSucursal = itemView.findViewById(R.id.tvSucursal);
        tvDeposito = itemView.findViewById(R.id.tvDeposito);
        tvCalle = itemView.findViewById(R.id.tvCalle);
        tvEstante = itemView.findViewById(R.id.tvEstante);
        tvFila = itemView.findViewById(R.id.tvFila);
        tvCantidad = itemView.findViewById(R.id.tvCantidad);
        btnEscanearProducto = itemView.findViewById(R.id.btnEscanearProducto);
    }
}
