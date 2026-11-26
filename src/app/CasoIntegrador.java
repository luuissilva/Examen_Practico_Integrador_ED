package app;

import modelo.Solicitud;
import servicios.ServicioRedLaboratorio;
import servicios.SistemaSIGAC;
import estructuras.ABB;

/** Ejecuta, paso a paso, los 6 puntos del caso integrador obligatorio. */
public class CasoIntegrador {

    public static void ejecutar(SistemaSIGAC s, Consola c) {
        titulo("CASO INTEGRADOR – CARGA DE DATOS");
        s.cargarCasoIntegrador();
        System.out.println("Estudiantes insertados en el orden: 50, 30, 70, 20, 40, 60, 80");
        System.out.println("Red de laboratorio (dirigida):");
        System.out.print(s.getLaboratorio().mostrar());
        System.out.println("Red social (no dirigida):");
        System.out.print(s.getSocial().mostrar());
        pausa(c);

        titulo("PASO 1 – Inorden del ABB");
        ABB abb = s.getAcademico().getIndice();
        System.out.print(abb.dibujar());
        System.out.println("Preorden : " + ABB.matriculas(abb.preorden()));
        System.out.println("Inorden  : " + ABB.matriculas(abb.inorden()) + "   (esperado: 20, 30, 40, 50, 60, 70, 80)");
        System.out.println("Posorden : " + ABB.matriculas(abb.posorden()));
        pausa(c);

        titulo("PASO 2 – Eliminar matrícula 30 (dos hijos → sucesor inorden 40)");
        System.out.println("Eliminado: " + s.eliminarEstudiante(30));
        System.out.print(abb.dibujar());
        System.out.println("Inorden  : " + ABB.matriculas(abb.inorden()) + "   (propiedad del ABB conservada)");
        System.out.println("Preorden : " + ABB.matriculas(abb.preorden()));
        pausa(c);

        titulo("PASO 3 – Tres solicitudes FIFO y recuperación de dos operaciones LIFO");
        Solicitud s1 = s.registrarSolicitud(20, "PC1", "Práctica de redes");
        Solicitud s2 = s.registrarSolicitud(40, "SRV1", "Despliegue de servidor");
        Solicitud s3 = s.registrarSolicitud(50, "PC2", "Prueba de conectividad");
        System.out.println("Encoladas: S" + s1.getId() + ", S" + s2.getId() + ", S" + s3.getId());
        System.out.print(s.getAtencion().verSolicitudes());
        for (int i = 1; i <= 3; i++) System.out.println("Atendida " + i + ": " + s.atenderSolicitud());
        System.out.println("\nHistorial (pila) antes de deshacer:");
        System.out.print(s.getAtencion().verHistorial());
        System.out.println(s.deshacer());
        System.out.println(s.deshacer());
        System.out.println("\nCola después de recuperar 2 operaciones (vuelven S2 y S3 en su orden):");
        System.out.print(s.getAtencion().verSolicitudes());
        pausa(c);

        titulo("PASO 4 – BFS desde PC1");
        ServicioRedLaboratorio lab = s.getLaboratorio();
        System.out.print(lab.conectividad("PC1"));
        System.out.println("DFS desde PC1: " + ServicioRedLaboratorio.unir(lab.dfs("PC1")));
        pausa(c);

        titulo("PASO 5 – Recomendaciones para Ana");
        System.out.print(s.getSocial().recomendarTexto("Ana"));
        System.out.println("(Beto y Carla son amigos directos y Ana es el propio usuario: se excluyen)");
        pausa(c);

        titulo("PASO 6 – Eliminar la arista SW1 -> SRV1 y actualizar conectividad");
        s.eliminarConexion("SW1", "SRV1");
        System.out.print(lab.mostrar());
        System.out.print(lab.conectividad("PC1"));
        System.out.println("DFS desde PC1: " + ServicioRedLaboratorio.unir(lab.dfs("PC1")));
        System.out.println("\nFin del caso integrador. Los datos quedan cargados para seguir probando desde el menú.");
    }

    private static void titulo(String t) {
        System.out.println("\n==================================================");
        System.out.println(t);
        System.out.println("==================================================");
    }

    private static void pausa(Consola c) {
        if (c != null) c.pausa();
    }
}
