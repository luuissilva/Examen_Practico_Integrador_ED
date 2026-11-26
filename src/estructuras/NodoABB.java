package estructuras;

import modelo.Estudiante;

/** Nodo del ABB: la clave es la matrícula del estudiante. */
public class NodoABB {
    Estudiante dato;
    NodoABB izquierdo;
    NodoABB derecho;

    public NodoABB(Estudiante dato) {
        this.dato = dato;
    }

    public Estudiante getDato() { return dato; }
    public NodoABB getIzquierdo() { return izquierdo; }
    public NodoABB getDerecho() { return derecho; }
}
