// Archivo: PagoTransferenciaBancaria.java
// Estrategia concreta: Transferencia Bancaria
public class PagoTransferenciaBancaria implements EstrategiaPago {

    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Generando referencia SPEI...");
        System.out.println("Esperando confirmacion de transferencia por $" + monto);
        // Logica de validacion interbancaria...
    }
}
