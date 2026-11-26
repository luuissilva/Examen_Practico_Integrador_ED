package pruebas;

import estructuras.ABB;
import estructuras.Cola;
import estructuras.GrafoDirigido;
import estructuras.Pila;
import estructuras.Vector;
import modelo.Estudiante;
import modelo.Recomendacion;
import servicios.ServicioRedLaboratorio;
import servicios.SistemaSIGAC;

/**
 * Pruebas automáticas sin librerías externas.
 * Tipos: N = normal, X = extrema, E = errónea. Los IDs coinciden con docs/matriz-pruebas.md.
 */
public class PruebasSIGAC {
    private int pasadas, falladas;

    private interface Accion { void ejecutar() throws Exception; }

    private void verificar(String id, String desc, boolean condicion) {
        if (condicion) pasadas++; else falladas++;
        System.out.printf("  %-6s %-62s %s%n", id, desc, condicion ? "PASA" : "FALLA");
    }

    private void esperarError(String id, String desc, Accion a) {
        boolean lanzo = false;
        String msg = "";
        try { a.ejecutar(); } catch (Exception e) { lanzo = true; msg = e.getMessage(); }
        verificar(id, desc, lanzo);
        if (lanzo) System.out.println("         ↳ mensaje: " + msg);
    }

    private static String mats(ABB abb) { return ABB.matriculas(abb.inorden()); }

    public void ejecutarTodas() {
        pasadas = falladas = 0;
        System.out.println("\n================ PRUEBAS NORMALES (caso integrador) ================");
        normales();
        System.out.println("\n================ PRUEBAS EXTREMAS ================");
        extremas();
        System.out.println("\n================ PRUEBAS ERRÓNEAS ================");
        erroneas();
        System.out.println("\nRESULTADO: " + pasadas + " pasadas, " + falladas + " falladas, total " + (pasadas + falladas));
    }

    private void normales() {
        SistemaSIGAC s = new SistemaSIGAC();
        s.cargarCasoIntegrador();
        ABB abb = s.getAcademico().getIndice();

        verificar("N01", "Inorden del ABB = 20,30,40,50,60,70,80",
                mats(abb).equals("20, 30, 40, 50, 60, 70, 80"));
        verificar("N02", "Preorden = 50,30,20,40,70,60,80",
                ABB.matriculas(abb.preorden()).equals("50, 30, 20, 40, 70, 60, 80"));
        verificar("N03", "Posorden = 20,40,30,60,80,70,50",
                ABB.matriculas(abb.posorden()).equals("20, 40, 30, 60, 80, 70, 50"));
        verificar("N04", "Búsqueda secuencial de 80 hace 7 comparaciones",
                s.getEstudiantes().buscarSecuencial(80) != null && s.getEstudiantes().getComparacionesVector() == 7);
        verificar("N05", "Búsqueda en ABB de 80 hace 3 comparaciones",
                abb.buscar(80) != null && abb.getUltimasComparaciones() == 3);

        s.eliminarEstudiante(30);
        verificar("N06", "Eliminar 30 (dos hijos): inorden 20,40,50,60,70,80",
                mats(abb).equals("20, 40, 50, 60, 70, 80"));
        verificar("N07", "Tras eliminar 30, preorden = 50,40,20,70,60,80 (sucesor 40)",
                ABB.matriculas(abb.preorden()).equals("50, 40, 20, 70, 60, 80"));
        verificar("N08", "30 ya no está en vector, lista ni árbol general",
                s.getEstudiantes().buscarSecuencial(30) == null && s.getEstudiantes().buscarEnLista(30) == null
                        && s.getAcademico().getJerarquia().buscarEstudiante(30) == null);

        s.registrarSolicitud(20, "PC1", "a");
        s.registrarSolicitud(40, "SRV1", "b");
        s.registrarSolicitud(50, "PC2", "c");
        int a1 = s.atenderSolicitud().getMatricula();
        int a2 = s.atenderSolicitud().getMatricula();
        int a3 = s.atenderSolicitud().getMatricula();
        verificar("N09", "FIFO: se atiende 20, 40, 50 en ese orden", a1 == 20 && a2 == 40 && a3 == 50);
        s.deshacer();
        s.deshacer();
        verificar("N10", "LIFO: deshacer 2 devuelve S2 y S3 (frente = matrícula 40)",
                s.getAtencion().pendientes() == 2 && s.getAtencion().verFrente().getMatricula() == 40);

        ServicioRedLaboratorio lab = s.getLaboratorio();
        verificar("N11", "BFS desde PC1 = PC1, SW1, PC3, SRV1, PC2",
                ServicioRedLaboratorio.unir(lab.bfs("PC1")).equals("PC1, SW1, PC3, SRV1, PC2"));
        verificar("N12", "DFS desde PC1 = PC1, SW1, SRV1, PC2, PC3",
                ServicioRedLaboratorio.unir(lab.dfs("PC1")).equals("PC1, SW1, SRV1, PC2, PC3"));
        verificar("N13", "PC4 es no alcanzable desde PC1",
                ServicioRedLaboratorio.unir(lab.noAlcanzables("PC1")).equals("PC4"));

        Vector<Recomendacion> r = s.getSocial().recomendar("Ana");
        verificar("N14", "Ana: 1° Diego (2 en común), 2° Elena (1 en común)",
                r.tamanio() == 2 && r.obtener(0).getNombre().equals("Diego") && r.obtener(0).getAmigosEnComun() == 2
                        && r.obtener(1).getNombre().equals("Elena") && r.obtener(1).getAmigosEnComun() == 1);

        s.eliminarConexion("SW1", "SRV1");
        verificar("N15", "Sin SW1->SRV1: BFS = PC1, SW1, PC3",
                ServicioRedLaboratorio.unir(lab.bfs("PC1")).equals("PC1, SW1, PC3"));
        verificar("N16", "Sin SW1->SRV1: no alcanzables = SRV1, PC2, PC4",
                ServicioRedLaboratorio.unir(lab.noAlcanzables("PC1")).equals("SRV1, PC2, PC4"));
        s.deshacer();
        verificar("N17", "Deshacer eliminación de arista restaura conectividad",
                ServicioRedLaboratorio.unir(lab.noAlcanzables("PC1")).equals("PC4"));

        s.actualizarNombre(50, "Nombre Nuevo");
        s.deshacer();
        verificar("N18", "Actualizar nombre y deshacer restaura el nombre anterior",
                s.getEstudiantes().buscarABB(50).getNombre().equals("Andrea Lopez"));

        s.eliminarEquipo("PC3");
        verificar("N19", "Eliminar PC3 borra también la arista entrante PC1->PC3",
                s.getLaboratorio().getRed().buscarVertice("PC1").aristaHacia("PC3") == null
                        && !s.getLaboratorio().existeEquipo("PC3"));
    }

