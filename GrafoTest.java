import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class GrafoTest {
    private Grafo grafo;

    @Before
    public void setUp() throws IOException {
        // Crea un archivo temporal para pruebas
        String data = "Mixco Antigua 30\nAntigua Escuintla 25\nEscuintla SantaLucia 15";
        File file = new File("guategrafo_test.txt");
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.close();

        grafo = new Grafo("guategrafo_test.txt");
    }

    @Test
    public void testAgregarNodoYArco() throws IOException {
        grafo.modificarGrafo("Mixco", "Escuintla", 50);
        assertEquals("Mixco -> Antigua -> Escuintla con una distancia de 55.0 KM.", grafo.rutaMasCorta("Mixco", "Escuintla"));
    }

    @Test
    public void testEliminarArco() throws IOException {
        grafo.eliminarConexion("Antigua", "Escuintla");
        assertEquals("No hay ruta entre Antigua y Escuintla", grafo.rutaMasCorta("Antigua", "Escuintla"));
    }

    @Test
    public void testFloydWarshall() {
        // Verificar que el algoritmo de Floyd-Warshall haya calculado correctamente las rutas más cortas
        grafo.modificarGrafo("Mixco", "SantaLucia", 100);
        assertEquals("Mixco -> Antigua -> Escuintla -> SantaLucia con una distancia de 70.0 KM.", grafo.rutaMasCorta("Mixco", "SantaLucia"));
    }

    @Test
    public void testCiudadCentro() {
        assertEquals("Antigua", grafo.ciudadCentro());
    }
}