package estructuras;

import modelo.TipoUnidad;
import modelo.UnidadAcademica;

/** Árbol general (n-ario) para la jerarquía Universidad → Facultad → Carrera → Estudiantes. */
public class ArbolGeneral {
    private final NodoArbolGeneral raiz;

    public ArbolGeneral(UnidadAcademica datoRaiz) {
        this.raiz = new NodoArbolGeneral(datoRaiz);
    }

    public NodoArbolGeneral getRaiz() { return raiz; }

    /** Busca (en preorden) un nodo por nombre y tipo, sin distinguir mayúsculas. O(n). */
    public NodoArbolGeneral buscar(String nombre, TipoUnidad tipo) {
        if (nombre == null) return null;
        return buscarRec(raiz, nombre.trim(), tipo);
    }

    private NodoArbolGeneral buscarRec(NodoArbolGeneral nodo, String nombre, TipoUnidad tipo) {
        UnidadAcademica u = nodo.getDato();
        if (u.getTipo() == tipo && u.getNombre().equalsIgnoreCase(nombre)) return nodo;
        for (NodoArbolGeneral h : nodo.getHijos()) {
            NodoArbolGeneral r = buscarRec(h, nombre, tipo);
            if (r != null) return r;
        }
        return null;
    }

    /** Busca el nodo hoja de un estudiante por matrícula. O(n). */
    public NodoArbolGeneral buscarEstudiante(int matricula) {
        return buscarEstRec(raiz, matricula);
    }

    private NodoArbolGeneral buscarEstRec(NodoArbolGeneral nodo, int matricula) {
        UnidadAcademica u = nodo.getDato();
        if (u.getTipo() == TipoUnidad.ESTUDIANTE && u.getMatricula() == matricula) return nodo;
        for (NodoArbolGeneral h : nodo.getHijos()) {
            NodoArbolGeneral r = buscarEstRec(h, matricula);
            if (r != null) return r;
        }
        return null;
    }

    /** Cuenta nodos de un tipo bajo un nodo dado. */
    public int contar(NodoArbolGeneral nodo, TipoUnidad tipo) {
        int c = nodo.getDato().getTipo() == tipo ? 1 : 0;
        for (NodoArbolGeneral h : nodo.getHijos()) c += contar(h, tipo);
        return c;
    }

    /** Representación con sangría (recorrido en preorden). */
    public String mostrar() {
        StringBuilder sb = new StringBuilder();
        mostrarRec(raiz, "", true, true, sb);
        return sb.toString();
    }

    private void mostrarRec(NodoArbolGeneral nodo, String prefijo, boolean ultimo, boolean esRaiz, StringBuilder sb) {
        if (esRaiz) {
            sb.append(nodo.getDato()).append("\n");
        } else {
            sb.append(prefijo).append(ultimo ? "└── " : "├── ").append(nodo.getDato()).append("\n");
        }
        String nuevoPrefijo = esRaiz ? "" : prefijo + (ultimo ? "    " : "│   ");
        int i = 0, n = nodo.getHijos().tamanio();
        for (NodoArbolGeneral h : nodo.getHijos()) {
            i++;
            mostrarRec(h, nuevoPrefijo, i == n, false, sb);
        }
    }
}
