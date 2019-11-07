package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ArticuloLote  extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Integer oid;
    private String lote;
    private Date fechaVencimiento;
    private RealmList<RepartoItem> repartoItemList;
    private Articulo articulo;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public RealmList<RepartoItem> getRepartoItemList() {
        return repartoItemList;
    }

    public void setRepartoItemList(RealmList<RepartoItem> repartoItemList) {
        this.repartoItemList = repartoItemList;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
