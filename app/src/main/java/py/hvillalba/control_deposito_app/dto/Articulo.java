package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Articulo extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Integer oid;
    private String codigo;
    private String descripcion;
    private RealmList<ArticuloLote> articuloLoteList;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripción(String descripción) {
        this.descripcion = descripción;
    }

    public RealmList<ArticuloLote> getArticuloLoteList() {
        return articuloLoteList;
    }

    public void setArticuloLoteList(RealmList<ArticuloLote> articuloLoteList) {
        this.articuloLoteList = articuloLoteList;
    }
}
