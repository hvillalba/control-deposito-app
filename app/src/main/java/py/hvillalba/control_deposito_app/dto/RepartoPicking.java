package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RepartoPicking extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Integer oid;
    private Integer cantidadNecesaria;
    private Integer cantidadIngresada;
    private Integer cantidadContada;
    private RepartoItem repartoItem;
    private Ubicaciones ubicaciones;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getCantidadNecesaria() {
        return cantidadNecesaria;
    }

    public void setCantidadNecesaria(Integer cantidadNecesaria) {
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public Integer getCantidadIngresada() {
        return cantidadIngresada;
    }

    public void setCantidadIngresada(Integer cantidadIngresada) {
        this.cantidadIngresada = cantidadIngresada;
    }

    public Integer getCantidadContada() {
        return cantidadContada;
    }

    public void setCantidadContada(Integer cantidadContada) {
        this.cantidadContada = cantidadContada;
    }

    public RepartoItem getRepartoItem() {
        return repartoItem;
    }

    public void setRepartoItem(RepartoItem repartoItem) {
        this.repartoItem = repartoItem;
    }

    public Ubicaciones getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(Ubicaciones ubicaciones) {
        this.ubicaciones = ubicaciones;
    }
}
