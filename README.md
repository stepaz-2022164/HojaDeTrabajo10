# Hoja De Trabajo 10

## Descripción

Sistema de planificación de rutas para el **Centro de Respuesta al Covid-19** de Guatemala. Dado un grafo dirigido y ponderado donde los nodos son ciudades y los arcos son distancias en kilómetros, el programa calcula:

- La **ruta más corta** entre cualquier par de ciudades (Floyd-Warshall).
- El **centro del grafo**, ciudad óptima para ubicar oficinas de logística.
- Actualización dinámica del grafo ante **cordones sanitarios o derrumbes** que interrumpen carreteras.

---

## Estructura del proyecto

```
HojaDeTrabajo10/
├── src/
│   ├── main/java/
│   │   ├── GrafoCovid.java       # Implementación del grafo y Floyd-Warshall
│   │   └── Main.java             # Programa principal (menú interactivo)
│   └── test/java/
│       └── GrafoCovidTest.java   # Pruebas unitarias JUnit 5
├── grafo_networkx.py             # Implementación opcional con NetworkX (Python)
├── guategrafo.txt                # Archivo de datos del grafo inicial
├── HojaDeTrabajo10.iml
└── README.md
```

---

## Requisitos

### Java
- JDK 11 o superior
- JUnit 5 (para pruebas unitarias)
- IDE recomendado: IntelliJ IDEA


---

## Formato del archivo de entrada

El programa lee el grafo desde `guategrafo.txt`. Cada línea define un arco dirigido:

```
Ciudad1 Ciudad2 KM
```

Ejemplo:
```
Mixco Antigua 30
Antigua Escuintla 25
Escuintla SantaLucia 15
Guatemala Mixco 10
Guatemala Coban 200
```

- El grafo es **dirigido**: `Mixco Antigua 30` no implica que haya ruta de Antigua a Mixco.
- Los nombres de ciudad no deben contener espacios.

---

## Opciones del menú

```
=== CENTRO DE RESPUESTA COVID-19 ===
1. Buscar ruta más corta entre dos ciudades
2. Indicar ciudad en el centro del grafo
3. Modificar el grafo (interrupción o nueva conexión)
4. Mostrar matriz de adyacencia
5. Finalizar programa
```

**Opción 1** Ingresa ciudad origen y destino; muestra distancia total en KM y los nodos intermedios de la ruta óptima.

**Opción 2** Muestra la ciudad que es el centro del grafo y su excentricidad.

**Opción 3** Permite:
- `a` Interrumpir tráfico entre dos ciudades (eliminar arco).
- `b` Establecer nueva conexión entre dos ciudades con su distancia. Tras cualquier modificación, Floyd-Warshall se recalcula automáticamente.

**Opción 4** — Imprime la matriz de distancias más cortas resultante de Floyd-Warshall.

---

## Algoritmos implementados

### Floyd-Warshall

Calcula la distancia mínima entre **todos los pares de vértices** en O(n³). La implementación mantiene dos matrices:

- `grafoOriginal[][]` — arcos directos tal como fueron ingresados.
- `distancias[][]` / `siguientes[][]` — resultado de Floyd, reconstruidas desde cero en cada recálculo para garantizar correctitud tras eliminar arcos.

### Centro del grafo

Para cada vértice `i` se calcula su **excentricidad**: el máximo de todas las distancias que otros vértices tienen *hacia* `i` (columna `i` de la matriz de Floyd). El centro es el vértice con excentricidad mínima.

---

## Pruebas unitarias

El archivo `GrafoCovidTest.java` cubre con **22 pruebas** los siguientes escenarios:

| Grupo | Pruebas |
|---|---|
| Agregar nodos | Nodo nuevo, duplicado, nodo vía `agregarRuta` |
| Agregar arcos | Distancia directa, grafo dirigido, actualizar arco existente |
| Eliminar arcos | Ruta directa, mantiene ruta indirecta, ciudad inexistente |
| Floyd-Warshall | Ruta directa, ruta indirecta más corta, varios intermedios, sin ruta, recálculo tras eliminar |
| Centro del grafo | Nodo correcto, excentricidad correcta |

Para ejecutar con JUnit 5 desde IntelliJ IDEA: clic derecho sobre `GrafoCovidTest.java` → *Run*.

---