package estructuras;

import modelo.Estudiante;

/**
 * Árbol Binario de Búsqueda indexado por matrícula.
 *
 * POLÍTICA DE ELIMINACIÓN (documentada para pruebas reproducibles):
 *  - Hoja: se quita el nodo.
 *  - Un hijo: el hijo ocupa el lugar del nodo.
 *  - Dos hijos: se reemplaza por el SUCESOR INORDEN (mínimo del subárbol derecho)
 *    y luego se elimina ese sucesor de su posición original.
 * No se permiten claves duplicadas.
 */
public class ABB {
    private NodoABB raiz;
    private int tamanio;
    private int ultimasComparaciones; // para comparar con la búsqueda secuencial

    /** Inserta un estudiante. O(h): O(log n) promedio, O(n) peor caso (árbol degenerado). */
    public void insertar(Estudiante e) {
        if (e == null) throw new IllegalArgumentException("No se puede insertar un estudiante nulo en el ABB.");
        raiz = insertarRec(raiz, e);
        tamanio++;
    }

    private NodoABB insertarRec(NodoABB nodo, Estudiante e) {
        if (nodo == null) return new NodoABB(e);
        int clave = e.getMatricula();
        int actual = nodo.dato.getMatricula();
        if (clave < actual) nodo.izquierdo = insertarRec(nodo.izquierdo, e);
        else if (clave > actual) nodo.derecho = insertarRec(nodo.derecho, e);
        else throw new IllegalArgumentException("Matrícula duplicada en el ABB: " + clave);
        return nodo;
    }

    /** Búsqueda iterativa. O(h). Registra el número de comparaciones. */
    public Estudiante buscar(int matricula) {
        ultimasComparaciones = 0;
        NodoABB actual = raiz;
        while (actual != null) {
            ultimasComparaciones++;
            int m = actual.dato.getMatricula();
            if (matricula == m) return actual.dato;
            actual = (matricula < m) ? actual.izquierdo : actual.derecho;
        }
        return null;
    }

    /** Elimina por matrícula. O(h). Devuelve true si existía. */
    public boolean eliminar(int matricula) {
        if (buscar(matricula) == null) return false;
        raiz = eliminarRec(raiz, matricula);
        tamanio--;
        return true;
    }

    private NodoABB eliminarRec(NodoABB nodo, int matricula) {
        if (nodo == null) return null;
        int m = nodo.dato.getMatricula();
        if (matricula < m) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, matricula);
        } else if (matricula > m) {
            nodo.derecho = eliminarRec(nodo.derecho, matricula);
        } else {
            // Caso 1 y 2: hoja o un solo hijo
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            // Caso 3: dos hijos -> sucesor inorden (mínimo del subárbol derecho)
            NodoABB sucesor = minimo(nodo.derecho);
            nodo.dato = sucesor.dato;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.dato.getMatricula());
        }
        return nodo;
    }

    private NodoABB minimo(NodoABB nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    // ---------------- Recorridos: O(n) ----------------
    public ListaEnlazada<Estudiante> preorden() {
        ListaEnlazada<Estudiante> l = new ListaEnlazada<>();
        preordenRec(raiz, l);
        return l;
    }

    private void preordenRec(NodoABB n, ListaEnlazada<Estudiante> l) {
        if (n == null) return;
        l.agregarAlFinal(n.dato);
        preordenRec(n.izquierdo, l);
        preordenRec(n.derecho, l);
    }

    public ListaEnlazada<Estudiante> inorden() {
        ListaEnlazada<Estudiante> l = new ListaEnlazada<>();
        inordenRec(raiz, l);
        return l;
    }

    private void inordenRec(NodoABB n, ListaEnlazada<Estudiante> l) {
        if (n == null) return;
        inordenRec(n.izquierdo, l);
        l.agregarAlFinal(n.dato);
        inordenRec(n.derecho, l);
    }

    public ListaEnlazada<Estudiante> posorden() {
        ListaEnlazada<Estudiante> l = new ListaEnlazada<>();
        posordenRec(raiz, l);
        return l;
    }

    private void posordenRec(NodoABB n, ListaEnlazada<Estudiante> l) {
        if (n == null) return;
        posordenRec(n.izquierdo, l);
        posordenRec(n.derecho, l);
        l.agregarAlFinal(n.dato);
    }

    public int altura() { return alturaRec(raiz); }

    private int alturaRec(NodoABB n) {
        if (n == null) return 0;
        return 1 + Math.max(alturaRec(n.izquierdo), alturaRec(n.derecho));
    }

    /** Dibujo lateral del árbol (derecha arriba, izquierda abajo). */
    public String dibujar() {
        if (raiz == null) return "(ABB vacío)\n";
        StringBuilder sb = new StringBuilder();
        dibujarRec(raiz, 0, sb);
        return sb.toString();
    }

    private void dibujarRec(NodoABB n, int nivel, StringBuilder sb) {
        if (n == null) return;
        dibujarRec(n.derecho, nivel + 1, sb);
        for (int i = 0; i < nivel; i++) sb.append("      ");
        sb.append(n.dato.getMatricula()).append("\n");
        dibujarRec(n.izquierdo, nivel + 1, sb);
    }

    /** Solo las matrículas, separadas por coma. */
    public static String matriculas(ListaEnlazada<Estudiante> l) {
        StringBuilder sb = new StringBuilder();
        for (Estudiante e : l) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.getMatricula());
        }
        return sb.length() == 0 ? "(vacío)" : sb.toString();
    }

    public NodoABB getRaiz() { return raiz; }
    public int tamanio() { return tamanio; }
    public boolean estaVacio() { return raiz == null; }
    public int getUltimasComparaciones() { return ultimasComparaciones; }
    public void vaciar() { raiz = null; tamanio = 0; }
}
