package modelo;

/** Solicitud de uso de un equipo de laboratorio (se atiende en orden FIFO). */
public class Solicitud {
    private static int contador = 0;
    private final int id;
    private final int matricula;
    private final String equipo;
    private final String descripcion;

    public Solicitud(int matricula, String equipo, String descripcion) {
        if (equipo == null || equipo.trim().isEmpty()) throw new IllegalArgumentException("El equipo no puede estar vacío.");
        this.id = ++contador;
        this.matricula = matricula;
        this.equipo = equipo.trim().toUpperCase();
        this.descripcion = (descripcion == null || descripcion.trim().isEmpty()) ? "Sin descripción" : descripcion.trim();
    }

    public int getId() { return id; }
    public int getMatricula() { return matricula; }
    public String getEquipo() { return equipo; }
    public String getDescripcion() { return descripcion; }

    public static void reiniciarContador() { contador = 0; }

    @Override
    public String toString() {
        return "S" + id + " (matrícula " + matricula + ", equipo " + equipo + ": " + descripcion + ")";
    }
}
