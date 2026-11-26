package servicios;

import estructuras.GrafoDirigido;
import estructuras.ListaEnlazada;

/** M4 – Red de laboratorios (grafo dirigido y ponderado). */
public class ServicioRedLaboratorio {
    private final GrafoDirigido red = new GrafoDirigido();

    public void agregarEquipo(String nombre) { red.agregarVertice(nombre); }
    public void eliminarEquipo(String nombre) { red.eliminarVertice(nombre); }
    public void agregarConexion(String o, String d, int peso) { red.agregarArista(o, d, peso); }
    public int eliminarConexion(String o, String d) { return red.eliminarArista(o, d); }
    public boolean existeEquipo(String nombre) { return red.existeVertice(nombre); }

    public ListaEnlazada<String> bfs(String inicio) { return red.bfs(inicio); }
    public ListaEnlazada<String> dfs(String inicio) { return red.dfs(inicio); }
    public ListaEnlazada<String> noAlcanzables(String inicio) { return red.noAlcanzables(inicio); }
    public boolean existeCamino(String o, String d) { return red.existeCamino(o, d); }

    /** Informe de conectividad desde un origen. */
    public String conectividad(String inicio) {
        ListaEnlazada<String> bfs = red.bfs(inicio);
        ListaEnlazada<String> no = red.noAlcanzables(inicio);
        return "BFS desde " + inicio.trim().toUpperCase() + ": " + unir(bfs) + "\n"
                + "No alcanzables: " + (no.estaVacia() ? "ninguno (todos son alcanzables)" : unir(no)) + "\n";
    }

    public String mostrar() {
        return red.mostrar() + "Equipos: " + red.cantidadVertices() + " | Conexiones: " + red.cantidadAristas() + "\n";
    }

    public static String unir(ListaEnlazada<String> l) {
        StringBuilder sb = new StringBuilder();
        for (String s : l) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(s);
        }
        return sb.length() == 0 ? "(vacío)" : sb.toString();
    }

    public GrafoDirigido getRed() { return red; }
    public void reiniciar() { red.vaciar(); }
}
