package estructuras;

/** Cola FIFO enlazada con referencias a frente y final. encolar y desencolar son O(1). */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public void encolar(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se puede encolar un elemento nulo.");
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía: no hay solicitudes pendientes.");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía.");
        return frente.dato;
    }

    /**
     * Devuelve un elemento al FRENTE de la cola. Solo lo usa "deshacer" para
     * revertir la atención de una solicitud: como fue la más antigua en salir,
     * vuelve a ser la primera en ser atendida. O(1).
     */
    public void devolverAlFrente(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se puede devolver un elemento nulo.");
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = frente;
        frente = nuevo;
        if (fin == null) fin = nuevo;
        tamanio++;
    }

    public boolean estaVacia() { return frente == null; }
    public int tamanio() { return tamanio; }

    public void vaciar() { frente = fin = null; tamanio = 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Nodo<T> n = frente; n != null; n = n.siguiente) {
            sb.append(i == 1 ? "FRENTE -> " : "          ").append(n.dato).append("\n");
            i++;
        }
        return sb.length() == 0 ? "(cola vacía)\n" : sb.toString();
    }
}
