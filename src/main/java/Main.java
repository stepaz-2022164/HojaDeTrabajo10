package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GrafoCovid grafo = new GrafoCovid(50);

        try (BufferedReader br = new BufferedReader(new FileReader("guategrafo.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(" ");
                if (partes.length == 3) {
                    String origen = partes[0];
                    String destino = partes[1];
                    int distancia = Integer.parseInt(partes[2]);
                    grafo.agregarRuta(origen, destino, distancia);
                }
            }
            System.out.println("Archivo cargado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo. Asegúrese de que 'guategrafo.txt' esté en la misma carpeta.");
            System.out.println("Iniciando con grafo vacío...\n");
        }

        grafo.calcularFloyd();

        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== CENTRO DE RESPUESTA COVID-19 ===");
            System.out.println("1. Buscar ruta más corta entre dos ciudades");
            System.out.println("2. Indicar ciudad en el centro del grafo");
            System.out.println("3. Modificar el grafo (Interrupción o nueva conexión)");
            System.out.println("4. Mostrar matriz de adyacencia");
            System.out.println("5. Finalizar programa");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Ingrese ciudad de origen: ");
                    String origen = scanner.nextLine();
                    System.out.print("Ingrese ciudad de destino: ");
                    String destino = scanner.nextLine();
                    System.out.println("\n" + grafo.obtenerRutaMinima(origen, destino));
                    break;

                case "2":
                    System.out.println("\n" + grafo.encontrarCentro());
                    break;

                case "3":
                    System.out.println("\n¿Qué desea hacer?");
                    System.out.println("a) Indicar interrupción de tráfico (eliminar ruta)");
                    System.out.println("b) Establecer nueva conexión");
                    System.out.print("Opción: ");
                    String subOpcion = scanner.nextLine();

                    if (subOpcion.equalsIgnoreCase("a")) {
                        System.out.print("Ciudad origen: ");
                        String o1 = scanner.nextLine();
                        System.out.print("Ciudad destino: ");
                        String d1 = scanner.nextLine();
                        grafo.eliminarRuta(o1, d1);
                        System.out.println("Ruta interrumpida. Recalculando logística...");
                    } else if (subOpcion.equalsIgnoreCase("b")) {
                        System.out.print("Ciudad origen: ");
                        String o2 = scanner.nextLine();
                        System.out.print("Ciudad destino: ");
                        String d2 = scanner.nextLine();
                        System.out.print("Distancia en KM: ");
                        try {
                            int dist = Integer.parseInt(scanner.nextLine());
                            grafo.agregarRuta(o2, d2, dist);
                            System.out.println("Conexión establecida. Recalculando logística...");
                        } catch (NumberFormatException e) {
                            System.out.println("Distancia no válida.");
                            break;
                        }
                    } else {
                        System.out.println("Opción no válida.");
                        break;
                    }

                    grafo.calcularFloyd();
                    break;

                case "4":
                    grafo.mostrarMatrizAdyacencia();
                    break;

                case "5":
                    salir = true;
                    System.out.println("Cerrando el sistema del Centro de Respuesta Covid-19. ¡Manténgase a salvo!");
                    break;

                default:
                    System.out.println("Opción no reconocida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}
