package main.java;

import java.util.*;

public class GrafoCovid {
    private int[][] distancias;
    private int[][] siguientes;
    private Map<String, Integer> ciudadAIndice;
    private Map<Integer, String> indiceACiudad;
    private int numNodos;
    private int capacidad;
    private final int INF = 999999999; // Representa el infinito

    public GrafoCovid(int capacidad) {
        this.capacidad = capacidad;
        distancias = new int[capacidad][capacidad];
        siguientes = new int[capacidad][capacidad];
        ciudadAIndice = new HashMap<>();
        indiceACiudad = new HashMap<>();
        numNodos = 0;

        inicializarMatrices();
    }

    private void inicializarMatrices() {
        for (int i = 0; i < capacidad; i++) {
            Arrays.fill(distancias[i], INF);
            Arrays.fill(siguientes[i], -1);
            distancias[i][i] = 0;
        }
    }

    // Agrega un nodo si no existe
    public void agregarCiudad(String ciudad) {
        if (!ciudadAIndice.containsKey(ciudad) && numNodos < capacidad) {
            ciudadAIndice.put(ciudad, numNodos);
            indiceACiudad.put(numNodos, ciudad);
            numNodos++;
        }
    }

    // Agrega o actualiza un arco (distancia)
    public void agregarRuta(String origen, String destino, int distancia) {
        agregarCiudad(origen);
        agregarCiudad(destino);

        int u = ciudadAIndice.get(origen);
        int v = ciudadAIndice.get(destino);

        distancias[u][v] = distancia;
        siguientes[u][v] = v; // El siguiente nodo para ir de u a v, inicialmente es v
    }

    // Interrumpe el tráfico entre dos ciudades
    public void eliminarRuta(String origen, String destino) {
        if (ciudadAIndice.containsKey(origen) && ciudadAIndice.containsKey(destino)) {
            int u = ciudadAIndice.get(origen);
            int v = ciudadAIndice.get(destino);
            distancias[u][v] = INF;
            siguientes[u][v] = -1;
        }
    }

    // Algoritmo de Floyd-Warshall
    public void calcularFloyd() {
        // k es el nodo intermedio
        for (int k = 0; k < numNodos; k++) {
            // i es el nodo de origen
            for (int i = 0; i < numNodos; i++) {
                // j es el nodo de destino
                for (int j = 0; j < numNodos; j++) {
                    if (distancias[i][k] != INF && distancias[k][j] != INF) {
                        if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                            distancias[i][j] = distancias[i][k] + distancias[k][j];
                            // Actualizamos el camino para pasar por k
                            siguientes[i][j] = siguientes[i][k];
                        }
                    }
                }
            }
        }
    }

    // Obtener la ruta completa y la distancia
    public String obtenerRutaMinima(String origen, String destino) {
        if (!ciudadAIndice.containsKey(origen) || !ciudadAIndice.containsKey(destino)) {
            return "Una o ambas ciudades no existen en el mapa.";
        }

        int u = ciudadAIndice.get(origen);
        int v = ciudadAIndice.get(destino);

        if (distancias[u][v] == INF) {
            return "No hay ruta disponible entre " + origen + " y " + destino + ".";
        }

        StringBuilder ruta = new StringBuilder();
        ruta.append(origen);
        int actual = u;

        while (actual != v) {
            actual = siguientes[actual][v];
            ruta.append(" -> ").append(indiceACiudad.get(actual));
        }

        return "Distancia total: " + distancias[u][v] + " KM.\nRuta: " + ruta.toString();
    }

    // Calcular el centro del grafo según el documento
    public String encontrarCentro() {
        int[] excentricidades = new int[numNodos];

        // Encontrar el costo máximo en cada columna i [cite: 25]
        for (int i = 0; i < numNodos; i++) {
            int maxColumna = 0;
            for (int j = 0; j < numNodos; j++) {
                if (distancias[j][i] > maxColumna && distancias[j][i] != INF) {
                    maxColumna = distancias[j][i];
                }
            }
            excentricidades[i] = maxColumna; // Excentricidad del vértice i [cite: 25]
        }

        // Encontrar el vértice con excentricidad mínima [cite: 26]
        int minExcentricidad = INF;
        int indiceCentro = -1;

        for (int i = 0; i < numNodos; i++) {
            // Ignoramos los nodos aislados (excentricidad 0 si no llega nadie, o INF)
            if (excentricidades[i] < minExcentricidad && excentricidades[i] > 0) {
                minExcentricidad = excentricidades[i];
                indiceCentro = i;
            }
        }

        if (indiceCentro != -1) {
            return "El centro del grafo es: " + indiceACiudad.get(indiceCentro) +
                    " (Excentricidad: " + minExcentricidad + ")"; // Centro de G [cite: 26]
        } else {
            return "No se pudo determinar un centro válido (el grafo podría estar desconectado).";
        }
    }

    // Método para mostrar la matriz de adyacencia
    public void mostrarMatrizAdyacencia() {
        System.out.println("\n--- Matriz de Adyacencia (Distancias más cortas) ---");
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                if (distancias[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(distancias[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
}
