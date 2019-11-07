package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sucursal extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sucursal;
    private String descripcion;
    @PrimaryKey
    private Integer codigo;
    private RealmList<Ubicaciones> ubicacionesList;

    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal(Integer sucursal) {
        this.sucursal = sucursal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public RealmList<Ubicaciones> getUbicacionesList() {
        return ubicacionesList;
    }

    public void setUbicacionesList(RealmList<Ubicaciones> ubicacionesList) {
        this.ubicacionesList = ubicacionesList;
    }
}
