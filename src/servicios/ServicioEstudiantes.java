package servicios;

import estructuras.ListaEnlazada;
import estructuras.Vector;
import modelo.Estudiante;

/**
 * M1 – Gestión de estudiantes con VECTOR (registro principal) y LISTA ENLAZADA
 * (copia enlazada). Ambas se mantienen sincronizadas con el ABB y el árbol
 * general a través de ServicioAcademico.
 */
public class ServicioEstudiantes {
    private final Vector<Estudiante> vector = new Vector<>();
    private final ListaEnlazada<Estudiante> lista = new ListaEnlazada<>();
    private final ServicioAcademico academico;
    private int comparacionesVector;
    private int comparacionesLista;

    public ServicioEstudiantes(ServicioAcademico academico) {
        if (academico == null) throw new IllegalArgumentException("Servicio académico nulo.");
        this.academico = academico;
    }

    /** CREATE. Rechaza nulos, duplicados y carreras inexistentes. */
    public void registrar(Estudiante e) {
        if (e == null) throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        if (academico.getIndice().buscar(e.getMatricula()) != null)
            throw new IllegalArgumentException("Ya existe un estudiante con la matrícula " + e.getMatricula() + ".");
        if (!academico.existeCarrera(e.getCarrera()))
            throw new IllegalArgumentException("La carrera " + e.getCarrera() + " no existe. Regístrela primero en M3.");
        academico.vincularEstudiante(e); // ABB + árbol general
        vector.agregar(e);
        lista.agregarAlFinal(e);
    }

    /** READ secuencial en el vector. O(n). */
    public Estudiante buscarSecuencial(int matricula) {
        comparacionesVector = 0;
        for (int i = 0; i < vector.tamanio(); i++) {
            comparacionesVector++;
            if (vector.obtener(i).getMatricula() == matricula) return vector.obtener(i);
        }
        return null;
    }

    /** READ secuencial en la lista enlazada. O(n). */
    public Estudiante buscarEnLista(int matricula) {
        comparacionesLista = 0;
        for (Estudiante e : lista) {
            comparacionesLista++;
            if (e.getMatricula() == matricula) return e;
        }
        return null;
    }

    /** READ en el ABB. O(log n) promedio. */
    public Estudiante buscarABB(int matricula) {
        return academico.getIndice().buscar(matricula);
    }

    /** Compara el número de comparaciones de cada estrategia. */
    public String compararBusquedas(int matricula) {
        Estudiante v = buscarSecuencial(matricula);
        buscarEnLista(matricula);
        buscarABB(matricula);
        int cAbb = academico.getIndice().getUltimasComparaciones();
        StringBuilder sb = new StringBuilder();
        sb.append(v == null ? "No existe la matrícula " + matricula + ".\n" : "Encontrado: " + v + "\n");
        sb.append(String.format("  Secuencial en vector : %d comparaciones  (O(n))%n", comparacionesVector));
        sb.append(String.format("  Secuencial en lista  : %d comparaciones  (O(n))%n", comparacionesLista));
        sb.append(String.format("  Búsqueda en ABB      : %d comparaciones  (O(log n) promedio, altura=%d)%n",
                cAbb, academico.getIndice().altura()));
        return sb.toString();
    }

    /** UPDATE del nombre. Devuelve el nombre anterior. */
    public String actualizarNombre(int matricula, String nuevoNombre) {
        Estudiante e = buscarABB(matricula);
        if (e == null) throw new IllegalArgumentException("No existe la matrícula " + matricula + ".");
        String anterior = e.getNombre();
        e.setNombre(nuevoNombre); // valida vacío; el objeto es compartido por vector, lista y ABB
        academico.actualizarHoja(e);
        return anterior;
    }

    /** DELETE seguro: valida estructura vacía y existencia; elimina de TODAS las estructuras. */
    public Estudiante eliminar(int matricula) {
        if (vector.estaVacio()) throw new IllegalStateException("No hay estudiantes registrados.");
        int pos = -1;
        for (int i = 0; i < vector.tamanio(); i++) {
            if (vector.obtener(i).getMatricula() == matricula) { pos = i; break; }
        }
        if (pos == -1) throw new IllegalArgumentException("No existe la matrícula " + matricula + ".");
        Estudiante e = vector.eliminarEn(pos);
        lista.eliminar(e);
        academico.desvincularEstudiante(matricula);
        return e;
    }

    public String listarVector() {
        if (vector.estaVacio()) return "(no hay estudiantes)\n";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vector.tamanio(); i++) sb.append("[").append(i).append("] ").append(vector.obtener(i)).append("\n");
        sb.append("Tamaño: ").append(vector.tamanio()).append(" | Capacidad del vector: ").append(vector.capacidad()).append("\n");
        return sb.toString();
    }

    public String listarLista() {
        if (lista.estaVacia()) return "(no hay estudiantes)\n";
        StringBuilder sb = new StringBuilder("INICIO -> ");
        for (Estudiante e : lista) sb.append(e.getMatricula()).append(" -> ");
        return sb.append("null\n").toString();
    }

    public int cantidad() { return vector.tamanio(); }
    public int getComparacionesVector() { return comparacionesVector; }
    public int getComparacionesLista() { return comparacionesLista; }

    public void reiniciar() {
        vector.vaciar();
        lista.vaciar();
    }
}
