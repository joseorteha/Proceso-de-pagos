// Archivo: PagoCriptomoneda.java
// Estrategia concreta: Criptomoneda
public class PagoCriptomoneda implements EstrategiaPago {

    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Verificando la red blockchain...");
        System.out.println("Procesando pago equivalente a $" + monto + " en Bitcoin.");
        // Logica compleja de billetera cripto...
    }
}
