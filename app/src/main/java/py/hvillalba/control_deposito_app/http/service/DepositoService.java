package py.hvillalba.control_deposito_app.http.service;

import java.util.List;

import py.hvillalba.control_deposito_app.dto.Articulo;
import py.hvillalba.control_deposito_app.dto.Reparto;
import py.hvillalba.control_deposito_app.dto.RepartoItem;
import py.hvillalba.control_deposito_app.dto.RepartoPicking;
import py.hvillalba.control_deposito_app.dto.Response;
import py.hvillalba.control_deposito_app.dto.Ubicaciones;
import py.hvillalba.control_deposito_app.dto.Vw_stock;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DepositoService {


    @GET("deposito/list-reparto")
    Call<Response<List<Reparto>>> getListReparto();

    @GET("deposito/list-repartoitem-articulos/{id}")
    Call<Response<List<RepartoItem>>> getListRepartoItemArticulos(@Path("id") Integer id);

    @GET("deposito/list-repartoitem-articulos-all/{id}")
    Call<Response<List<RepartoItem>>> getListRepartoItemArticulosAll(@Path("id") Integer id);

    @GET("deposito/get-ubicacion/{idArticulo}/{lote}")
    Call<Response<List<Ubicaciones>>> getUbicacion(@Path("idArticulo") String idArticulo, @Path("lote") String lote);

    @POST("deposito/update-reparto-picking/{ubicacion}/{repartoItem}/{cantidad}")
    Call<Response<RepartoPicking>> updateRepartoPicking(@Path("ubicacion") Integer ubicacion,
                                                        @Path("repartoItem") Integer repartoItem,
                                                        @Path("cantidad") Double cantidad );

    @POST("deposito/update-reparto-item/{oid}")
    Call<Response<RepartoItem>> updateRepartoPicking(@Path("oid") Integer repartoItem );

    @POST("deposito/update-reparto/{oid}")
    Call<Response<Reparto>> updateReparto(@Path("oid") Integer repartoOid);


}
