import networkx as nx

class Grafo:
    def __init__(self, archivo):
        self.G = nx.DiGraph()
        self.leer_archivo(archivo)
        self.distancias = dict(nx.all_pairs_dijkstra_path_length(self.G))

    def leer_archivo(self, archivo):
        with open(archivo, 'r') as file:
            for line in file:
                ciudad1, ciudad2, distancia = line.strip().split()
                self.G.add_edge(ciudad1, ciudad2, distancia=float(distancia))
    
    def ruta_mas_corta(self, origen, destino):
        try:
            distancia = self.distancias[origen][destino]
            ruta = nx.shortest_path(self.G, origen, destino)
            return f"Ruta más corta de {origen} a {destino}: {' -> '.join(ruta)} con una distancia de {distancia} KM."
        except KeyError:
            return "No hay ruta entre las ciudades especificadas."

    def ciudad_centro(self):
        excentricidades = nx.eccentricity(self.G)
        centro = min(excentricidades, key=excentricidades.get)
        return f"La ciudad centro del grafo es: {centro}."

if __name__ == "__main__":
    grafo = Grafo("guategrafo.txt")

    while True:
        print("Opciones:")
        print("1. Consultar ruta más corta entre dos ciudades.")
        print("2. Mostrar ciudad centro del grafo.")
        print("3. Finalizar programa.")
        opcion = input("Seleccione una opción: ")

        if opcion == '1':
            origen = input("Ciudad origen: ")
            destino = input("Ciudad destino: ")
            print(grafo.ruta_mas_corta(origen, destino))
        elif opcion == '2':
            print(grafo.ciudad_centro())
        elif opcion == '3':
            print("Finalizando programa...")
            break
        else:
            print("Opción no válida.")