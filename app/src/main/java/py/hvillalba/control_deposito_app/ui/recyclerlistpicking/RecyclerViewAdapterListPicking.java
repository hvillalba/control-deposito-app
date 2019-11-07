package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.ui.ArticuloListActivity;

public class RecyclerViewAdapterListPicking extends RecyclerView.Adapter<CustomViewHolderListPicking> {

    List<Reparto> repartoList = new ArrayList<>();
    Context context;
    private boolean conteo;

    public RecyclerViewAdapterListPicking(List<Reparto> lista, Context context, boolean conteo) {
        this.context = context;
        this.repartoList = lista;
        this.conteo = conteo;
    }

    @NonNull
    @Override
    public CustomViewHolderListPicking onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new CustomViewHolderListPicking(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderListPicking holder, int i) {
        Reparto reparto =  repartoList.get(holder.getAdapterPosition());
        holder.tvOid.setText(String.valueOf(reparto.getOid().toString()));
        holder.tvChofer.setText(reparto.getChofer().getNombreChofer());
        holder.tvRegistroOrigen.setText(reparto.getRegistroOrigen().toString());
        holder.tvOid.setOnClickListener(e -> {
            Intent intent = new Intent(context, ArticuloListActivity.class);
            intent.putExtra("oid",reparto.getOid());
            intent.putExtra("conteo", conteo);
            context.startActivity(intent);
        });
        holder.tvRegistroOrigen.setOnClickListener(e -> {
            Intent intent = new Intent(context, ArticuloListActivity.class);
            intent.putExtra("oid",reparto.getOid());
            intent.putExtra("conteo", conteo);
            context.startActivity(intent);
        });
        holder.tvChofer.setOnClickListener(e -> {
            Intent intent = new Intent(context, ArticuloListActivity.class);
            intent.putExtra("oid",reparto.getOid());
            intent.putExtra("conteo", conteo);
            context.startActivity(intent);
        });
        holder.imageView.setOnClickListener(e -> {
            Intent intent = new Intent(context, ArticuloListActivity.class);
            intent.putExtra("oid",reparto.getOid());
            intent.putExtra("conteo", conteo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return repartoList.size();
    }
}