    private void extremas() {
        ABB vacio = new ABB();
        verificar("X01", "Recorridos de un ABB vacío devuelven lista vacía",
                vacio.inorden().estaVacia() && vacio.preorden().estaVacia() && vacio.posorden().estaVacia());
        verificar("X02", "Buscar en ABB vacío devuelve null", vacio.buscar(10) == null);

        ABB uno = new ABB();
        uno.insertar(new Estudiante(1, "Solo", "Software"));
        uno.eliminar(1);
        verificar("X03", "Eliminar la raíz única deja el ABB vacío", uno.estaVacio() && uno.tamanio() == 0);

        ABB deg = new ABB();
        for (int i = 1; i <= 100; i++) deg.insertar(new Estudiante(i, "E" + i, "Software"));
        deg.buscar(100);
        verificar("X04", "ABB degenerado (1..100): altura 100 y 100 comparaciones (peor caso)",
                deg.altura() == 100 && deg.getUltimasComparaciones() == 100);

        SistemaSIGAC s = new SistemaSIGAC();
        s.cargarCasoIntegrador();
        s.eliminarEstudiante(50);
        verificar("X05", "Eliminar la RAÍZ 50 (dos hijos): sube 60, inorden correcto",
                s.getAcademico().getIndice().getRaiz().getDato().getMatricula() == 60
                        && mats(s.getAcademico().getIndice()).equals("20, 30, 40, 60, 70, 80"));
        s.eliminarEstudiante(20);
        verificar("X06", "Eliminar una hoja (20)", mats(s.getAcademico().getIndice()).equals("30, 40, 60, 70, 80"));

        Vector<Integer> v = new Vector<>(2);
        for (int i = 0; i < 1000; i++) v.agregar(i);
        verificar("X07", "Vector crece de capacidad 2 a 1024 con 1000 elementos",
                v.tamanio() == 1000 && v.capacidad() == 1024 && v.obtener(999) == 999);

        Cola<Integer> c = new Cola<>();
        c.encolar(1);
        c.desencolar();
        verificar("X08", "Cola queda vacía tras encolar y desencolar un elemento", c.estaVacia());

        GrafoDirigido g = new GrafoDirigido();
        g.agregarVertice("A");
        verificar("X09", "BFS en grafo con un solo vértice = A",
                ServicioRedLaboratorio.unir(g.bfs("A")).equals("A"));
        verificar("X10", "BFS desde PC4 (aislado) solo visita PC4",
                ServicioRedLaboratorio.unir(s.getLaboratorio().bfs("PC4")).equals("PC4"));

        servicios.ServicioRecomendaciones rs = new servicios.ServicioRecomendaciones();
        String[] us = {"Uno", "Ana", "Bea", "Yago", "Xime"};
        for (String u : us) rs.agregarUsuario(u);
        rs.agregarAmistad("Uno", "Ana");
        rs.agregarAmistad("Uno", "Bea");
        rs.agregarAmistad("Ana", "Yago");
        rs.agregarAmistad("Bea", "Xime");
        Vector<Recomendacion> emp = rs.recomendar("Uno");
        verificar("X11", "Empate (1 y 1 en común) se ordena por nombre: Xime, Yago",
                emp.tamanio() == 2 && emp.obtener(0).getNombre().equals("Xime") && emp.obtener(1).getNombre().equals("Yago"));
        s.agregarUsuario("Fabian");
        verificar("X12", "Usuario sin amigos no recibe recomendaciones",
                s.getSocial().recomendar("Fabian").estaVacio());
    }

