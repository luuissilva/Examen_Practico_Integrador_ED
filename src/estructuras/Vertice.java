package estructuras;

/** Vértice de grafo dinámico con su lista de adyacencia (lista enlazada). */
public class Vertice {
    private final String nombre;
    private final ListaEnlazada<Arista> adyacentes;
    boolean visitado; // marca para recorridos

    public Vertice(String nombre) {
        this.nombre = nombre;
        this.adyacentes = new ListaEnlazada<>();
    }

    public String getNombre() { return nombre; }
    public ListaEnlazada<Arista> getAdyacentes() { return adyacentes; }

    /** Busca la arista hacia un destino. O(grado). */
    public Arista aristaHacia(String destino) {
        for (Arista a : adyacentes) {
            if (a.getDestino().getNombre().equals(destino)) return a;
        }
        return null;
    }

    @Override
    public String toString() { return nombre; }
}
