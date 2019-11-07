package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;

import io.realm.RealmObject;

public class Transportista  extends RealmObject implements Serializable {

    private Integer transportista;
    private String razonSocial;
    private String ruc;
    private Integer codigo;

    public Integer getTransportista() {
        return transportista;
    }

    public void setTransportista(Integer transportista) {
        this.transportista = transportista;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}