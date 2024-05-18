import java.io.*;
import java.util.*;

public class Grafo {
    private Map<String, Integer> ciudades;
    private double[][] distancias;
    private int[][] camino;
    private int numCiudades;

    public Grafo(String archivo) throws IOException {
        ciudades = new HashMap<>();
        List<String[]> aristas = leerArchivo(archivo);
        numCiudades = ciudades.size();
        inicializarMatrices(aristas);
        floydWarshall();
    }

    private List<String[]> leerArchivo(String archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        List<String[]> aristas = new ArrayList<>();
        int indice = 0;

        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(" ");
            String ciudad1 = datos[0];
            String ciudad2 = datos[1];
            double distancia = Double.parseDouble(datos[2]);

            if (!ciudades.containsKey(ciudad1)) {
                ciudades.put(ciudad1, indice++);
            }
            if (!ciudades.containsKey(ciudad2)) {
                ciudades.put(ciudad2, indice++);
            }
            aristas.add(datos);
        }
        br.close();
        return aristas;
    }

    private void inicializarMatrices(List<String[]> aristas) {
        distancias = new double[numCiudades][numCiudades];
        camino = new int[numCiudades][numCiudades];

        for (int i = 0; i < numCiudades; i++) {
            Arrays.fill(distancias[i], Double.POSITIVE_INFINITY);
            Arrays.fill(camino[i], -1);
            distancias[i][i] = 0;
        }

        for (String[] arista : aristas) {
            int origen = ciudades.get(arista[0]);
            int destino = ciudades.get(arista[1]);
            double distancia = Double.parseDouble(arista[2]);

            distancias[origen][destino] = distancia;
            camino[origen][destino] = origen;
        }
    }

    private void floydWarshall() {
        for (int k = 0; k < numCiudades; k++) {
            for (int i = 0; i < numCiudades; i++) {
                for (int j = 0; j < numCiudades; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        camino[i][j] = camino[k][j];
                    }
                }
            }
        }
    }

    public void mostrarMatriz() {
        for (int i = 0; i < numCiudades; i++) {
            for (int j = 0; j < numCiudades; j++) {
                if (distancias[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("INF ");
                } else {
                    System.out.printf("%.2f ", distancias[i][j]);
                }
            }
            System.out.println();
        }
    }

    public String rutaMasCorta(String origen, String destino) {
        if (!ciudades.containsKey(origen) || !ciudades.containsKey(destino)) {
            return "Una de las ciudades no está en el grafo.";
        }

        int origenIndex = ciudades.get(origen);
        int destinoIndex = ciudades.get(destino);

        if (distancias[origenIndex][destinoIndex] == Double.POSITIVE_INFINITY) {
            return "No hay ruta entre " + origen + " y " + destino;
        }

        List<String> ruta = new ArrayList<>();
        reconstruirCamino(origenIndex, destinoIndex, ruta);
        return String.join(" -> ", ruta) + " con una distancia de " + distancias[origenIndex][destinoIndex] + " KM.";
    }

    private void reconstruirCamino(int i, int j, List<String> ruta) {
        if (i != j) {
            reconstruirCamino(i, camino[i][j], ruta);
        }
        ruta.add(getCiudadPorIndice(j));
    }

    private String getCiudadPorIndice(int indice) {
        for (Map.Entry<String, Integer> entry : ciudades.entrySet()) {
            if (entry.getValue() == indice) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String ciudadCentro() {
        double[] excetricidades = new double[numCiudades];
        Arrays.fill(excetricidades, Double.POSITIVE_INFINITY);

        for (int i = 0; i < numCiudades; i++) {
            for (int j = 0; j < numCiudades; j++) {
                if (i != j && distancias[i][j] < excetricidades[i]) {
                    excetricidades[i] = distancias[i][j];
                }
            }
        }

        double minExcentricidad = Double.POSITIVE_INFINITY;
        int centro = -1;
        for (int i = 0; i < numCiudades; i++) {
            if (excetricidades[i] < minExcentricidad) {
                minExcentricidad = excetricidades[i];
                centro = i;
            }
        }

        return getCiudadPorIndice(centro);
    }

    public void modificarGrafo(String ciudad1, String ciudad2, double distancia) {
        if (!ciudades.containsKey(ciudad1)) {
            ciudades.put(ciudad1, numCiudades++);
        }
        if (!ciudades.containsKey(ciudad2)) {
            ciudades.put(ciudad2, numCiudades++);
        }

        int origen = ciudades.get(ciudad1);
        int destino = ciudades.get(ciudad2);

        if (origen >= distancias.length || destino >= distancias.length) {
            double[][] nuevasDistancias = new double[numCiudades][numCiudades];
            int[][] nuevosCaminos = new int[numCiudades][numCiudades];

            for (int i = 0; i < distancias.length; i++) {
                for (int j = 0; j < distancias[i].length; j++) {
                    nuevasDistancias[i][j] = distancias[i][j];
                    nuevosCaminos[i][j] = camino[i][j];
                }
            }

            for (int i = distancias.length; i < nuevasDistancias.length; i++) {
                Arrays.fill(nuevasDistancias[i], Double.POSITIVE_INFINITY);
                Arrays.fill(nuevosCaminos[i], -1);
                nuevasDistancias[i][i] = 0;
            }

            distancias = nuevasDistancias;
            camino = nuevosCaminos;
        }

        distancias[origen][destino] = distancia;
        camino[origen][destino] = origen;
        floydWarshall();
    }

    public void eliminarConexion(String ciudad1, String ciudad2) {
        if (ciudades.containsKey(ciudad1) && ciudades.containsKey(ciudad2)) {
            int origen = ciudades.get(ciudad1);
            int destino = ciudades.get(ciudad2);
            distancias[origen][destino] = Double.POSITIVE_INFINITY;
            camino[origen][destino] = -1;
            floydWarshall();
        }
    }
}