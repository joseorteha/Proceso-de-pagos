# Reporte: Implementacion del Patron de Diseno Strategy

## Sistema de Procesamiento de Pagos

**Materia:** Desarrollo de Software  
**Tema:** Patron de Diseno de Comportamiento - Strategy  
**Fecha:** 23 de mayo de 2026

---

## 1. Analisis del Codigo Original

El codigo original de la clase `ProcesadorPagos` contenia un unico metodo `procesarPago()` con una cadena de `if-else` para determinar que tipo de pago procesar:

```java
public void procesarPago(String metodoPago, double monto, String cuenta, String password) {
    if (metodoPago.equalsIgnoreCase("TarjetaCredito")) {
        // logica tarjeta...
    } else if (metodoPago.equalsIgnoreCase("PayPal")) {
        // logica PayPal...
    } else if (metodoPago.equalsIgnoreCase("Criptomoneda")) {
        // logica cripto...
    } else if (metodoPago.equalsIgnoreCase("TransferenciaBancaria")) {
        // logica transferencia...
    } else {
        System.out.println("Metodo de pago no soportado.");
    }
}
```

### Problemas identificados

| Problema | Descripcion |
|----------|-------------|
| **Viola el Principio Abierto/Cerrado (OCP)** | Para agregar un nuevo metodo de pago (ej. OXXO) es necesario **modificar** la clase `ProcesadorPagos` agregando otro `else if`. La clase deberia estar abierta a extension pero cerrada a modificacion. |
| **Alta complejidad ciclomatica** | La cadena de `if-else` crece linealmente con cada metodo de pago, haciendo el codigo dificil de leer y mantener. |
| **Bajo nivel de cohesion** | Una sola clase concentra la logica de todos los metodos de pago, cuando cada uno tiene responsabilidades distintas. |
| **Dificultad para probar** | No se puede probar un metodo de pago de forma aislada sin cargar toda la clase. |
| **Viola el Principio de Responsabilidad Unica (SRP)** | `ProcesadorPagos` tiene multiples razones para cambiar: cualquier modificacion en la logica de cualquier metodo de pago obliga a tocar esta clase. |

---

## 2. Solucion: Patron Strategy

El patron Strategy permite definir una familia de algoritmos (metodos de pago), encapsular cada uno en su propia clase, y hacerlos intercambiables en tiempo de ejecucion.

### Diagrama de clases

```
+------------------------------+
|      <<interface>>           |
|      EstrategiaPago          |
+------------------------------+
| + procesarPago(double monto, |
|   String cuenta,             |
|   String password): void     |
+------------------------------+
        ^           ^
        |           |
   +----|-----------|----+--------------------+--------------------+
   |    |           |    |                    |                    |
+--+----------+ +---+---------+ +-------------+--+ +--------------+-+ +----------+
| PagoTarjeta | | PagoPayPal  | | PagoCripto     | | PagoTransf     | | PagoOxxo |
| Credito     | |             | | moneda         | | Bancaria       | |          |
+-------------+ +-------------+ +----------------+ +----------------+ +----------+
| +procesarPago | +procesarPago | +procesarPago    | +procesarPago    | +procesarPago
+-------------+ +-------------+ +----------------+ +----------------+ +----------+

+----------------------------+         usa         +------------------+
|    ProcesadorPagos         |------------------->>| EstrategiaPago   |
|       (Contexto)           |                     |   (interfaz)     |
+----------------------------+                     +------------------+
| - estrategia: EstrategiaPago |
| + setEstrategia()            |
| + procesarPago()             |
+------------------------------+
```

---

## 3. Implementacion

### 3.1 Interfaz `EstrategiaPago`

Define el contrato comun que deben cumplir todos los metodos de pago.

```java
public interface EstrategiaPago {
    void procesarPago(double monto, String cuenta, String password);
}
```

### 3.2 Estrategias Concretas

Cada metodo de pago se encapsula en su propia clase, implementando la interfaz.

**PagoTarjetaCredito.java**
```java
public class PagoTarjetaCredito implements EstrategiaPago {
    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Validando limite de credito...");
        System.out.println("Procesando pago de $" + monto + " con Tarjeta de Credito.");
    }
}
```

**PagoPayPal.java**
```java
public class PagoPayPal implements EstrategiaPago {
    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Autenticando cuenta de PayPal: " + cuenta);
        System.out.println("Procesando pago de $" + monto + " a traves de PayPal.");
    }
}
```

**PagoCriptomoneda.java**
```java
public class PagoCriptomoneda implements EstrategiaPago {
    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Verificando la red blockchain...");
        System.out.println("Procesando pago equivalente a $" + monto + " en Bitcoin.");
    }
}
```

**PagoTransferenciaBancaria.java**
```java
public class PagoTransferenciaBancaria implements EstrategiaPago {
    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Generando referencia SPEI...");
        System.out.println("Esperando confirmacion de transferencia por $" + monto);
    }
}
```

### 3.3 Nueva Estrategia: `PagoOxxo`

Demuestra la escalabilidad del patron. Se agrego **sin modificar ninguna clase existente**.

```java
public class PagoOxxo implements EstrategiaPago {
    @Override
    public void procesarPago(double monto, String cuenta, String password) {
        System.out.println("Generando codigo de barras para pago en OXXO...");
        System.out.println("Monto a pagar en ventanilla: $" + monto);
        System.out.println("Referencia de pago: OXXO-" + System.currentTimeMillis());
        System.out.println("El cliente tiene 24 horas para realizar el pago.");
    }
}
```

### 3.4 Clase Contexto: `ProcesadorPagos` (Refactorizada)

Recibe la estrategia por inyeccion de dependencias. Ya no contiene ningun `if-else`.

```java
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
}
```

---

