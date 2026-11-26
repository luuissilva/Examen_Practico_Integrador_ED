package modelo;

/** Modelo de un estudiante. La matrícula es el identificador único. */
public class Estudiante {
    private final int matricula;
    private String nombre;
    private String carrera;

    public Estudiante(int matricula, String nombre, String carrera) {
        if (matricula <= 0) throw new IllegalArgumentException("La matrícula debe ser un entero positivo.");
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (carrera == null || carrera.trim().isEmpty()) throw new IllegalArgumentException("La carrera no puede estar vacía.");
        this.matricula = matricula;
        this.nombre = nombre.trim();
        this.carrera = carrera.trim();
    }

    public int getMatricula() { return matricula; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.nombre = nombre.trim();
    }

    @Override
    public String toString() {
        return "[" + matricula + "] " + nombre + " - " + carrera;
    }
}
