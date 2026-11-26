package modelo;

/** Dato que guarda cada nodo del árbol general académico. */
public class UnidadAcademica {
    private final String nombre;
    private final TipoUnidad tipo;
    private final int matricula; // solo se usa cuando tipo == ESTUDIANTE

    public UnidadAcademica(String nombre, TipoUnidad tipo) {
        this(nombre, tipo, -1);
    }

    public UnidadAcademica(String nombre, TipoUnidad tipo, int matricula) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre de la unidad no puede estar vacío.");
        if (tipo == null) throw new IllegalArgumentException("El tipo de unidad no puede ser nulo.");
        this.nombre = nombre.trim();
        this.tipo = tipo;
        this.matricula = matricula;
    }

    public String getNombre() { return nombre; }
    public TipoUnidad getTipo() { return tipo; }
    public int getMatricula() { return matricula; }

    @Override
    public String toString() {
        if (tipo == TipoUnidad.ESTUDIANTE) return "Estudiante " + matricula + " - " + nombre;
        return tipo + ": " + nombre;
    }
}
