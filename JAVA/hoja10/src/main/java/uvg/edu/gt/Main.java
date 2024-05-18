import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Grafo grafo = new Grafo("guategrafo.txt");

            while (true) {
                System.out.println("Opciones:");
                System.out.println("1. Consultar ruta más corta entre dos ciudades.");
                System.out.println("2. Mostrar ciudad centro del grafo.");
                System.out.println("3. Modificar el grafo.");
                System.out.println("4. Finalizar programa.");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Ciudad origen: ");
                        String origen = scanner.nextLine();
                        System.out.print("Ciudad destino: ");
                        String destino = scanner.nextLine();
                        System.out.println(grafo.rutaMasCorta(origen, destino));
                        break;
                    case 2:
                        System.out.println("La ciudad centro del grafo es: " + grafo.ciudadCentro());
                        break;
                    case 3:
                        System.out.println("Opciones para modificar el grafo:");
                        System.out.println("1. Interrupción de tráfico entre un par de ciudades.");
                        System.out.println("2. Establecer conexión entre dos ciudades con una distancia.");
                        char opcionMod = scanner.nextLine().charAt(0);
                        System.out.print("Ciudad 1: ");
                        String ciudad1 = scanner.nextLine();
                        System.out.print("Ciudad 2: ");
                        String ciudad2 = scanner.nextLine();
                        if (opcionMod == '1') {
                            grafo.eliminarConexion(ciudad1, ciudad2);
                            System.out.println("Interrupción de tráfico realizada.");
                        } else if (opcionMod == '2') {
                            System.out.print("Distancia en KM: ");
                            double distancia = scanner.nextDouble();
                            scanner.nextLine();  
                            grafo.modificarGrafo(ciudad1, ciudad2, distancia);
                            System.out.println("Conexión establecida.");
                        } else {
                            System.out.println("Opción no válida.");
                        }
                        break;
                    case 4:
                        System.out.println("Finalizando programa...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}