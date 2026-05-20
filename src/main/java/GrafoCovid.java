package main.java;

import java.util.*;

public class GrafoCovid {
    private int[][] grafoOriginal;
    private int[][] distancias;    // Resultado de Floyd
    private int[][] siguientes;    // Matriz de caminos de Floyd
    private Map<String, Integer> ciudadAIndice;
    private Map<Integer, String> indiceACiudad;
    private int numNodos;
    private int capacidad;
    public static final int INF = 999999999;

    public GrafoCovid(int capacidad) {
        this.capacidad = capacidad;
        grafoOriginal = new int[capacidad][capacidad];
        distancias = new int[capacidad][capacidad];
        siguientes = new int[capacidad][capacidad];
        ciudadAIndice = new HashMap<>();
        indiceACiudad = new HashMap<>();
        numNodos = 0;
        inicializarGrafoOriginal();
    }

    private void inicializarGrafoOriginal() {
        for (int i = 0; i < capacidad; i++) {
            Arrays.fill(grafoOriginal[i], INF);
            grafoOriginal[i][i] = 0;
        }
    }

    // Reinicia las matrices de Floyd copiando desde el grafo original
    private void reiniciarFloyd() {
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                distancias[i][j] = grafoOriginal[i][j];
                siguientes[i][j] = (grafoOriginal[i][j] != INF && i != j) ? j : -1;
            }
            distancias[i][i] = 0;
            siguientes[i][i] = i;
        }
    }

    public void agregarCiudad(String ciudad) {
        if (!ciudadAIndice.containsKey(ciudad) && numNodos < capacidad) {
            ciudadAIndice.put(ciudad, numNodos);
            indiceACiudad.put(numNodos, ciudad);
            numNodos++;
        }
    }

    public void agregarRuta(String origen, String destino, int distancia) {
        agregarCiudad(origen);
        agregarCiudad(destino);
        int u = ciudadAIndice.get(origen);
        int v = ciudadAIndice.get(destino);
        grafoOriginal[u][v] = distancia;
    }

    public void eliminarRuta(String origen, String destino) {
        if (ciudadAIndice.containsKey(origen) && ciudadAIndice.containsKey(destino)) {
            int u = ciudadAIndice.get(origen);
            int v = ciudadAIndice.get(destino);
            grafoOriginal[u][v] = INF;
        }
    }

    public void calcularFloyd() {
        reiniciarFloyd();
        for (int k = 0; k < numNodos; k++) {
            for (int i = 0; i < numNodos; i++) {
                for (int j = 0; j < numNodos; j++) {
                    if (distancias[i][k] != INF && distancias[k][j] != INF) {
                        long nuevaDist = (long) distancias[i][k] + distancias[k][j];
                        if (nuevaDist < distancias[i][j]) {
                            distancias[i][j] = (int) nuevaDist;
                            siguientes[i][j] = siguientes[i][k];
                        }
                    }
                }
            }
        }
    }

    public String obtenerRutaMinima(String origen, String destino) {
        if (!ciudadAIndice.containsKey(origen) || !ciudadAIndice.containsKey(destino)) {
            return "Una o ambas ciudades no existen en el mapa.";
        }
        int u = ciudadAIndice.get(origen);
        int v = ciudadAIndice.get(destino);

        if (u == v) return "El origen y destino son la misma ciudad.";

        if (distancias[u][v] == INF || siguientes[u][v] == -1) {
            return "No hay ruta disponible entre " + origen + " y " + destino + ".";
        }

        StringBuilder ruta = new StringBuilder(origen);
        int actual = u;
        int maxSteps = numNodos + 1;
        int steps = 0;

        while (actual != v && steps < maxSteps) {
            int siguiente = siguientes[actual][v];
            if (siguiente == -1) {
                return "No hay ruta disponible entre " + origen + " y " + destino + ".";
            }
            actual = siguiente;
            ruta.append(" -> ").append(indiceACiudad.get(actual));
            steps++;
        }

        return "Distancia total: " + distancias[u][v] + " KM.\nRuta: " + ruta.toString();
    }

    public String encontrarCentro() {
        int[] excentricidades = new int[numNodos];

        for (int i = 0; i < numNodos; i++) {
            int maxColumna = 0;
            for (int j = 0; j < numNodos; j++) {
                if (i != j && distancias[j][i] != INF && distancias[j][i] > maxColumna) {
                    maxColumna = distancias[j][i];
                }
            }
            excentricidades[i] = maxColumna;
        }

        int minExcentricidad = INF;
        int indiceCentro = -1;

        for (int i = 0; i < numNodos; i++) {
            if (excentricidades[i] > 0 && excentricidades[i] < minExcentricidad) {
                minExcentricidad = excentricidades[i];
                indiceCentro = i;
            }
        }

        if (indiceCentro != -1) {
            return "El centro del grafo es: " + indiceACiudad.get(indiceCentro) +
                    " (Excentricidad: " + minExcentricidad + ")";
        } else {
            return "No se pudo determinar un centro válido (el grafo podría estar desconectado).";
        }
    }

    public void mostrarMatrizAdyacencia() {
        System.out.println("\n--- Matriz de Distancias Más Cortas (Floyd-Warshall) ---");
        System.out.print("\t\t");
        for (int j = 0; j < numNodos; j++) {
            String nombre = indiceACiudad.get(j);
            System.out.printf("%-14s", nombre);
        }
        System.out.println();
        for (int i = 0; i < numNodos; i++) {
            System.out.printf("%-14s", indiceACiudad.get(i));
            for (int j = 0; j < numNodos; j++) {
                if (distancias[i][j] == INF) {
                    System.out.printf("%-14s", "INF");
                } else {
                    System.out.printf("%-14d", distancias[i][j]);
                }
            }
            System.out.println();
        }
    }

    public int getDistancia(String origen, String destino) {
        if (!ciudadAIndice.containsKey(origen) || !ciudadAIndice.containsKey(destino)) return INF;
        return distancias[ciudadAIndice.get(origen)][ciudadAIndice.get(destino)];
    }

    public boolean existeCiudad(String ciudad) {
        return ciudadAIndice.containsKey(ciudad);
    }

    public int getNumNodos() { return numNodos; }
}