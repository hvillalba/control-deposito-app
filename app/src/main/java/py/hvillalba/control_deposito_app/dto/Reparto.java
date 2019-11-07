package py.hvillalba.control_deposito_app.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reparto extends RealmObject implements Serializable {

    @PrimaryKey
    private Integer oid;
    private Integer registroOrigen;
    private Date fecha;
    private String observacion;
    private Integer sucursalCarga;
    private String depositoCarga;
    private Boolean conteoConfirmado;
    private TransportistaChofer chofer;

    public TransportistaChofer getChofer() {
        return chofer;
    }

    public void setChofer(TransportistaChofer chofer) {
        this.chofer = chofer;
    }

    //Para manejor interno, nos dice si ya se conto al menos uno de sus articulos;
    @Expose(serialize = false)
    private Boolean contado;

    public Boolean getContado() {
        return contado;
    }

    public void setContado(Boolean contado) {
        this.contado = contado;
    }

    private RealmList<RepartoItem> repartoItemList;

    public RealmList<RepartoItem> getRepartoItemList() {
        return repartoItemList;
    }

    public void setRepartoItemList(RealmList<RepartoItem> repartoItemList) {
        this.repartoItemList = repartoItemList;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getRegistroOrigen() {
        return registroOrigen;
    }

    public void setRegistroOrigen(Integer registroOrigen) {
        this.registroOrigen = registroOrigen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getSucursalCarga() {
        return sucursalCarga;
    }

    public void setSucursalCarga(Integer sucursalCarga) {
        this.sucursalCarga = sucursalCarga;
    }

    public String getDepositoCarga() {
        return depositoCarga;
    }

    public void setDepositoCarga(String depositoCarga) {
        this.depositoCarga = depositoCarga;
    }

    public Boolean getConteoConfirmado() {
        return conteoConfirmado;
    }

    public void setConteoConfirmado(Boolean conteoConfirmado) {
        this.conteoConfirmado = conteoConfirmado;
    }
}
