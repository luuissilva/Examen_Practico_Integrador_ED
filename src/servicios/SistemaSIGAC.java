package servicios;

import modelo.Estudiante;
import modelo.Operacion;
import modelo.Solicitud;
import modelo.TipoOperacion;

/**
 * Fachada de integración (M6): todos los módulos comparten los mismos datos.
 *  - Una solicitud (M2) solo se acepta si la matrícula existe (M1) y el equipo existe (M4).
 *  - Registrar/eliminar un estudiante (M1) actualiza vector, lista, ABB y árbol general (M3).
 *  - Las operaciones reversibles se apilan en el historial (M2) y "deshacer" las revierte.
 */
public class SistemaSIGAC {
    private final ServicioAcademico academico = new ServicioAcademico();
    private final ServicioEstudiantes estudiantes = new ServicioEstudiantes(academico);
    private final ServicioAtencion atencion = new ServicioAtencion();
    private final ServicioRedLaboratorio laboratorio = new ServicioRedLaboratorio();
    private final ServicioRecomendaciones social = new ServicioRecomendaciones();

    // ===================== M1 =====================
    public void registrarEstudiante(int matricula, String nombre, String carrera) {
        String oficial = academico.nombreCarrera(carrera);
        Estudiante e = new Estudiante(matricula, nombre, oficial == null ? (carrera == null ? "" : carrera) : oficial);
        estudiantes.registrar(e);
        atencion.registrarOperacion(Operacion.deEstudiante(TipoOperacion.REGISTRAR_ESTUDIANTE, e, "Registrar " + e));
    }

    public Estudiante eliminarEstudiante(int matricula) {
        if (atencion.haySolicitudesDe(matricula))
            throw new IllegalStateException("El estudiante " + matricula + " tiene solicitudes pendientes; atiéndalas antes de eliminarlo.");
        Estudiante e = estudiantes.eliminar(matricula);
        atencion.registrarOperacion(Operacion.deEstudiante(TipoOperacion.ELIMINAR_ESTUDIANTE, e, "Eliminar " + e));
        return e;
    }

    public void actualizarNombre(int matricula, String nuevo) {
        String anterior = estudiantes.actualizarNombre(matricula, nuevo);
        atencion.registrarOperacion(Operacion.deActualizacion(estudiantes.buscarABB(matricula), anterior));
    }

    // ===================== M2 =====================
    public Solicitud registrarSolicitud(int matricula, String equipo, String descripcion) {
        if (estudiantes.buscarABB(matricula) == null)
            throw new IllegalArgumentException("No existe la matrícula " + matricula + ".");
        if (!laboratorio.existeEquipo(equipo))
            throw new IllegalArgumentException("El equipo " + (equipo == null ? "null" : equipo.trim().toUpperCase()) + " no existe en la red.");
        Solicitud s = new Solicitud(matricula, equipo, descripcion);
        atencion.registrarSolicitud(s);
        return s;
    }

    public Solicitud atenderSolicitud() {
        Solicitud s = atencion.atender();
        atencion.registrarOperacion(Operacion.deSolicitud(s));
        return s;
    }

    /**
     * Deshace la última operación reversible (LIFO).
     * Si la operación ya no puede revertirse (p. ej. se eliminó el equipo), se descarta y se informa.
     */
    public String deshacer() {
        Operacion op = atencion.sacarUltimaOperacion(); // lanza excepción si la pila está vacía
        try {
            switch (op.getTipo()) {
                case REGISTRAR_ESTUDIANTE:
                    if (atencion.haySolicitudesDe(op.getEstudiante().getMatricula()))
                        throw new IllegalStateException("el estudiante tiene solicitudes pendientes");
                    estudiantes.eliminar(op.getEstudiante().getMatricula());
                    break;
                case ELIMINAR_ESTUDIANTE:
                    estudiantes.registrar(op.getEstudiante());
                    break;
                case ACTUALIZAR_ESTUDIANTE:
                    estudiantes.actualizarNombre(op.getEstudiante().getMatricula(), op.getNombreAnterior());
                    break;
                case ATENDER_SOLICITUD:
                    atencion.devolverAlFrente(op.getSolicitud());
                    break;
                case AGREGAR_CONEXION:
                    laboratorio.eliminarConexion(op.getOrigen(), op.getDestino());
                    break;
                case ELIMINAR_CONEXION:
                    laboratorio.agregarConexion(op.getOrigen(), op.getDestino(), op.getPeso());
                    break;
                case AGREGAR_AMISTAD:
                    social.eliminarAmistad(op.getOrigen(), op.getDestino());
                    break;
                default:
                    throw new IllegalStateException("tipo de operación desconocido");
            }
        } catch (RuntimeException ex) {
            throw new IllegalStateException("No se pudo deshacer [" + op + "]: " + ex.getMessage() + ". La operación se descartó.");
        }
        return "Deshecho: " + op;
    }

