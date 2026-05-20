package test.java;

import main.java.GrafoCovid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrafoCovidTest {

    private GrafoCovid grafo;

    @BeforeEach
    public void setUp() {
        // Inicializamos un grafo limpio antes de cada prueba
        grafo = new GrafoCovid(10);
    }

    @Test
    public void testAgregarNodosYArcos() {
        // Prueba de: Agregar nodos y arcos
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();

        String resultado = grafo.obtenerRutaMinima("Guatemala", "Antigua");

        // Verificamos que la distancia se haya guardado correctamente
        assertTrue(resultado.contains("Distancia total: 40 KM"), "La distancia entre Guatemala y Antigua debería ser 40.");
        assertTrue(resultado.contains("Guatemala -> Antigua"), "La ruta debería ser directa.");
    }

    @Test
    public void testEliminarArco() {
        // Prueba de: Eliminar arcos
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd(); // Calculamos para establecer la ruta

        // Eliminamos la ruta (interrupción de tráfico)
        grafo.eliminarRuta("Guatemala", "Antigua");
        grafo.calcularFloyd(); // Recalculamos

        String resultado = grafo.obtenerRutaMinima("Guatemala", "Antigua");

        // Verificamos que ya no exista la ruta
        assertTrue(resultado.contains("No hay ruta disponible"), "La ruta debió ser eliminada.");
    }

    @Test
    public void testAlgoritmoFloyd() {
        // Prueba de: Algoritmo de Floyd (Ruta más corta con intermediarios)
        grafo.agregarRuta("Guatemala", "Mixco", 15);
        grafo.agregarRuta("Mixco", "Antigua", 25);
        grafo.agregarRuta("Guatemala", "Antigua", 50); // Ruta directa más larga

        grafo.calcularFloyd();

        String resultado = grafo.obtenerRutaMinima("Guatemala", "Antigua");

        // El algoritmo de Floyd debe preferir pasar por Mixco (15 + 25 = 40) en lugar de la directa (50)
        assertTrue(resultado.contains("Distancia total: 40 KM"), "Floyd debería calcular 40 KM pasando por Mixco.");
        assertTrue(resultado.contains("Guatemala -> Mixco -> Antigua"), "La ruta debe mostrar el nodo intermedio.");
    }

    @Test
    public void testCentroDelGrafo() {
        // Prueba de: Centro del Grafo
        grafo.agregarRuta("a", "b", 1);
        grafo.agregarRuta("b", "c", 2);
        grafo.agregarRuta("b", "d", 1);
        grafo.agregarRuta("c", "d", 2);
        grafo.agregarRuta("c", "e", 4);
        grafo.agregarRuta("d", "c", 3);
        grafo.agregarRuta("d", "e", 5);

        grafo.calcularFloyd();
        String resultadoCentro = grafo.encontrarCentro();

        assertTrue(resultadoCentro.contains("b"), "El centro matemático real del grafo debería ser el vértice 'b'.");
        assertTrue(resultadoCentro.contains("1"), "La excentricidad del centro debería ser 1.");
    }
}