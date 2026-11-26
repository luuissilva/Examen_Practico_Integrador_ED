package estructuras;

/** Pila LIFO enlazada. apilar, desapilar y cima son O(1). */
public class Pila<T> {
    private Nodo<T> cima;
    private int tamanio;

    public void apilar(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se puede apilar un elemento nulo.");
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía: no hay operaciones para deshacer.");
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    public T verCima() {
        if (estaVacia()) throw new IllegalStateException("La pila está vacía.");
        return cima.dato;
    }

    public boolean estaVacia() { return cima == null; }
    public int tamanio() { return tamanio; }

    public void vaciar() { cima = null; tamanio = 0; }

    /** Muestra desde la cima hacia el fondo. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Nodo<T> n = cima; n != null; n = n.siguiente) {
            sb.append(i++).append(". ").append(n.dato).append(i == 2 ? "   <- cima" : "").append("\n");
        }
        return sb.length() == 0 ? "(pila vacía)\n" : sb.toString();
    }
}
