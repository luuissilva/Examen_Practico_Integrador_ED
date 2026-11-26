package app;

import java.util.Scanner;

/** Lectura validada por teclado. Nunca deja que el programa se caiga por una entrada mal escrita. */
public class Consola {
    private final Scanner sc;

    public Consola(Scanner sc) { this.sc = sc; }

    private String linea() {
        if (!sc.hasNextLine()) {
            System.out.println("\n(Fin de la entrada. Saliendo...)");
            System.exit(0);
        }
        return sc.nextLine();
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = linea().trim();
            if (!t.isEmpty()) return t;
            System.out.println("  ! El valor no puede estar vacío. Intente de nuevo.");
        }
    }

    /** Texto opcional (puede quedar vacío). */
    public String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return linea().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = linea().trim();
            try {
                return Integer.parseInt(t);
            } catch (NumberFormatException e) {
                System.out.println("  ! \"" + t + "\" no es un número entero válido. Intente de nuevo.");
            }
        }
    }

    public int leerEnteroPositivo(String mensaje) {
        while (true) {
            int n = leerEntero(mensaje);
            if (n > 0) return n;
            System.out.println("  ! Debe ser un entero mayor que 0.");
        }
    }

    public boolean confirmar(String mensaje) {
        while (true) {
            String t = leerTexto(mensaje + " (S/N): ").toUpperCase();
            if (t.equals("S") || t.equals("SI")) return true;
            if (t.equals("N") || t.equals("NO")) return false;
            System.out.println("  ! Responda S o N.");
        }
    }

    public void pausa() {
        System.out.print("\nPresione ENTER para continuar...");
        linea();
    }
}
