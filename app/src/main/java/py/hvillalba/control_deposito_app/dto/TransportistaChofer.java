package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;

public class TransportistaChofer extends RealmObject implements Serializable {

    private Integer idChofer;
    private String nombreChofer;
    private String documento;
    private Transportista transportista;
    private Long codigo;

    public Integer getIdChofer() {
        return idChofer;
    }

    public void setIdChofer(Integer idChofer) {
        this.idChofer = idChofer;
    }

    public String getNombreChofer() {
        return nombreChofer;
    }

    public void setNombreChofer(String nombreChofer) {
        this.nombreChofer = nombreChofer;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }


    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
}
