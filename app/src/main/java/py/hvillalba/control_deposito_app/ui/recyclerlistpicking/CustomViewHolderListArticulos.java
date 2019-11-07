package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import py.hvillalba.control_deposito_app.R;

public class CustomViewHolderListArticulos extends RecyclerView.ViewHolder {

    TextView tvCodigo;
    TextView tvDescripcion;
    TextView tvLote;
    TextView tvCantidad;
    TextView tvNroFactura;
    TextView tvRazonSocial;
    ImageView imageView;
    TextView tvCantidadIngresada;
    TextView tvCantidadFaltante;
    TextView textViewCantidad;
    TextView textViewCantidadIngresada;
    TextView textViewCantidadFaltante;
    TextView tvConfirmado;
    TextView textViewConfirmado;
    LinearLayout linear;

    public CustomViewHolderListArticulos(@NonNull View itemView) {
        super(itemView);
        tvCodigo = itemView.findViewById(R.id.tvCodigo);
        tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        tvLote = itemView.findViewById(R.id.tvLote);
        tvCantidad = itemView.findViewById(R.id.tvCantidad);
        imageView = itemView.findViewById(R.id.imageViewArticulo);
        tvRazonSocial = itemView.findViewById(R.id.tvRazonSocial);
        tvNroFactura = itemView.findViewById(R.id.tvNroFactura);
        tvCantidadIngresada = itemView.findViewById(R.id.tvCantidadIngresada);
        tvCantidadFaltante = itemView.findViewById(R.id.tvCantidadFaltante);
        textViewCantidad = itemView.findViewById(R.id.textViewCantidad);
        textViewCantidadIngresada = itemView.findViewById(R.id.textViewCantidadIngresada);
        textViewCantidadFaltante = itemView.findViewById(R.id.textViewCantidadFaltante);
        tvConfirmado = itemView.findViewById(R.id.tvConfirmado);
        linear = itemView.findViewById(R.id.linear);
        textViewConfirmado = itemView.findViewById(R.id.textViewConfirmado);
    }
}
