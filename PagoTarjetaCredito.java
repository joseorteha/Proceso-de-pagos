// Archivo: PagoTarjetaCredito.java
// Estrategia concreta: Tarjeta de Credito
public class PagoTarjetaCredito implements EstrategiaPago {

    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Validando limite de credito...");
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Credito.");
        // Logica compleja de conexion con el banco...
    }
}
