package py.hvillalba.control_deposito_app.ui.recyclerlistpicking;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import py.hvillalba.control_deposito_app.R;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.dto.Vw_stock;
import py.hvillalba.control_deposito_app.ui.qrCamera.QrCameraZxincActivity;
import py.hvillalba.control_deposito_app.util.eventbus.Events;
import py.hvillalba.control_deposito_app.util.eventbus.GlobalBus;

public class RecyclerViewAdapterUbicacion extends RecyclerView.Adapter<CustomViewHolderUbicacion> {

    private Context context;
    private List<Ubicaciones> stockList =new ArrayList<>();
    private Integer repartoItemOid;
    private Double cantReparto;
    private RepartoItem repartoItem;
    private boolean conteo;
    private Integer falta;


    public RecyclerViewAdapterUbicacion(Context context, List<Ubicaciones> list,
                                        Integer repartoItemOid,
                                        Double cantReparto,
                                        RepartoItem repartoItem,
                                        boolean conteo,
                                        Integer falta){
        this.context = context;
        this.stockList = list;
        this.repartoItemOid = repartoItemOid;
        this.cantReparto = cantReparto;
        this.repartoItem = repartoItem;
        this.conteo = conteo;
        this.falta = falta;
    }

    @NonNull
    @Override
    public CustomViewHolderUbicacion onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ubicaciones, viewGroup, false);
        CustomViewHolderUbicacion customViewHolderUbicacion = new CustomViewHolderUbicacion(view);
        return customViewHolderUbicacion;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderUbicacion holder, int i) {
        Ubicaciones vw_stock = stockList.get(holder.getAdapterPosition());
        DecimalFormat df = new DecimalFormat("###,###,###.##");
        holder.tvCantidad.setText(df.format(vw_stock.getExistencia()));
        holder.tvFila.setText(vw_stock.getFila().getDescripcion() == null ? "" : vw_stock.getFila().getDescripcion());
        holder.tvEstante.setText(vw_stock.getEstante() == null ? "" : vw_stock.getEstante().getDescripcion());
        holder.tvCalle.setText(vw_stock.getCalle() == null ? "" : vw_stock.getCalle().getDescripcion());
        holder.tvDeposito.setText(vw_stock.getDeposito() == null ? "" : vw_stock.getDeposito().getDescripcion());
        holder.tvSucursal.setText(vw_stock.getSucursal() == null ? "" : vw_stock.getSucursal().getDescripcion());
        holder.tvCodArticulo.setText(vw_stock.getCodigoArticulo() == null ? "" : vw_stock.getCodigoArticulo());
        holder.tvLote.setText(vw_stock.getLote() == null ? "" : vw_stock.getLote());
        holder.btnEscanearProducto.setOnClickListener(e -> {
            Intent intent = new Intent(context, QrCameraZxincActivity.class);
            intent.putExtra("idArticulo",vw_stock.getCodigoArticulo());
            intent.putExtra("lote", vw_stock.getLote());
            intent.putExtra("vw", vw_stock);
            intent.putExtra("repartoItemOid", repartoItemOid);
            intent.putExtra("cantReparto", cantReparto);
            intent.putExtra("falta", falta);
            intent.putExtra("conteo", conteo);
            if (repartoItem != null)
                intent.putExtra("repartoItem", repartoItem);
            context.startActivity(intent);
            GlobalBus.getBus().post(new Events.CerrarVentana("cerrar"));
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
