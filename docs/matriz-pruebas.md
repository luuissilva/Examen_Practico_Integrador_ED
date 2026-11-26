# Matriz de pruebas – SIGAC-UTA

Ejecución automática: `java -cp out pruebas.PruebasSIGAC` (u opción **8** del menú).
Tipos: **N** = normal, **X** = extrema, **E** = errónea. Resultado de la última ejecución: **55 de 55 pasan**.

## Pruebas normales (caso integrador)

| ID | Módulo | Entrada / acción | Resultado esperado | Obtenido |
|---|---|---|---|---|
| N01 | M3 | Insertar 50, 30, 70, 20, 40, 60, 80 y recorrer inorden | 20, 30, 40, 50, 60, 70, 80 | PASA |
| N02 | M3 | Preorden | 50, 30, 20, 40, 70, 60, 80 | PASA |
| N03 | M3 | Posorden | 20, 40, 30, 60, 80, 70, 50 | PASA |
| N04 | M1 | Búsqueda secuencial de 80 en el vector | Encontrado con 7 comparaciones | PASA |
| N05 | M1/M3 | Búsqueda de 80 en el ABB | Encontrado con 3 comparaciones | PASA |
| N06 | M3 | Eliminar 30 (dos hijos) | Inorden 20, 40, 50, 60, 70, 80 | PASA |
| N07 | M3 | Preorden tras eliminar 30 | 50, 40, 20, 70, 60, 80 (sucesor 40) | PASA |
| N08 | M1/M3 | Buscar 30 en vector, lista y árbol general | No existe en ninguna estructura | PASA |
| N09 | M2 | Encolar solicitudes de 20, 40, 50 y atender 3 | Atiende 20, 40, 50 (FIFO) | PASA |
| N10 | M2 | Deshacer 2 veces | Vuelven S3 y S2 (LIFO); frente = matrícula 40 | PASA |
| N11 | M4 | BFS desde PC1 | PC1, SW1, PC3, SRV1, PC2 | PASA |
| N12 | M4 | DFS desde PC1 | PC1, SW1, SRV1, PC2, PC3 | PASA |
| N13 | M4 | No alcanzables desde PC1 | PC4 | PASA |
| N14 | M5 | Recomendar a Ana | Diego (2), Elena (1) | PASA |
| N15 | M4 | Eliminar SW1→SRV1 y BFS desde PC1 | PC1, SW1, PC3 | PASA |
| N16 | M4 | No alcanzables tras eliminar la arista | SRV1, PC2, PC4 | PASA |
| N17 | M2/M4 | Deshacer la eliminación de la arista | No alcanzables vuelve a ser solo PC4 | PASA |
| N18 | M1/M2 | Actualizar nombre de 50 y deshacer | Nombre original "Andrea Lopez" | PASA |
| N19 | M4 | Eliminar el equipo PC3 | Se borra también la arista entrante PC1→PC3 | PASA |

## Pruebas extremas

| ID | Módulo | Entrada / acción | Resultado esperado | Obtenido |
|---|---|---|---|---|
| X01 | M3 | Recorridos de ABB vacío | Listas vacías, sin errores | PASA |
| X02 | M3 | Buscar en ABB vacío | `null` | PASA |
| X03 | M3 | Insertar 1 nodo y eliminar la raíz | ABB vacío, tamaño 0 | PASA |
| X04 | M3 | Insertar 1..100 en orden (árbol degenerado) y buscar 100 | Altura 100, 100 comparaciones (peor caso O(n)) | PASA |
| X05 | M3 | Eliminar la raíz 50 (dos hijos) | Sube 60; inorden 20, 30, 40, 60, 70, 80 | PASA |
| X06 | M3 | Eliminar la hoja 20 | Inorden 30, 40, 60, 70, 80 | PASA |
| X07 | M1 | Vector de capacidad 2 con 1000 elementos | Capacidad 1024, último elemento 999 | PASA |
| X08 | M2 | Encolar y desencolar un solo elemento | Cola vacía | PASA |
| X09 | M4 | BFS en grafo de un solo vértice | A | PASA |
| X10 | M4 | BFS desde PC4 (aislado) | Solo PC4 | PASA |
| X11 | M5 | Dos candidatos con 1 amigo en común cada uno | Orden alfabético: Xime, Yago | PASA |
| X12 | M5 | Recomendar a usuario sin amigos | Lista vacía | PASA |

## Pruebas erróneas (validaciones)

| ID | Módulo | Entrada / acción | Resultado esperado | Obtenido |
|---|---|---|---|---|
| E01 | M1 | Registrar matrícula 50 repetida | Rechazo: "Ya existe un estudiante con la matrícula 50." | PASA |
| E02 | M1 | Contar estudiantes tras el duplicado | Siguen 7 (no se insertó nada) | PASA |
| E03 | M1 | Nombre vacío | Rechazo | PASA |
| E04 | M1 | Nombre `null` | Rechazo (referencia nula) | PASA |
| E05 | M1/M3 | Carrera inexistente (Medicina) | Rechazo | PASA |
| E06 | M1 | Matrícula negativa | Rechazo | PASA |
| E07 | M1 | Eliminar matrícula 99 inexistente | Rechazo | PASA |
| E08 | M3 | Insertar `null` en el ABB | Rechazo | PASA |
| E09 | M2 | Atender con la cola vacía | "La cola está vacía" | PASA |
| E10 | M2 | Deshacer con la pila vacía | "La pila está vacía" | PASA |
| E11 | M2 | Solicitud con matrícula inexistente | Rechazo | PASA |
| E12 | M2/M4 | Solicitud a equipo inexistente PC9 | Rechazo | PASA |
| E13 | M4 | Agregar equipo duplicado `pc1` | Rechazo | PASA |
| E14 | M4 | Conexión hacia equipo inexistente | Rechazo | PASA |
| E15 | M4 | Conexión con peso 0 | Rechazo | PASA |
| E16 | M4 | Conexión duplicada PC1→SW1 | Rechazo | PASA |
| E17 | M4 | Eliminar conexión inexistente PC4→PC1 | Rechazo | PASA |
| E18 | M4 | BFS desde equipo inexistente | Rechazo | PASA |
| E19 | M5 | Amistad Ana–ana (consigo mismo) | Rechazo | PASA |
| E20 | M5 | Amistad duplicada Ana–Beto | Rechazo | PASA |
| E21 | M5 | Recomendar a usuario inexistente | Rechazo | PASA |
| E22 | M1/M2 | Eliminar estudiante con solicitud pendiente | Rechazo (eliminación segura) | PASA |
| E23 | M2 | Desapilar una pila vacía | Rechazo | PASA |
| E24 | M3 | Facultad duplicada `fisei` | Rechazo | PASA |

## Validaciones de entrada por teclado (manuales)

| Caso | Entrada | Comportamiento | Evidencia |
|---|---|---|---|
| Texto donde se pide número | `cincuenta` en matrícula | Mensaje y vuelve a pedir el dato | `capturas/09-validaciones-errores.png` |
| Peso no positivo | `-3` | "Debe ser un entero mayor que 0" y vuelve a pedir | `capturas/09` |
| Campo vacío | ENTER sin texto | "El valor no puede estar vacío" | `Consola.leerTexto` |
| Opción fuera de rango | `9` en el menú principal | "Opción no válida" | `capturas/09` |
| Confirmación | Eliminar estudiante o equipo | Pide S/N antes de borrar | `capturas/06` |
