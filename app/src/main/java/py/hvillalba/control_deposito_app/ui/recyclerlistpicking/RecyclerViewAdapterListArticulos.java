package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.internal.ReflectedParcelable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Articulo;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.ui.UbicacionActivity;

public class RecyclerViewAdapterListArticulos extends RecyclerView.Adapter<CustomViewHolderListArticulos> {
    private List<RepartoItem>  articuloList = new ArrayList<>();
    private transient Context context;
    private boolean conteo;

    public RecyclerViewAdapterListArticulos(Context context, List<RepartoItem> list, boolean conteo){
        this.context = context;
        this.articuloList = list;
        this.conteo = conteo;
    }

    @NonNull
    @Override
    public CustomViewHolderListArticulos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_1, viewGroup,false);
        CustomViewHolderListArticulos customViewHolderListArticulos = new CustomViewHolderListArticulos(view);
        return customViewHolderListArticulos;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderListArticulos holder, int i) {
        RepartoItem repartoItem = articuloList.get(holder.getAdapterPosition());
        holder.tvCodigo.setText( repartoItem.getArticuloLote().getArticulo().getCodigo().toString());
        holder.tvDescripcion.setText( repartoItem.getArticuloLote().getArticulo().getDescripcion());
        holder.tvLote.setText(repartoItem.getArticuloLote().getLote());
        holder.tvCantidad.setText( repartoItem.getCantidad().toString());
        holder.tvNroFactura.setText(repartoItem.getFactura());
        holder.tvRazonSocial.setText(repartoItem.getRazonSocial());
        holder.tvCantidadIngresada.setText(repartoItem.getCantidadIngresada() == null ? "0" : repartoItem.getCantidadIngresada().toString());
        Integer cant =  repartoItem.getCantidad().intValue() - (repartoItem.getCantidadIngresada() == null ? 0 : repartoItem.getCantidadIngresada());
        holder.tvCantidadFaltante.setText(cant.toString());
        if (repartoItem.getConfirmado()!= null){
            if (repartoItem.getConfirmado()){
                holder.tvConfirmado.setText("Si");
                holder.tvConfirmado.setTextColor(context.getResources().getColor(R.color.greenMonchisDefault));
                holder.linear.setEnabled(false);
                holder.tvCodigo.setEnabled(false);
                holder.tvDescripcion.setEnabled(false);
                holder.tvLote.setEnabled(false);
                holder.tvCantidad.setEnabled(false);
                holder.tvNroFactura.setEnabled(false);
                holder.tvRazonSocial.setEnabled(false);
                holder.tvCantidadIngresada.setEnabled(false);
                holder.imageView.setEnabled(false);
            }else {
                holder.tvConfirmado.setText("N");
                holder.tvConfirmado.setTextColor(context.getResources().getColor(R.color.redMonchis1));
            }
        }else {
            holder.tvConfirmado.setText("N");
            holder.tvConfirmado.setTextColor(context.getResources().getColor(R.color.redMonchis1));
        }

        if (conteo){
            holder.tvCantidadFaltante.setVisibility(View.GONE);
            holder.tvCantidad.setVisibility(View.GONE);
            holder.tvCantidadIngresada.setVisibility(View.GONE);
            holder.textViewCantidad.setVisibility(View.GONE);
            holder.textViewCantidadFaltante.setVisibility(View.GONE);
            holder.textViewCantidadIngresada.setVisibility(View.GONE);
            holder.tvConfirmado.setVisibility(View.VISIBLE);
            holder.textViewConfirmado.setVisibility(View.VISIBLE);
        }
        holder.tvCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItemOid", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                context.startActivity(intent);
            }
        });
        holder.tvDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItem", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                context.startActivity(intent);
            }
        });
        holder.tvLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItem", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                context.startActivity(intent);
            }
        });
        holder.tvCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItem", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                context.startActivity(intent);
            }
        });
        holder.tvCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItem", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                context.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbicacionActivity.class);
                intent.putExtra("idArticulo", repartoItem.getArticuloLote().getArticulo().getCodigo());
                intent.putExtra("lote", repartoItem.getArticuloLote().getLote());
                intent.putExtra("repartoItemOid", repartoItem.getOid());
                intent.putExtra("cantReparto", repartoItem.getCantidad().doubleValue());
                intent.putExtra("falta", cant);
                intent.putExtra("conteo", conteo);
                if (!repartoItem.isContado()){
                    intent.putExtra("repartoItem", repartoItem);
                }
                 context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articuloList.size();
    }
}
