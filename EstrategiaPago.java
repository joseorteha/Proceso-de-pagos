// Archivo: EstrategiaPago.java
// Interfaz Strategy: define el contrato comun para todos los metodos de pago.
public interface EstrategiaPago {
    void procesarPago(double monto, String cuenta, String password);
}
