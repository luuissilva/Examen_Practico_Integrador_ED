package servicios;

import estructuras.GrafoSocial;
import estructuras.GrafoSocial.Usuario;
import estructuras.ListaEnlazada;
import estructuras.Vector;
import modelo.Recomendacion;

/** M5 – Recomendación de contactos de segundo grado (amigos de mis amigos). */
public class ServicioRecomendaciones {
    private final GrafoSocial red = new GrafoSocial();

    public void agregarUsuario(String nombre) { red.agregarUsuario(nombre); }
    public void agregarAmistad(String a, String b) { red.agregarAmistad(a, b); }
    public void eliminarAmistad(String a, String b) { red.eliminarAmistad(a, b); }
    public String mostrar() { return red.mostrar(); }

    /**
     * Algoritmo:
     *  1. Para cada amigo directo F del usuario U,
     *  2. para cada amigo G de F:
     *     - se EXCLUYE G si es el propio U,
     *     - se EXCLUYE G si ya es amigo directo de U,
     *     - en otro caso se suma 1 a los "amigos en común" de G.
     *  3. Se ordena por amigos en común (descendente); empate: nombre (ascendente).
     * Complejidad: O(d² · (d + R)) con d = grado máximo y R = candidatos; ordenamiento O(R²).
     */
    public Vector<Recomendacion> recomendar(String nombre) {
        Usuario u = red.obtenerExistente(nombre);
        ListaEnlazada<Recomendacion> candidatos = new ListaEnlazada<>();
        for (Usuario amigo : u.getAmigos()) {
            for (Usuario g : amigo.getAmigos()) {
                if (g == u) continue;            // excluye al propio usuario
                if (u.esAmigoDe(g)) continue;    // excluye amigos directos
                Recomendacion r = buscar(candidatos, g.getNombre());
                if (r == null) {
                    r = new Recomendacion(g.getNombre());
                    candidatos.agregarAlFinal(r);
                }
                r.incrementar();
            }
        }
        Vector<Recomendacion> v = new Vector<>();
        for (Recomendacion r : candidatos) v.agregar(r);
        ordenar(v);
        return v;
    }

    private Recomendacion buscar(ListaEnlazada<Recomendacion> l, String nombre) {
        for (Recomendacion r : l) if (r.getNombre().equals(nombre)) return r;
        return null;
    }

    /** Ordenamiento por inserción implementado manualmente sobre el vector. */
    private void ordenar(Vector<Recomendacion> v) {
        int n = v.tamanio();
        Recomendacion[] a = new Recomendacion[n];
        for (int i = 0; i < n; i++) a[i] = v.obtener(i);
        for (int i = 1; i < n; i++) {
            Recomendacion clave = a[i];
            int j = i - 1;
            while (j >= 0 && vaAntes(clave, a[j])) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = clave;
        }
        v.vaciar();
        for (Recomendacion r : a) v.agregar(r);
    }

    private boolean vaAntes(Recomendacion x, Recomendacion y) {
        if (x.getAmigosEnComun() != y.getAmigosEnComun()) return x.getAmigosEnComun() > y.getAmigosEnComun();
        return x.getNombre().compareTo(y.getNombre()) < 0;
    }

    public String recomendarTexto(String nombre) {
        Vector<Recomendacion> v = recomendar(nombre);
        String n = GrafoSocial.normalizar(nombre);
        if (v.estaVacio()) return "No hay contactos de segundo grado para recomendar a " + n + ".\n";
        StringBuilder sb = new StringBuilder("Recomendaciones para " + n + ":\n");
        for (int i = 0; i < v.tamanio(); i++) sb.append("  ").append(i + 1).append(". ").append(v.obtener(i)).append("\n");
        return sb.toString();
    }

    public GrafoSocial getRed() { return red; }
    public void reiniciar() { red.vaciar(); }
}
