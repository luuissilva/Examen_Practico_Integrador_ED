package app;

import estructuras.ABB;
import java.util.Scanner;
import modelo.Estudiante;
import modelo.Solicitud;
import pruebas.PruebasSIGAC;
import servicios.ServicioRedLaboratorio;
import servicios.SistemaSIGAC;

/**
 * SIGAC-UTA – Sistema Inteligente de Gestión Académica y Conectividad.
 * Todas las operaciones se ingresan por TECLADO desde este menú.
 */
public class Main {
    private static final SistemaSIGAC sistema = new SistemaSIGAC();
    private static Consola in;

    public static void main(String[] args) {
        in = new Consola(new Scanner(System.in));
        int op;
        do {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║        SIGAC-UTA  –  Estructura de Datos       ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            System.out.println(" 1. M1 Gestión de estudiantes (vector y lista)");
            System.out.println(" 2. M2 Solicitudes FIFO e historial LIFO");
            System.out.println(" 3. M3 Organización académica (árbol general y ABB)");
            System.out.println(" 4. M4 Red de laboratorios (grafo dirigido)");
            System.out.println(" 5. M5 Red social y recomendaciones");
            System.out.println(" 6. Cargar datos del caso integrador");
            System.out.println(" 7. Ejecutar caso integrador paso a paso");
            System.out.println(" 8. Ejecutar pruebas (normales, extremas y erróneas)");
            System.out.println(" 0. Salir");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1: menuEstudiantes(); break;
                    case 2: menuAtencion(); break;
                    case 3: menuAcademico(); break;
                    case 4: menuLaboratorio(); break;
                    case 5: menuSocial(); break;
                    case 6:
                        if (in.confirmar("Se borrarán los datos actuales. ¿Continuar?")) {
                            sistema.cargarCasoIntegrador();
                            System.out.println("✔ Datos del caso integrador cargados.");
                        }
                        break;
                    case 7: CasoIntegrador.ejecutar(sistema, in); break;
                    case 8: new PruebasSIGAC().ejecutarTodas(); break;
                    case 0: System.out.println("¡Hasta pronto!"); break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    // ============================ M1 ============================
    private static void menuEstudiantes() {
        int op;
        do {
            System.out.println("\n--- M1 GESTIÓN DE ESTUDIANTES ---");
            System.out.println(" 1. Registrar estudiante");
            System.out.println(" 2. Buscar por matrícula (secuencial vs ABB)");
            System.out.println(" 3. Actualizar nombre");
            System.out.println(" 4. Eliminar estudiante");
            System.out.println(" 5. Listar (vector)");
            System.out.println(" 6. Listar (lista enlazada)");
            System.out.println(" 0. Volver");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1: registrarEstudiante(); break;
                    case 2: {
                        int m = in.leerEntero("Matrícula a buscar: ");
                        System.out.print(sistema.getEstudiantes().compararBusquedas(m));
                        break;
                    }
                    case 3: {
                        int m = in.leerEntero("Matrícula: ");
                        Estudiante e = sistema.getEstudiantes().buscarABB(m);
                        if (e == null) { System.out.println("  ✘ No existe la matrícula " + m + "."); break; }
                        System.out.println("Actual: " + e);
                        sistema.actualizarNombre(m, in.leerTexto("Nuevo nombre: "));
                        System.out.println("✔ Actualizado: " + e);
                        break;
                    }
                    case 4: {
                        int m = in.leerEntero("Matrícula a eliminar: ");
                        Estudiante e = sistema.getEstudiantes().buscarABB(m);
                        if (e == null) { System.out.println("  ✘ No existe la matrícula " + m + "."); break; }
                        if (in.confirmar("¿Eliminar a " + e + "?")) {
                            sistema.eliminarEstudiante(m);
                            System.out.println("✔ Eliminado de vector, lista, ABB y árbol académico.");
                        }
                        break;
                    }
                    case 5: System.out.print(sistema.getEstudiantes().listarVector()); break;
                    case 6: System.out.print(sistema.getEstudiantes().listarLista()); break;
                    case 0: break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void registrarEstudiante() {
        if (sistema.getAcademico().listarCarreras().estaVacia()) {
            System.out.println("  ! No hay carreras registradas. Cree una facultad y una carrera en M3 primero.");
            return;
        }
        System.out.println("Carreras disponibles: " + sistema.getAcademico().listarCarreras());
        int m = in.leerEnteroPositivo("Matrícula: ");
        String n = in.leerTexto("Nombre: ");
        String c = in.leerTexto("Carrera: ");
        sistema.registrarEstudiante(m, n, c);
        System.out.println("✔ Estudiante registrado (vector, lista, ABB y árbol académico).");
    }

    // ============================ M2 ============================
    private static void menuAtencion() {
        int op;
        do {
            System.out.println("\n--- M2 SOLICITUDES E HISTORIAL ---");
            System.out.println(" 1. Registrar solicitud de laboratorio (encolar)");
            System.out.println(" 2. Atender siguiente solicitud (FIFO)");
            System.out.println(" 3. Ver cola de solicitudes");
            System.out.println(" 4. Deshacer última operación (LIFO)");
            System.out.println(" 5. Ver historial de operaciones (pila)");
            System.out.println(" 0. Volver");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1: {
                        int m = in.leerEntero("Matrícula del estudiante: ");
                        String eq = in.leerTexto("Equipo solicitado (ej. PC1): ");
                        String d = in.leerTextoOpcional("Descripción (opcional): ");
                        Solicitud s = sistema.registrarSolicitud(m, eq, d);
                        System.out.println("✔ Encolada " + s + " | Pendientes: " + sistema.getAtencion().pendientes());
                        break;
                    }
                    case 2: System.out.println("✔ Atendida: " + sistema.atenderSolicitud()); break;
                    case 3: System.out.print(sistema.getAtencion().verSolicitudes()); break;
                    case 4: System.out.println("✔ " + sistema.deshacer()); break;
                    case 5: System.out.print(sistema.getAtencion().verHistorial()); break;
                    case 0: break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    // ============================ M3 ============================
    private static void menuAcademico() {
        int op;
        do {
            ABB abb = sistema.getAcademico().getIndice();
            System.out.println("\n--- M3 ORGANIZACIÓN ACADÉMICA ---");
            System.out.println(" 1. Agregar facultad");
            System.out.println(" 2. Agregar carrera a una facultad");
            System.out.println(" 3. Mostrar jerarquía (árbol general)");
            System.out.println(" 4. Insertar estudiante en el ABB");
            System.out.println(" 5. Buscar en el ABB");
            System.out.println(" 6. Eliminar del ABB");
            System.out.println(" 7. Recorridos preorden, inorden y posorden");
            System.out.println(" 8. Dibujar ABB y altura");
            System.out.println(" 0. Volver");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        sistema.getAcademico().agregarFacultad(in.leerTexto("Nombre de la facultad: "));
                        System.out.println("✔ Facultad agregada.");
                        break;
                    case 2: {
                        String f = in.leerTexto("Facultad: ");
                        String c = in.leerTexto("Nombre de la carrera: ");
                        sistema.getAcademico().agregarCarrera(f, c);
                        System.out.println("✔ Carrera agregada.");
                        break;
                    }
                    case 3: System.out.print(sistema.getAcademico().mostrarJerarquia()); break;
                    case 4: registrarEstudiante(); break;
                    case 5: {
                        int m = in.leerEntero("Matrícula: ");
                        Estudiante e = abb.buscar(m);
                        System.out.println(e == null ? "  ✘ No existe la matrícula " + m + "." : "✔ " + e);
                        System.out.println("  Comparaciones en el ABB: " + abb.getUltimasComparaciones());
                        break;
                    }
                    case 6: {
                        int m = in.leerEntero("Matrícula a eliminar: ");
                        sistema.eliminarEstudiante(m);
                        System.out.println("✔ Eliminado (política: dos hijos → sucesor inorden).");
                        System.out.println("Inorden: " + ABB.matriculas(abb.inorden()));
                        break;
                    }
                    case 7:
                        if (abb.estaVacio()) { System.out.println("  ! El ABB está vacío."); break; }
                        System.out.println("Preorden : " + ABB.matriculas(abb.preorden()));
                        System.out.println("Inorden  : " + ABB.matriculas(abb.inorden()));
                        System.out.println("Posorden : " + ABB.matriculas(abb.posorden()));
                        break;
                    case 8:
                        System.out.print(abb.dibujar());
                        System.out.println("Nodos: " + abb.tamanio() + " | Altura: " + abb.altura());
                        break;
                    case 0: break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    // ============================ M4 ============================
    private static void menuLaboratorio() {
        int op;
        do {
            ServicioRedLaboratorio lab = sistema.getLaboratorio();
            System.out.println("\n--- M4 RED DE LABORATORIOS ---");
            System.out.println(" 1. Agregar equipo");
            System.out.println(" 2. Agregar conexión dirigida con peso");
            System.out.println(" 3. Eliminar conexión");
            System.out.println(" 4. Eliminar equipo (y sus aristas entrantes/salientes)");
            System.out.println(" 5. Mostrar red");
            System.out.println(" 6. Recorrido BFS");
            System.out.println(" 7. Recorrido DFS");
            System.out.println(" 8. Equipos no alcanzables");
            System.out.println(" 9. ¿Existe camino entre dos equipos?");
            System.out.println(" 0. Volver");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        sistema.agregarEquipo(in.leerTexto("Nombre del equipo: "));
                        System.out.println("✔ Equipo agregado.");
                        break;
                    case 2: {
                        String o = in.leerTexto("Origen: ");
                        String d = in.leerTexto("Destino: ");
                        int p = in.leerEnteroPositivo("Peso (latencia/costo): ");
                        sistema.agregarConexion(o, d, p);
                        System.out.println("✔ Conexión agregada.");
                        break;
                    }
                    case 3: {
                        String o = in.leerTexto("Origen: ");
                        String d = in.leerTexto("Destino: ");
                        sistema.eliminarConexion(o, d);
                        System.out.println("✔ Conexión eliminada.");
                        break;
                    }
                    case 4: {
                        String e = in.leerTexto("Equipo a eliminar: ");
                        if (in.confirmar("Esta acción no se puede deshacer. ¿Continuar?")) {
                            sistema.eliminarEquipo(e);
                            System.out.println("✔ Equipo eliminado junto con sus aristas entrantes y salientes.");
                        }
                        break;
                    }
                    case 5: System.out.print(lab.mostrar()); break;
                    case 6: {
                        String s = in.leerTexto("Equipo de inicio: ");
                        System.out.println("BFS: " + ServicioRedLaboratorio.unir(lab.bfs(s)));
                        break;
                    }
                    case 7: {
                        String s = in.leerTexto("Equipo de inicio: ");
                        System.out.println("DFS: " + ServicioRedLaboratorio.unir(lab.dfs(s)));
                        break;
                    }
                    case 8: System.out.print(lab.conectividad(in.leerTexto("Equipo de inicio: "))); break;
                    case 9: {
                        String o = in.leerTexto("Origen: ");
                        String d = in.leerTexto("Destino: ");
                        System.out.println(lab.existeCamino(o, d) ? "✔ Sí existe camino." : "✘ No existe camino.");
                        break;
                    }
                    case 0: break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }

    // ============================ M5 ============================
    private static void menuSocial() {
        int op;
        do {
            System.out.println("\n--- M5 RED SOCIAL Y RECOMENDACIONES ---");
            System.out.println(" 1. Agregar usuario");
            System.out.println(" 2. Registrar amistad");
            System.out.println(" 3. Eliminar amistad");
            System.out.println(" 4. Mostrar red social");
            System.out.println(" 5. Recomendar contactos de segundo grado");
            System.out.println(" 0. Volver");
            op = in.leerEntero("Opción: ");
            try {
                switch (op) {
                    case 1:
                        sistema.agregarUsuario(in.leerTexto("Nombre del usuario: "));
                        System.out.println("✔ Usuario agregado.");
                        break;
                    case 2: {
                        String a = in.leerTexto("Usuario 1: ");
                        String b = in.leerTexto("Usuario 2: ");
                        sistema.agregarAmistad(a, b);
                        System.out.println("✔ Amistad registrada.");
                        break;
                    }
                    case 3: {
                        String a = in.leerTexto("Usuario 1: ");
                        String b = in.leerTexto("Usuario 2: ");
                        sistema.eliminarAmistad(a, b);
                        System.out.println("✔ Amistad eliminada.");
                        break;
                    }
                    case 4: System.out.print(sistema.getSocial().mostrar()); break;
                    case 5: System.out.print(sistema.getSocial().recomendarTexto(in.leerTexto("Usuario: "))); break;
                    case 0: break;
                    default: System.out.println("  ! Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("  ✘ Error: " + e.getMessage());
            }
        } while (op != 0);
    }
}