## 4. Prueba de Escritorio y Ejecucion

### Metodo `main`

```java
public static void main(String[] args) {
    // 1. Pago con Tarjeta de Credito
    ProcesadorPagos procesador = new ProcesadorPagos(new PagoTarjetaCredito());
    procesador.procesarPago(1500.50, null, null);

    // 2. Cambiar estrategia a PayPal en tiempo de ejecucion
    procesador.setEstrategia(new PagoPayPal());
    procesador.procesarPago(850.00, "alumno@itsz.edu.mx", "12345");

    // 3. Criptomoneda
    procesador.setEstrategia(new PagoCriptomoneda());
    procesador.procesarPago(5000.00, "wallet_abc123", null);

    // 4. Transferencia Bancaria
    procesador.setEstrategia(new PagoTransferenciaBancaria());
    procesador.procesarPago(3200.00, "CLABE_012345678", null);

    // 5. OXXO (nuevo metodo, sin modificar clases existentes)
    procesador.setEstrategia(new PagoOxxo());
    procesador.procesarPago(499.99, null, null);
}
```

### Salida en consola

```
=== PATRON STRATEGY: Sistema de Procesamiento de Pagos ===

--- Pago 1: Tarjeta de Credito ---
Validando limite de credito...
Procesando pago de $1500.5 con Tarjeta de Credito.

--- Pago 2: PayPal ---
Autenticando cuenta de PayPal: alumno@itsz.edu.mx
Procesando pago de $850.0 a traves de PayPal.

--- Pago 3: Criptomoneda ---
Verificando la red blockchain...
Procesando pago equivalente a $5000.0 en Bitcoin.

--- Pago 4: Transferencia Bancaria ---
Generando referencia SPEI...
Esperando confirmacion de transferencia por $3200.0

--- Pago 5: OXXO (nuevo metodo agregado) ---
Generando codigo de barras para pago en OXXO...
Monto a pagar en ventanilla: $499.99
Referencia de pago: OXXO-1779518949116
El cliente tiene 24 horas para realizar el pago.

=== Todos los pagos procesados exitosamente ===
```

---

## 5. Prueba de Escritorio (Traza manual)

| Paso | Accion | Objeto en memoria | Metodo ejecutado |
|------|--------|-------------------|------------------|
| 1 | `new ProcesadorPagos(new PagoTarjetaCredito())` | `procesador.estrategia = PagoTarjetaCredito` | Constructor |
| 2 | `procesador.procesarPago(1500.50, null, null)` | Delega a `PagoTarjetaCredito` | `PagoTarjetaCredito.procesarPago()` |
| 3 | `procesador.setEstrategia(new PagoPayPal())` | `procesador.estrategia = PagoPayPal` | Setter |
| 4 | `procesador.procesarPago(850.00, "alumno@...", "12345")` | Delega a `PagoPayPal` | `PagoPayPal.procesarPago()` |
| 5 | `procesador.setEstrategia(new PagoCriptomoneda())` | `procesador.estrategia = PagoCriptomoneda` | Setter |
| 6 | `procesador.procesarPago(5000.00, "wallet_abc123", null)` | Delega a `PagoCriptomoneda` | `PagoCriptomoneda.procesarPago()` |
| 7 | `procesador.setEstrategia(new PagoTransferenciaBancaria())` | `procesador.estrategia = PagoTransferenciaBancaria` | Setter |
| 8 | `procesador.procesarPago(3200.00, "CLABE_012345678", null)` | Delega a `PagoTransferenciaBancaria` | `PagoTransferenciaBancaria.procesarPago()` |
| 9 | `procesador.setEstrategia(new PagoOxxo())` | `procesador.estrategia = PagoOxxo` | Setter |
| 10 | `procesador.procesarPago(499.99, null, null)` | Delega a `PagoOxxo` | `PagoOxxo.procesarPago()` |

---

## 6. Comparativa: Antes vs Despues

| Aspecto | Antes (if-else) | Despues (Strategy) |
|---------|------------------|--------------------|
| Agregar metodo de pago | Modificar `ProcesadorPagos` | Crear nueva clase, sin tocar nada existente |
| Principio OCP | Violado | Cumplido |
| Principio SRP | Violado | Cumplido |
| Complejidad de `ProcesadorPagos` | Crece con cada metodo | Se mantiene constante |
| Testabilidad | Dificil (todo acoplado) | Facil (cada estrategia se prueba aislada) |
| Reutilizacion | Ninguna | Las estrategias se pueden reutilizar en otros contextos |

---

## 7. Archivos del Proyecto

| Archivo | Rol en el patron |
|---------|------------------|
| `EstrategiaPago.java` | Interfaz Strategy |
| `PagoTarjetaCredito.java` | Estrategia concreta |
| `PagoPayPal.java` | Estrategia concreta |
| `PagoCriptomoneda.java` | Estrategia concreta |
| `PagoTransferenciaBancaria.java` | Estrategia concreta |
| `PagoOxxo.java` | Estrategia concreta (nueva, demuestra escalabilidad) |
| `ProcesadorPagos.java` | Contexto + metodo main |

---

## 8. Conclusion

El patron Strategy elimina las estructuras condicionales `if-else` al encapsular cada algoritmo (metodo de pago) en su propia clase. El contexto (`ProcesadorPagos`) solo conoce la interfaz, no las implementaciones concretas, lo que permite:

- **Agregar nuevos metodos de pago** sin modificar codigo existente (se demostro con `PagoOxxo`).
- **Cambiar el comportamiento en tiempo de ejecucion** mediante `setEstrategia()`.
- **Cumplir con los principios SOLID**, especialmente el Principio Abierto/Cerrado y el de Responsabilidad Unica.
- **Mejorar la mantenibilidad y escalabilidad** del sistema a largo plazo.
