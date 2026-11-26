package estructuras;

/**
 * Grafo social NO DIRIGIDO (la amistad es mutua). Cada usuario guarda su lista
 * enlazada de amigos en orden de registro. Los nombres se comparan sin
 * distinguir mayúsculas y se guardan con la primera letra en mayúscula.
 */
public class GrafoSocial {

    /** Usuario = vértice del grafo social. */
    public static class Usuario {
        private final String nombre;
        private final ListaEnlazada<Usuario> amigos = new ListaEnlazada<>();
        Usuario(String nombre) { this.nombre = nombre; }
        public String getNombre() { return nombre; }
        public ListaEnlazada<Usuario> getAmigos() { return amigos; }
        public boolean esAmigoDe(Usuario otro) { return amigos.contiene(otro); }
        @Override public String toString() { return nombre; }
    }

    private final ListaEnlazada<Usuario> usuarios = new ListaEnlazada<>();

    public static String normalizar(String n) {
        if (n == null || n.trim().isEmpty()) throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        String t = n.trim().toLowerCase();
        return Character.toUpperCase(t.charAt(0)) + t.substring(1);
    }

    public void agregarUsuario(String nombre) {
        String n = normalizar(nombre);
        if (buscar(n) != null) throw new IllegalArgumentException("El usuario " + n + " ya existe.");
        usuarios.agregarAlFinal(new Usuario(n));
    }

    public Usuario buscar(String nombre) {
        if (nombre == null) return null;
        for (Usuario u : usuarios) if (u.getNombre().equalsIgnoreCase(nombre.trim())) return u;
        return null;
    }

    public Usuario obtenerExistente(String nombre) {
        Usuario u = buscar(normalizar(nombre));
        if (u == null) throw new IllegalArgumentException("El usuario " + normalizar(nombre) + " no existe.");
        return u;
    }

    /** Amistad mutua a <-> b. O(U + grado). */
    public void agregarAmistad(String a, String b) {
        Usuario ua = obtenerExistente(a);
        Usuario ub = obtenerExistente(b);
        if (ua == ub) throw new IllegalArgumentException("Un usuario no puede ser amigo de sí mismo.");
        if (ua.esAmigoDe(ub)) throw new IllegalArgumentException(ua + " y " + ub + " ya son amigos.");
        ua.amigos.agregarAlFinal(ub);
        ub.amigos.agregarAlFinal(ua);
    }

    public void eliminarAmistad(String a, String b) {
        Usuario ua = obtenerExistente(a);
        Usuario ub = obtenerExistente(b);
        if (!ua.esAmigoDe(ub)) throw new IllegalArgumentException(ua + " y " + ub + " no son amigos.");
        ua.amigos.eliminar(ub);
        ub.amigos.eliminar(ua);
    }

    public ListaEnlazada<Usuario> getUsuarios() { return usuarios; }

    public String mostrar() {
        if (usuarios.estaVacia()) return "(red social vacía)\n";
        StringBuilder sb = new StringBuilder();
        for (Usuario u : usuarios) {
            sb.append(String.format("%-8s amigos: ", u.getNombre()));
            sb.append(u.amigos.estaVacia() ? "(ninguno)" : u.amigos.toString()).append("\n");
        }
        return sb.toString();
    }

    public void vaciar() { usuarios.vaciar(); }
}
