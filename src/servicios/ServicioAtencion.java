package servicios;

import estructuras.Cola;
import estructuras.Pila;
import modelo.Operacion;
import modelo.Solicitud;

/** M2 – Cola FIFO de solicitudes de laboratorio y pila LIFO de historial reversible. */
public class ServicioAtencion {
    private final Cola<Solicitud> solicitudes = new Cola<>();
    private final Pila<Operacion> historial = new Pila<>();

    public void registrarSolicitud(Solicitud s) {
        if (s == null) throw new IllegalArgumentException("La solicitud no puede ser nula.");
        solicitudes.encolar(s);
    }

    /** Atiende la solicitud más antigua (FIFO). */
    public Solicitud atender() {
        return solicitudes.desencolar(); // lanza excepción si está vacía
    }

    public void devolverAlFrente(Solicitud s) { solicitudes.devolverAlFrente(s); }

    /** ¿Hay solicitudes pendientes de esa matrícula o de ese equipo? */
    public boolean haySolicitudesDe(int matricula) {
        Cola<Solicitud> aux = new Cola<>();
        boolean hay = false;
        while (!solicitudes.estaVacia()) {
            Solicitud s = solicitudes.desencolar();
            if (s.getMatricula() == matricula) hay = true;
            aux.encolar(s);
        }
        while (!aux.estaVacia()) solicitudes.encolar(aux.desencolar());
        return hay;
    }

    public boolean haySolicitudesParaEquipo(String equipo) {
        Cola<Solicitud> aux = new Cola<>();
        boolean hay = false;
        while (!solicitudes.estaVacia()) {
            Solicitud s = solicitudes.desencolar();
            if (s.getEquipo().equalsIgnoreCase(equipo.trim())) hay = true;
            aux.encolar(s);
        }
        while (!aux.estaVacia()) solicitudes.encolar(aux.desencolar());
        return hay;
    }

    public void registrarOperacion(Operacion op) { historial.apilar(op); }

    public Operacion sacarUltimaOperacion() { return historial.desapilar(); }

    public String verSolicitudes() { return solicitudes.toString(); }
    public String verHistorial() { return historial.toString(); }
    public int pendientes() { return solicitudes.tamanio(); }
    public int operacionesEnHistorial() { return historial.tamanio(); }
    public Solicitud verFrente() { return solicitudes.verFrente(); }

    public void limpiarHistorial() { historial.vaciar(); }

    public void reiniciar() {
        solicitudes.vaciar();
        historial.vaciar();
    }
}
