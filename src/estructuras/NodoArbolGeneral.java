package estructuras;

import modelo.UnidadAcademica;

/** Nodo de árbol general: cada nodo tiene una lista enlazada de hijos. */
public class NodoArbolGeneral {
    private final UnidadAcademica dato;
    private final ListaEnlazada<NodoArbolGeneral> hijos;
    private NodoArbolGeneral padre;

    public NodoArbolGeneral(UnidadAcademica dato) {
        if (dato == null) throw new IllegalArgumentException("El dato del nodo no puede ser nulo.");
        this.dato = dato;
        this.hijos = new ListaEnlazada<>();
    }

    public UnidadAcademica getDato() { return dato; }
    public ListaEnlazada<NodoArbolGeneral> getHijos() { return hijos; }
    public NodoArbolGeneral getPadre() { return padre; }

    public void agregarHijo(NodoArbolGeneral hijo) {
        hijo.padre = this;
        hijos.agregarAlFinal(hijo);
    }

    public boolean eliminarHijo(NodoArbolGeneral hijo) {
        return hijos.eliminar(hijo);
    }
}
