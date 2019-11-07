package py.hvillalba.control_deposito_app.dto;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Vw_stock extends RealmObject implements Serializable {

    @PrimaryKey
    private String codigoArticulo;
    private String lote;
    private Integer codigoSucursal;
    private String codigoDeposito;
    private String codigoCalle;
    private String codigoEstante;
    private String codigoFila;
    private Double existencia;

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(Integer codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public String getCodigoDeposito() {
        return codigoDeposito;
    }

    public void setCodigoDeposito(String codigoDeposito) {
        this.codigoDeposito = codigoDeposito;
    }

    public String getCodigoCalle() {
        return codigoCalle;
    }

    public void setCodigoCalle(String codigoCalle) {
        this.codigoCalle = codigoCalle;
    }

    public String getCodigoEstante() {
        return codigoEstante;
    }

    public void setCodigoEstante(String codigoEstante) {
        this.codigoEstante = codigoEstante;
    }

    public String getCodigoFila() {
        return codigoFila;
    }

    public void setCodigoFila(String codigoFila) {
        this.codigoFila = codigoFila;
    }

    public Double getExistencia() {
        return existencia;
    }

    public void setExistencia(Double existencia) {
        this.existencia = existencia;
    }
}