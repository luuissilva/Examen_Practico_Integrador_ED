package estructuras;

/**
 * Vector (arreglo dinámico) implementado manualmente.
 * Cuando se llena, duplica su capacidad (inserción amortizada O(1)).
 */
public class Vector<T> {
    private Object[] datos;
    private int tamanio;

    public Vector() { this(4); }

    public Vector(int capacidadInicial) {
        if (capacidadInicial <= 0) capacidadInicial = 4;
        datos = new Object[capacidadInicial];
        tamanio = 0;
    }

    /** Agrega al final. O(1) amortizado. */
    public void agregar(T dato) {
        if (dato == null) throw new IllegalArgumentException("No se permiten elementos nulos en el vector.");
        if (tamanio == datos.length) redimensionar();
        datos[tamanio++] = dato;
    }

    private void redimensionar() {
        Object[] nuevo = new Object[datos.length * 2];
        for (int i = 0; i < tamanio; i++) nuevo[i] = datos[i];
        datos = nuevo;
    }

    @SuppressWarnings("unchecked")
    public T obtener(int i) {
        if (i < 0 || i >= tamanio) throw new IndexOutOfBoundsException("Índice fuera de rango: " + i);
        return (T) datos[i];
    }

    /** Elimina la posición i desplazando los elementos a la izquierda. O(n). */
    @SuppressWarnings("unchecked")
    public T eliminarEn(int i) {
        if (i < 0 || i >= tamanio) throw new IndexOutOfBoundsException("Índice fuera de rango: " + i);
        T eliminado = (T) datos[i];
        for (int j = i; j < tamanio - 1; j++) datos[j] = datos[j + 1];
        datos[--tamanio] = null; // evita referencias colgantes
        return eliminado;
    }

    public int tamanio() { return tamanio; }
    public int capacidad() { return datos.length; }
    public boolean estaVacio() { return tamanio == 0; }

    public void vaciar() {
        datos = new Object[4];
        tamanio = 0;
    }
}
