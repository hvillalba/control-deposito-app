package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Calle extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer calle;
    private String descripcion;
    @PrimaryKey
    private String codigo;
    private RealmList<Ubicaciones> ubicacionesList;

    public Integer getCalle() {
        return calle;
    }

    public void setCalle(Integer calle) {
        this.calle = calle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public RealmList<Ubicaciones> getUbicacionesList() {
        return ubicacionesList;
    }

    public void setUbicacionesList(RealmList<Ubicaciones> ubicacionesList) {
        this.ubicacionesList = ubicacionesList;
    }
}