    private void erroneas() {
        SistemaSIGAC s = new SistemaSIGAC();
        s.cargarCasoIntegrador();

        esperarError("E01", "Registrar matrícula duplicada (50)", () -> s.registrarEstudiante(50, "Otro", "Software"));
        verificar("E02", "Tras el duplicado siguen habiendo 7 estudiantes", s.getEstudiantes().cantidad() == 7);
        esperarError("E03", "Registrar con nombre vacío", () -> s.registrarEstudiante(90, "  ", "Software"));
        esperarError("E04", "Registrar con nombre null", () -> s.registrarEstudiante(91, null, "Software"));
        esperarError("E05", "Registrar con carrera inexistente", () -> s.registrarEstudiante(92, "Luis", "Medicina"));
        esperarError("E06", "Registrar matrícula negativa", () -> s.registrarEstudiante(-5, "Luis", "Software"));
        esperarError("E07", "Eliminar matrícula inexistente (99)", () -> s.eliminarEstudiante(99));
        esperarError("E08", "Insertar null en el ABB", () -> new ABB().insertar(null));
        esperarError("E09", "Atender con cola vacía", () -> s.atenderSolicitud());
        esperarError("E10", "Deshacer con historial vacío", () -> s.deshacer());
        esperarError("E11", "Solicitud de matrícula inexistente", () -> s.registrarSolicitud(99, "PC1", ""));
        esperarError("E12", "Solicitud a equipo inexistente", () -> s.registrarSolicitud(50, "PC9", ""));
        esperarError("E13", "Equipo duplicado (PC1)", () -> s.agregarEquipo("pc1"));
        esperarError("E14", "Conexión a equipo inexistente", () -> s.agregarConexion("PC1", "PC9", 1));
        esperarError("E15", "Conexión con peso 0", () -> s.agregarConexion("PC2", "PC4", 0));
        esperarError("E16", "Conexión duplicada PC1->SW1", () -> s.agregarConexion("PC1", "SW1", 5));
        esperarError("E17", "Eliminar conexión inexistente PC4->PC1", () -> s.eliminarConexion("PC4", "PC1"));
        esperarError("E18", "BFS desde equipo inexistente", () -> s.getLaboratorio().bfs("XYZ"));
        esperarError("E19", "Amistad consigo mismo", () -> s.agregarAmistad("Ana", "ana"));
        esperarError("E20", "Amistad duplicada Ana-Beto", () -> s.agregarAmistad("Ana", "Beto"));
        esperarError("E21", "Recomendar a usuario inexistente", () -> s.getSocial().recomendar("Zoe"));
        s.registrarSolicitud(70, "PC3", "pendiente");
        esperarError("E22", "Eliminar estudiante con solicitud pendiente", () -> s.eliminarEstudiante(70));
        esperarError("E23", "Desapilar pila vacía", () -> new Pila<String>().desapilar());
        esperarError("E24", "Facultad duplicada", () -> s.getAcademico().agregarFacultad("fisei"));
    }

    public static void main(String[] args) {
        new PruebasSIGAC().ejecutarTodas();
    }
}
