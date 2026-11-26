package modelo;

/** Contacto recomendado y número de amigos en común con el usuario. */
public class Recomendacion {
    private final String nombre;
    private int amigosEnComun;

    public Recomendacion(String nombre) {
        this.nombre = nombre;
        this.amigosEnComun = 0;
    }

    public String getNombre() { return nombre; }
    public int getAmigosEnComun() { return amigosEnComun; }
    public void incrementar() { amigosEnComun++; }

    @Override
    public String toString() {
        return nombre + " (" + amigosEnComun + (amigosEnComun == 1 ? " amigo en común)" : " amigos en común)");
    }
}
