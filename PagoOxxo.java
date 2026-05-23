// Archivo: PagoOxxo.java
// Estrategia concreta: Pago en OXXO
// Demuestra lo facil que es agregar un nuevo metodo de pago
// sin modificar ninguna clase existente (Principio Abierto/Cerrado).
public class PagoOxxo implements EstrategiaPago {

    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Generando codigo de barras para pago en OXXO...");
        System.out.println("Monto a pagar en ventanilla: $" + monto);
        System.out.println("Referencia de pago: OXXO-" + System.currentTimeMillis());
        System.out.println("El cliente tiene 24 horas para realizar el pago.");
        // Logica de generacion de referencia OXXO...
    }
}
