package py.hvillalba.control_deposito_app.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.py_hvillalba_control_deposito_app_dto_RepartoItemRealmProxy;


public class RepartoItem  extends RealmObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @PrimaryKey
    private Integer oid;
    private Integer registroFactura;
    private String factura;
    private Float cantidad;
    private Boolean confirmado;
    @Expose(serialize = false)
    private boolean contado;
    //private RealmList<RepartoPicking> repartoPickingList;
    private ArticuloLote articuloLote;
    private Reparto reparto;
    private Integer cantidadIngresada;
    private String razonSocial;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getCantidadIngresada() {
        return cantidadIngresada;
    }

    public void setCantidadIngresada(Integer cantidadIngresada) {
        this.cantidadIngresada = cantidadIngresada;
    }

    public boolean isContado() {
        return contado;
    }

    public void setContado(boolean contado) {
        this.contado = contado;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getRegistroFactura() {
        return registroFactura;
    }

    public void setRegistroFactura(Integer registroFactura) {
        this.registroFactura = registroFactura;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }


    public ArticuloLote getArticuloLote() {
        return articuloLote;
    }

    public void setArticuloLote(ArticuloLote articuloLote) {
        this.articuloLote = articuloLote;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

}
