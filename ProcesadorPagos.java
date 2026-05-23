// Archivo: ProcesadorPagos.java
// Clase Contexto del patron Strategy.
// Ya no contiene logica condicional (if-else).
// Recibe la estrategia por inyeccion de dependencias y delega el procesamiento.
public class ProcesadorPagos {

    private EstrategiaPago estrategia;

    // Inyeccion de dependencias por constructor
    public ProcesadorPagos(EstrategiaPago estrategia) {
        this.estrategia = estrategia;
    }

    // Permite cambiar la estrategia en tiempo de ejecucion
    public void setEstrategia(EstrategiaPago estrategia) {
        this.estrategia = estrategia;
    }

    // Delega el procesamiento a la estrategia inyectada
    public void procesarPago(double monto, String cuenta, String password) {
        estrategia.procesarPago(monto, cuenta, password);
    }

    public static void main(String[] args) {
        System.out.println("=== PATRON STRATEGY: Sistema de Procesamiento de Pagos ===\n");

        // 1. Pago con Tarjeta de Credito
        System.out.println("--- Pago 1: Tarjeta de Credito ---");
        ProcesadorPagos procesador = new ProcesadorPagos(new PagoTarjetaCredito());
        procesador.procesarPago(1500.50, null, null);

        // 2. Pago con PayPal (cambiamos la estrategia en tiempo de ejecucion)
        System.out.println("\n--- Pago 2: PayPal ---");
        procesador.setEstrategia(new PagoPayPal());
        procesador.procesarPago(850.00, "alumno@itsz.edu.mx", "12345");

        // 3. Pago con Criptomoneda
        System.out.println("\n--- Pago 3: Criptomoneda ---");
        procesador.setEstrategia(new PagoCriptomoneda());
        procesador.procesarPago(5000.00, "wallet_abc123", null);

        // 4. Pago con Transferencia Bancaria
        System.out.println("\n--- Pago 4: Transferencia Bancaria ---");
        procesador.setEstrategia(new PagoTransferenciaBancaria());
        procesador.procesarPago(3200.00, "CLABE_012345678", null);

        // 5. Nuevo metodo de pago: OXXO (sin modificar ninguna clase existente)
        System.out.println("\n--- Pago 5: OXXO (nuevo metodo agregado) ---");
        procesador.setEstrategia(new PagoOxxo());
        procesador.procesarPago(499.99, null, null);

        System.out.println("\n=== Todos los pagos procesados exitosamente ===");
    }
}