    // ===================== M4 =====================
    public void agregarEquipo(String nombre) { laboratorio.agregarEquipo(nombre); }

    public void eliminarEquipo(String nombre) {
        if (nombre != null && atencion.haySolicitudesParaEquipo(nombre))
            throw new IllegalStateException("El equipo tiene solicitudes pendientes; atiéndalas antes de eliminarlo.");
        laboratorio.eliminarEquipo(nombre);
    }

    public void agregarConexion(String o, String d, int peso) {
        laboratorio.agregarConexion(o, d, peso);
        atencion.registrarOperacion(Operacion.deRelacion(TipoOperacion.AGREGAR_CONEXION, o.trim().toUpperCase(),
                d.trim().toUpperCase(), peso, "Conexión " + o.trim().toUpperCase() + " -> " + d.trim().toUpperCase() + " (" + peso + ")"));
    }

    public void eliminarConexion(String o, String d) {
        int peso = laboratorio.eliminarConexion(o, d);
        atencion.registrarOperacion(Operacion.deRelacion(TipoOperacion.ELIMINAR_CONEXION, o.trim().toUpperCase(),
                d.trim().toUpperCase(), peso, "Quitar conexión " + o.trim().toUpperCase() + " -> " + d.trim().toUpperCase() + " (" + peso + ")"));
    }

    // ===================== M5 =====================
    public void agregarUsuario(String nombre) { social.agregarUsuario(nombre); }

    public void agregarAmistad(String a, String b) {
        social.agregarAmistad(a, b);
        atencion.registrarOperacion(Operacion.deRelacion(TipoOperacion.AGREGAR_AMISTAD, a.trim(), b.trim(), 0,
                "Amistad " + a.trim() + " - " + b.trim()));
    }

    public void eliminarAmistad(String a, String b) { social.eliminarAmistad(a, b); }

    // ===================== Caso integrador =====================
    /**
     * Carga los datos del caso integrador obligatorio. Es una carga inicial,
     * por eso al final se limpia el historial (no se puede "deshacer" la carga).
     */
    public void cargarCasoIntegrador() {
        reiniciar();
        academico.agregarFacultad("FISEI");
        academico.agregarCarrera("FISEI", "Software");
        int[] mats = {50, 30, 70, 20, 40, 60, 80};
        String[] nombres = {"Andrea Lopez", "Bruno Mena", "Camila Ortiz", "Daniel Paredes",
                "Erika Quispe", "Fernando Ruiz", "Gabriela Sanchez"};
        for (int i = 0; i < mats.length; i++) registrarEstudiante(mats[i], nombres[i], "Software");

        String[] equipos = {"PC1", "SW1", "SRV1", "PC2", "PC3", "PC4"};
        for (String eq : equipos) agregarEquipo(eq);
        agregarConexion("PC1", "SW1", 1);   // vecinos de PC1 en orden: SW1, PC3
        agregarConexion("SW1", "SRV1", 2);
        agregarConexion("SRV1", "PC2", 3);
        agregarConexion("PC1", "PC3", 4);   // PC4 queda aislado

        String[] usuarios = {"Ana", "Beto", "Carla", "Diego", "Elena"};
        for (String u : usuarios) agregarUsuario(u);
        agregarAmistad("Ana", "Beto");
        agregarAmistad("Ana", "Carla");
        agregarAmistad("Beto", "Diego");
        agregarAmistad("Carla", "Diego");
        agregarAmistad("Carla", "Elena");

        atencion.limpiarHistorial();
    }

    public void reiniciar() {
        estudiantes.reiniciar();
        academico.reiniciar();
        atencion.reiniciar();
        laboratorio.reiniciar();
        social.reiniciar();
        Solicitud.reiniciarContador();
    }

    public ServicioAcademico getAcademico() { return academico; }
    public ServicioEstudiantes getEstudiantes() { return estudiantes; }
    public ServicioAtencion getAtencion() { return atencion; }
    public ServicioRedLaboratorio getLaboratorio() { return laboratorio; }
    public ServicioRecomendaciones getSocial() { return social; }
}
