package test.java;

import main.java.GrafoCovid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrafoCovidTest {

    private GrafoCovid grafo;

    @BeforeEach
    public void setUp() {
        grafo = new GrafoCovid(10);
    }

    // Pruebas de agregar nodos

    @Test
    public void testAgregarNodo_NodoNuevo() {
        grafo.agregarCiudad("Guatemala");
        assertTrue(grafo.existeCiudad("Guatemala"), "Guatemala debería existir después de agregarla.");
        assertEquals(1, grafo.getNumNodos());
    }

    @Test
    public void testAgregarNodo_Duplicado() {
        grafo.agregarCiudad("Antigua");
        grafo.agregarCiudad("Antigua");
        assertEquals(1, grafo.getNumNodos(), "No debe haber duplicados.");
    }

    @Test
    public void testAgregarNodo_ViaAgregarRuta() {
        grafo.agregarRuta("Mixco", "Antigua", 30);
        assertTrue(grafo.existeCiudad("Mixco"));
        assertTrue(grafo.existeCiudad("Antigua"));
        assertEquals(2, grafo.getNumNodos());
    }

    // Pruebas de agregar arcos

    @Test
    public void testAgregarArco_DistanciaDirecta() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();
        assertEquals(40, grafo.getDistancia("Guatemala", "Antigua"),
                "La distancia directa debe ser 40 KM.");
    }

    @Test
    public void testAgregarArco_DireccionUnica() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();
        assertEquals(GrafoCovid.INF, grafo.getDistancia("Antigua", "Guatemala"),
                "El grafo es dirigido; Antigua->Guatemala no debe existir.");
    }

    @Test
    public void testAgregarArco_ActualizarExistente() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.agregarRuta("Guatemala", "Antigua", 20); // Actualizar
        grafo.calcularFloyd();
        assertEquals(20, grafo.getDistancia("Guatemala", "Antigua"),
                "La distancia debe actualizarse a 20 KM.");
    }

    // Pruebas de eliminar arcos

    @Test
    public void testEliminarArco_RutaDirecta() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();
        grafo.eliminarRuta("Guatemala", "Antigua");
        grafo.calcularFloyd();
        assertEquals(GrafoCovid.INF, grafo.getDistancia("Guatemala", "Antigua"),
                "La ruta eliminada debe ser INF.");
        assertTrue(grafo.obtenerRutaMinima("Guatemala", "Antigua").contains("No hay ruta disponible"));
    }

    @Test
    public void testEliminarArco_MantieneRutaIndirecta() {
        grafo.agregarRuta("Guatemala", "Mixco", 15);
        grafo.agregarRuta("Mixco", "Antigua", 25);
        grafo.agregarRuta("Guatemala", "Antigua", 50);
        grafo.calcularFloyd();

        grafo.eliminarRuta("Guatemala", "Antigua");
        grafo.calcularFloyd();

        assertEquals(40, grafo.getDistancia("Guatemala", "Antigua"),
                "Debe quedar la ruta indirecta de 40 KM.");
    }

    @Test
    public void testEliminarArco_CiudadInexistente() {
        assertDoesNotThrow(() -> grafo.eliminarRuta("CiudadX", "CiudadY"),
                "Eliminar una ruta inexistente no debe lanzar excepción.");
    }

    // Pruebas del algoritmo de Floyd

    @Test
    public void testFloyd_RutaDirecta() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();
        String resultado = grafo.obtenerRutaMinima("Guatemala", "Antigua");
        assertTrue(resultado.contains("Distancia total: 40 KM"));
        assertTrue(resultado.contains("Guatemala -> Antigua"));
    }

    @Test
    public void testFloyd_RutaIndirectaMasCorta() {
        grafo.agregarRuta("Guatemala", "Mixco", 15);
        grafo.agregarRuta("Mixco", "Antigua", 25);
        grafo.agregarRuta("Guatemala", "Antigua", 50); // Directa más larga
        grafo.calcularFloyd();

        assertEquals(40, grafo.getDistancia("Guatemala", "Antigua"));
        String resultado = grafo.obtenerRutaMinima("Guatemala", "Antigua");
        assertTrue(resultado.contains("Distancia total: 40 KM"));
        assertTrue(resultado.contains("Guatemala -> Mixco -> Antigua"));
    }

    @Test
    public void testFloyd_VariosIntermedios() {
        grafo.agregarRuta("A", "B", 1);
        grafo.agregarRuta("B", "C", 2);
        grafo.agregarRuta("C", "D", 3);
        grafo.calcularFloyd();
        assertEquals(6, grafo.getDistancia("A", "D"));
        assertTrue(grafo.obtenerRutaMinima("A", "D").contains("A -> B -> C -> D"));
    }

    @Test
    public void testFloyd_SinRuta() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.agregarRuta("Coban", "Peten", 100);
        grafo.calcularFloyd();
        assertTrue(grafo.obtenerRutaMinima("Guatemala", "Coban").contains("No hay ruta disponible"));
    }

    @Test
    public void testFloyd_CiudadInexistente() {
        grafo.agregarRuta("Guatemala", "Antigua", 40);
        grafo.calcularFloyd();
        assertTrue(grafo.obtenerRutaMinima("Guatemala", "CiudadX").contains("no existen en el mapa"));
    }

    @Test
    public void testFloyd_RecalculoTrasEliminar() {
        grafo.agregarRuta("Guatemala", "Mixco", 15);
        grafo.agregarRuta("Mixco", "Antigua", 25);
        grafo.calcularFloyd();
        assertEquals(40, grafo.getDistancia("Guatemala", "Antigua"));

        // Eliminar el único camino
        grafo.eliminarRuta("Mixco", "Antigua");
        grafo.calcularFloyd();
        assertEquals(GrafoCovid.INF, grafo.getDistancia("Guatemala", "Antigua"),
                "Después de eliminar el único camino, debe ser INF.");
    }

    //  Prueba del centro del grafo

    @Test
    public void testCentroGrafo() {
        // Ejemplo del documento
        grafo.agregarRuta("a", "b", 1);
        grafo.agregarRuta("b", "c", 2);
        grafo.agregarRuta("b", "d", 1);
        grafo.agregarRuta("c", "d", 2);
        grafo.agregarRuta("c", "e", 4);
        grafo.agregarRuta("d", "c", 3);
        grafo.agregarRuta("d", "e", 5);
        grafo.calcularFloyd();

        String centro = grafo.encontrarCentro();
        assertTrue(centro.contains("b"), "El centro debe ser 'b'.");
        assertTrue(centro.contains("1"), "La excentricidad de 'b' debe ser 1.");
    }
}