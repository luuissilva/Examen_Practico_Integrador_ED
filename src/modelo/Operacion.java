package modelo;

/** Operación reversible: guarda los datos necesarios para deshacerla. */
public class Operacion {
    private final TipoOperacion tipo;
    private final String descripcion;
    private Estudiante estudiante;     // estudiante registrado/eliminado
    private String nombreAnterior;     // para deshacer una actualización
    private Solicitud solicitud;       // solicitud atendida
    private String origen;             // conexión / amistad
    private String destino;
    private int peso;

    public Operacion(TipoOperacion tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public static Operacion deEstudiante(TipoOperacion tipo, Estudiante e, String desc) {
        Operacion op = new Operacion(tipo, desc);
        op.estudiante = e;
        return op;
    }

    public static Operacion deActualizacion(Estudiante e, String nombreAnterior) {
        Operacion op = new Operacion(TipoOperacion.ACTUALIZAR_ESTUDIANTE,
                "Actualizar nombre de " + e.getMatricula() + " (antes: " + nombreAnterior + ")");
        op.estudiante = e;
        op.nombreAnterior = nombreAnterior;
        return op;
    }

    public static Operacion deSolicitud(Solicitud s) {
        Operacion op = new Operacion(TipoOperacion.ATENDER_SOLICITUD, "Atender solicitud " + s);
        op.solicitud = s;
        return op;
    }

    public static Operacion deRelacion(TipoOperacion tipo, String origen, String destino, int peso, String desc) {
        Operacion op = new Operacion(tipo, desc);
        op.origen = origen;
        op.destino = destino;
        op.peso = peso;
        return op;
    }

    public TipoOperacion getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public Estudiante getEstudiante() { return estudiante; }
    public String getNombreAnterior() { return nombreAnterior; }
    public Solicitud getSolicitud() { return solicitud; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public int getPeso() { return peso; }

    @Override
    public String toString() { return tipo + " -> " + descripcion; }
}
