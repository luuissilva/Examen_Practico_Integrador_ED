package servicios;

import estructuras.ABB;
import estructuras.ArbolGeneral;
import estructuras.ListaEnlazada;
import estructuras.NodoArbolGeneral;
import modelo.Estudiante;
import modelo.TipoUnidad;
import modelo.UnidadAcademica;

/** M3 – Organización académica: árbol general (jerarquía) + ABB (índice por matrícula). */
public class ServicioAcademico {
    public static final String UNIVERSIDAD = "Universidad Técnica de Ambato";

    private ArbolGeneral jerarquia;
    private final ABB indice;

    public ServicioAcademico() {
        jerarquia = new ArbolGeneral(new UnidadAcademica(UNIVERSIDAD, TipoUnidad.UNIVERSIDAD));
        indice = new ABB();
    }

    public void agregarFacultad(String nombre) {
        validarTexto(nombre, "facultad");
        if (jerarquia.buscar(nombre, TipoUnidad.FACULTAD) != null)
            throw new IllegalArgumentException("La facultad " + nombre.trim() + " ya existe.");
        jerarquia.getRaiz().agregarHijo(new NodoArbolGeneral(new UnidadAcademica(nombre, TipoUnidad.FACULTAD)));
    }

    public void agregarCarrera(String facultad, String carrera) {
        validarTexto(facultad, "facultad");
        validarTexto(carrera, "carrera");
        NodoArbolGeneral f = jerarquia.buscar(facultad, TipoUnidad.FACULTAD);
        if (f == null) throw new IllegalArgumentException("La facultad " + facultad.trim() + " no existe.");
        if (jerarquia.buscar(carrera, TipoUnidad.CARRERA) != null)
            throw new IllegalArgumentException("La carrera " + carrera.trim() + " ya existe.");
        f.agregarHijo(new NodoArbolGeneral(new UnidadAcademica(carrera, TipoUnidad.CARRERA)));
    }

    public boolean existeCarrera(String carrera) {
        return carrera != null && jerarquia.buscar(carrera, TipoUnidad.CARRERA) != null;
    }

    /** Nombre oficial de la carrera (como fue registrada). */
    public String nombreCarrera(String carrera) {
        NodoArbolGeneral c = jerarquia.buscar(carrera, TipoUnidad.CARRERA);
        return c == null ? null : c.getDato().getNombre();
    }

    /** Cuelga al estudiante como hoja de su carrera e inserta en el ABB. */
    public void vincularEstudiante(Estudiante e) {
        NodoArbolGeneral c = jerarquia.buscar(e.getCarrera(), TipoUnidad.CARRERA);
        if (c == null) throw new IllegalArgumentException("La carrera " + e.getCarrera() + " no existe en la jerarquía.");
        indice.insertar(e); // lanza excepción si la matrícula está duplicada
        c.agregarHijo(new NodoArbolGeneral(new UnidadAcademica(e.getNombre(), TipoUnidad.ESTUDIANTE, e.getMatricula())));
    }

    /** Quita al estudiante del árbol general y del ABB. */
    public void desvincularEstudiante(int matricula) {
        NodoArbolGeneral hoja = jerarquia.buscarEstudiante(matricula);
        if (hoja != null) hoja.getPadre().eliminarHijo(hoja);
        indice.eliminar(matricula);
    }

    /** Actualiza el nombre que se ve en la hoja del árbol general. */
    public void actualizarHoja(Estudiante e) {
        NodoArbolGeneral hoja = jerarquia.buscarEstudiante(e.getMatricula());
        if (hoja == null) return;
        NodoArbolGeneral padre = hoja.getPadre();
        padre.eliminarHijo(hoja);
        padre.agregarHijo(new NodoArbolGeneral(new UnidadAcademica(e.getNombre(), TipoUnidad.ESTUDIANTE, e.getMatricula())));
    }

    public ListaEnlazada<String> listarCarreras() {
        ListaEnlazada<String> r = new ListaEnlazada<>();
        for (NodoArbolGeneral f : jerarquia.getRaiz().getHijos())
            for (NodoArbolGeneral c : f.getHijos())
                r.agregarAlFinal(c.getDato().getNombre() + " (" + f.getDato().getNombre() + ")");
        return r;
    }

    public String mostrarJerarquia() {
        return jerarquia.mostrar()
                + "Facultades: " + jerarquia.contar(jerarquia.getRaiz(), TipoUnidad.FACULTAD)
                + " | Carreras: " + jerarquia.contar(jerarquia.getRaiz(), TipoUnidad.CARRERA)
                + " | Estudiantes: " + jerarquia.contar(jerarquia.getRaiz(), TipoUnidad.ESTUDIANTE) + "\n";
    }

    public ABB getIndice() { return indice; }
    public ArbolGeneral getJerarquia() { return jerarquia; }

    public void reiniciar() {
        jerarquia = new ArbolGeneral(new UnidadAcademica(UNIVERSIDAD, TipoUnidad.UNIVERSIDAD));
        indice.vaciar();
    }

    private static void validarTexto(String t, String campo) {
        if (t == null || t.trim().isEmpty()) throw new IllegalArgumentException("El nombre de la " + campo + " no puede estar vacío.");
    }
}
