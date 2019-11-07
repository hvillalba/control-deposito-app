package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fila extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer fila;
    private String descripcion;
    @PrimaryKey
    private String codigo;
    private RealmList<Ubicaciones> ubicacionesList;

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
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
