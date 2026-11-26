package estructuras;

/**
 * Grafo DIRIGIDO y PONDERADO con listas de adyacencia dinámicas (listas enlazadas).
 * Los nombres de los vértices se normalizan a MAYÚSCULAS.
 * Orden de vecinos: el orden en que se insertaron las aristas (se agregan al final).
 */
public class GrafoDirigido {
    private final ListaEnlazada<Vertice> vertices = new ListaEnlazada<>();

    private static String normalizar(String n) {
        if (n == null || n.trim().isEmpty()) throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        return n.trim().toUpperCase();
    }

    /** O(V) por la verificación de duplicados. */
    public void agregarVertice(String nombre) {
        String n = normalizar(nombre);
        if (buscarVertice(n) != null) throw new IllegalArgumentException("El equipo " + n + " ya existe.");
        vertices.agregarAlFinal(new Vertice(n));
    }

    /** O(V). */
    public Vertice buscarVertice(String nombre) {
        if (nombre == null) return null;
        String n = nombre.trim().toUpperCase();
        for (Vertice v : vertices) if (v.getNombre().equals(n)) return v;
        return null;
    }

    public boolean existeVertice(String nombre) { return buscarVertice(nombre) != null; }

    private Vertice obtenerExistente(String nombre) {
        Vertice v = buscarVertice(normalizar(nombre));
        if (v == null) throw new IllegalArgumentException("El equipo " + normalizar(nombre) + " no existe.");
        return v;
    }

    /** Agrega origen -> destino con peso. O(V + grado(origen)). */
    public void agregarArista(String origen, String destino, int peso) {
        Vertice o = obtenerExistente(origen);
        Vertice d = obtenerExistente(destino);
        if (o == d) throw new IllegalArgumentException("No se permite conectar un equipo consigo mismo.");
        if (peso <= 0) throw new IllegalArgumentException("El peso debe ser mayor que 0.");
        if (o.aristaHacia(d.getNombre()) != null)
            throw new IllegalArgumentException("La conexión " + o + " -> " + d + " ya existe.");
        o.getAdyacentes().agregarAlFinal(new Arista(d, peso));
    }

    /** Elimina origen -> destino. Devuelve el peso que tenía. O(V + grado(origen)). */
    public int eliminarArista(String origen, String destino) {
        Vertice o = obtenerExistente(origen);
        Vertice d = obtenerExistente(destino);
        Arista a = o.aristaHacia(d.getNombre());
        if (a == null) throw new IllegalArgumentException("No existe la conexión " + o + " -> " + d + ".");
        o.getAdyacentes().eliminar(a);
        return a.getPeso();
    }

    /**
     * Elimina un vértice: primero borra todas las aristas ENTRANTES (recorriendo
     * las listas de adyacencia de los demás vértices) y luego el vértice con sus
     * aristas salientes. O(V + E).
     */
    public void eliminarVertice(String nombre) {
        Vertice objetivo = obtenerExistente(nombre);
        for (Vertice v : vertices) {
            Arista entrante = v.aristaHacia(objetivo.getNombre());
            if (entrante != null) v.getAdyacentes().eliminar(entrante);
        }
        vertices.eliminar(objetivo);
    }

    private void limpiarVisitados() {
        for (Vertice v : vertices) v.visitado = false;
    }

    /** Recorrido en anchura usando una COLA. O(V + E). */
    public ListaEnlazada<String> bfs(String inicio) {
        Vertice s = obtenerExistente(inicio);
        limpiarVisitados();
        ListaEnlazada<String> orden = new ListaEnlazada<>();
        Cola<Vertice> cola = new Cola<>();
        s.visitado = true;           // se marca al ENCOLAR para no repetir vértices
        cola.encolar(s);
        while (!cola.estaVacia()) {
            Vertice actual = cola.desencolar();
            orden.agregarAlFinal(actual.getNombre());
            for (Arista a : actual.getAdyacentes()) {
                Vertice w = a.getDestino();
                if (!w.visitado) {
                    w.visitado = true;
                    cola.encolar(w);
                }
            }
        }
        return orden;
    }

    /** Recorrido en profundidad (recursivo, vecinos en orden de inserción). O(V + E). */
    public ListaEnlazada<String> dfs(String inicio) {
        Vertice s = obtenerExistente(inicio);
        limpiarVisitados();
        ListaEnlazada<String> orden = new ListaEnlazada<>();
        dfsRec(s, orden);
        return orden;
    }

    private void dfsRec(Vertice v, ListaEnlazada<String> orden) {
        v.visitado = true;
        orden.agregarAlFinal(v.getNombre());
        for (Arista a : v.getAdyacentes()) {
            if (!a.getDestino().visitado) dfsRec(a.getDestino(), orden);
        }
    }

    /** Equipos que NO se alcanzan desde el origen (se apoya en BFS). O(V + E). */
    public ListaEnlazada<String> noAlcanzables(String inicio) {
        bfs(inicio); // deja marcados los visitados
        ListaEnlazada<String> r = new ListaEnlazada<>();
        for (Vertice v : vertices) if (!v.visitado) r.agregarAlFinal(v.getNombre());
        return r;
    }

    /** ¿Existe un camino dirigido de origen a destino? O(V + E). */
    public boolean existeCamino(String origen, String destino) {
        Vertice d = obtenerExistente(destino);
        bfs(origen);
        return d.visitado;
    }

    public ListaEnlazada<Vertice> getVertices() { return vertices; }
    public int cantidadVertices() { return vertices.tamanio(); }

    public int cantidadAristas() {
        int c = 0;
        for (Vertice v : vertices) c += v.getAdyacentes().tamanio();
        return c;
    }

    public String mostrar() {
        if (vertices.estaVacia()) return "(red sin equipos)\n";
        StringBuilder sb = new StringBuilder();
        for (Vertice v : vertices) {
            sb.append(String.format("%-6s -> ", v.getNombre()));
            if (v.getAdyacentes().estaVacia()) sb.append("(sin conexiones salientes)");
            else sb.append(v.getAdyacentes());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void vaciar() { vertices.vaciar(); }
}
