// Archivo: PagoPayPal.java
// Estrategia concreta: PayPal
public class PagoPayPal implements EstrategiaPago {

    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Autenticando cuenta de PayPal: " + cuenta);
        System.out.println("Procesando pago de $" + monto + " a traves de PayPal.");
        // Logica compleja de API de PayPal...
    }
}
