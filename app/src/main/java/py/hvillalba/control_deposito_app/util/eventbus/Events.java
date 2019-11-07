package py.hvillalba.control_deposito_app.util.eventbus;



/**
 * Created by Gustavo on 11/8/17.
 */

public class Events {


    /****************************************
     * EVENTOS DEL Cerrar Ventana
     ****************************************/
    public static class CerrarVentana{
        public String message;
        public CerrarVentana(String message){
            this.message = message;
        }
    }

    /****************************************
     * EVENTOS DEL CARRITO
     ****************************************/
    public static class ActualizarPendientes{

        public ActualizarPendientes(){

        }
    }

    public static class CheckAddons{
        public boolean check;
        public CheckAddons(boolean check){
            this.check = check;
        }
    }

    /****************************************
     * EVENTOS DEL ACTUALIZAR ORDENES
     ****************************************/
    public static class MQTTEventsConectado{
        public boolean conectado;
        public MQTTEventsConectado(boolean conectado){
            this.conectado = conectado;
        }
    }

    /****************************************
     * EVENTOS DEL ACTUALIZAR ORDENES
     ****************************************/
    public static class MQTTPublish{
        public String type;
        public String message;
        public MQTTPublish(String message, String type){
            this.message = message;
            this.type = type;
        }
    }

    /****************************************
     * EVENTOS DEL ACTUALIZAR ORDENES
     ****************************************/
    public static class AddHistory{
        public String message;
        public AddHistory(String message){
            this.message = message;
        }
    }

    /****************************************
     * EVENTOS DE LAS ANIMACIONES
     ****************************************/
    public static class showAnimationVale {
        public int status;// 1 = animacion de fade 2 = cerrar el dialog
        public showAnimationVale(int status) {
            this.status = status;
        }
    }
    public static class ShowCheckAnimation {
        public ShowCheckAnimation() {
        }
    }


    public static class TomarOrden {
        public boolean retorno;
        public long orderId;
        public TomarOrden(boolean retorno, long orderId){
            this.orderId = orderId;
            this.retorno = retorno;
        }
    }

    public static class FinalizarOrden{
        public boolean success;
        public String message;

        public FinalizarOrden(boolean success, String message){
            this.success = success;
            this.message = message;
        }
    }

}
