package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada simple implementada manualmente.
 * Inserta al final (O(1) gracias a la referencia "fin"), por eso el orden de
 * recorrido es siempre el ORDEN DE INSERCIÓN (importante para BFS/DFS reproducibles).
 */
public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> inicio;
    private Nodo<T> fin;
    private int tamanio;

    /** Inserta al final. O(1). */
    public void agregarAlFinal(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se permiten elementos nulos en la lista.");
        Nodo<T> nuevo = new Nodo<>(dato);
        if (inicio == null) {
            inicio = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    /** Inserta al inicio. O(1). */
    public void agregarAlInicio(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se permiten elementos nulos en la lista.");
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = inicio;
        inicio = nuevo;
        if (fin == null) fin = nuevo;
        tamanio++;
    }

    /** Elimina la primera aparición (comparando con equals). O(n). */
    public boolean eliminar(T dato) {
        if (inicio == null || dato == null) return false;
        if (inicio.dato.equals(dato)) {
            inicio = inicio.siguiente;
            if (inicio == null) fin = null;
            tamanio--;
            return true;
        }
        Nodo<T> anterior = inicio;
        Nodo<T> actual = inicio.siguiente;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                anterior.siguiente = actual.siguiente;
                if (actual == fin) fin = anterior;
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean contiene(T dato) {
        for (Nodo<T> n = inicio; n != null; n = n.siguiente) {
            if (n.dato.equals(dato)) return true;
        }
        return false;
    }

    /** Acceso por posición. O(n). */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        Nodo<T> n = inicio;
        for (int i = 0; i < indice; i++) n = n.siguiente;
        return n.dato;
    }

    public void vaciar() {
        inicio = fin = null;
        tamanio = 0;
    }

    public int tamanio() { return tamanio; }
    public boolean estaVacia() { return tamanio == 0; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = inicio;
            public boolean hasNext() { return actual != null; }
            public T next() {
                if (actual == null) throw new NoSuchElementException();
                T d = actual.dato;
                actual = actual.siguiente;
                return d;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Nodo<T> n = inicio; n != null; n = n.siguiente) {
            sb.append(n.dato);
            if (n.siguiente != null) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
