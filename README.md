# SIGAC-UTA
### Sistema Inteligente de Gestión Académica y Conectividad

| | |
|---|---|
| **Universidad** | Universidad Técnica de Ambato – FISEI |
| **Carrera** | Ingeniería en Software – Tercer nivel |
| **Asignatura** | Estructura de Datos |
| **Docente** | Ing. Ruben Caiza |
| **Actividad** | Examen práctico integrador |
| **Autor** | Luis Silva (trabajo individual) |
| **Lenguaje** | Java |

---

## 1. Descripción general

SIGAC-UTA es un prototipo de consola que integra en una sola aplicación las estructuras de datos estudiadas durante el período: **vectores, listas enlazadas, pilas, colas, árboles generales, árboles binarios de búsqueda (ABB) y grafos dinámicos**, junto con los algoritmos de recorrido **BFS** y **DFS**.

El sistema responde a una necesidad de la FISEI: gestionar estudiantes, organizar la estructura académica, atender solicitudes de laboratorio, analizar la conectividad de los equipos y recomendar colaboradores académicos a partir de contactos en común. Cada módulo usa la estructura que mejor se ajusta a su problema, y todos comparten los mismos datos para funcionar como un solo sistema.

## 2. Objetivo

Desarrollar e integrar un sistema de gestión académica que aplique cada estructura de datos a un problema real, implementándolas manualmente, validando los casos normales, extremos y erróneos, y documentando su comportamiento y su complejidad temporal.

---

## 3. Solución por módulos

### M1 – Gestión de estudiantes (vector y lista enlazada)
Los estudiantes se registran en un **vector dinámico** que duplica su capacidad cuando se llena, y en una **lista enlazada** con inserción al final. El módulo permite registrar, buscar por matrícula, actualizar y eliminar. Rechaza matrículas duplicadas y solo elimina después de verificar que el estudiante existe y no tiene trámites pendientes.

También compara la **búsqueda secuencial** con la **búsqueda en el ABB** contando las comparaciones que hace cada una. Por ejemplo, para encontrar la matrícula 80 la búsqueda secuencial necesita 7 comparaciones y el ABB solo 3.

### M2 – Atención e historial (cola y pila)
- **Cola FIFO:** las solicitudes de laboratorio se atienden en el orden en que llegan.
- **Pila LIFO:** guarda las operaciones reversibles del sistema. La opción *Deshacer* revierte siempre la última.

| Operación registrada | Cómo se deshace |
|---|---|
| Registrar estudiante | Se elimina |
| Eliminar estudiante | Se vuelve a registrar con sus datos |
| Actualizar nombre | Se restaura el nombre anterior |
| Atender solicitud | La solicitud vuelve al frente de la cola |
| Agregar o eliminar conexión | Se elimina o se vuelve a agregar con el mismo peso |
| Agregar amistad | Se elimina la amistad |

### M3 – Organización académica (árbol general y ABB)
- **Árbol general:** representa la jerarquía **Universidad → Facultad → Carrera → Estudiantes**. Cada nodo guarda a sus hijos en una lista enlazada, así que puede tener cualquier cantidad.
- **ABB:** funciona como índice de estudiantes ordenado por matrícula. Permite insertar, buscar, eliminar y recorrer en **preorden, inorden y posorden**.

### M4 – Red de laboratorios (grafo dinámico)
Los equipos (PC, switches y servidores) forman un **grafo dirigido y ponderado** representado con listas de adyacencia. El peso de cada conexión indica su costo o latencia. El módulo:
- recorre la red con **BFS** (usando una cola) y **DFS** (recursivo);
- detecta los **equipos no alcanzables** desde un origen;
- comprueba si existe un camino entre dos equipos;
- elimina conexiones, y al eliminar un equipo borra también todas sus **aristas entrantes y salientes**.

### M5 – Recomendaciones (grafo social)
Las amistades forman un **grafo no dirigido**. Para recomendar contactos a un usuario se buscan los **amigos de sus amigos** (segundo grado):
1. Se recorren los amigos de cada amigo directo del usuario.
2. Se descarta al propio usuario y a quienes ya son sus amigos directos.
3. Cada coincidencia suma un amigo en común.
4. Los candidatos se ordenan por número de amigos en común, de mayor a menor. Si empatan, por nombre.

### M6 – Integración y pruebas
Un **menú de consola** reúne todos los módulos. Todos los datos se ingresan por teclado y cada entrada se valida para que el programa no se detenga ante un error. Se incluyen pruebas automáticas normales, extremas y erróneas, además de una opción que ejecuta paso a paso el caso integrador.

---

## 4. Integración entre módulos

Los módulos no trabajan aislados:

- Al **registrar un estudiante** se guarda al mismo tiempo en el vector, la lista enlazada, el ABB y como hoja de su carrera en el árbol general. Al eliminarlo se quita de las cuatro estructuras.
- Una **solicitud de laboratorio** solo se acepta si la matrícula existe (M1) y el equipo existe en la red (M4).
- No se puede eliminar un estudiante ni un equipo que tenga **solicitudes pendientes**.
- El **historial** guarda operaciones de M1, M2, M4 y M5, de modo que *Deshacer* funciona en todo el sistema.

```
            ┌──────────────── SistemaSIGAC (integración) ────────────────┐
            │                                                            │
  M1 Estudiantes ──► M3 ABB + árbol académico        M4 Red de laboratorios
        │                                                   │
        └───────────► M2 Cola de solicitudes ◄──────────────┘
                      M2 Pila de historial ◄── M1, M4, M5
                                                     M5 Red social
```

---

## 5. Organización del proyecto

| Paquete | Contenido |
|---|---|
| `modelo` | Datos del dominio: `Estudiante`, `Solicitud`, `Operacion`, `UnidadAcademica`, `Recomendacion` |
| `estructuras` | Estructuras implementadas a mano: `Vector`, `ListaEnlazada`, `Pila`, `Cola`, `ArbolGeneral`, `ABB`, `GrafoDirigido`, `GrafoSocial` |
| `servicios` | Lógica de cada módulo y la clase de integración `SistemaSIGAC` |
| `pruebas` | `PruebasSIGAC`: pruebas normales, extremas y erróneas |
| `app` | `Main` (menú), `Consola` (lectura validada) y `CasoIntegrador` |
| `docs` | Matriz de pruebas, contribuciones y capturas de ejecución |

**Restricción técnica cumplida:** no se usan `ArrayList`, `LinkedList`, `Stack`, `Queue`, `HashMap`, `TreeMap`, `TreeSet` ni bibliotecas de grafos. Todas las estructuras centrales y sus algoritmos están programados manualmente.

---

## 6. Decisiones de diseño

Estas reglas se fijaron para que los resultados sean siempre los mismos y las pruebas se puedan repetir:

| Aspecto | Decisión |
|---|---|
| Orden de los vecinos | Las conexiones nuevas se agregan al final de la lista, así que BFS y DFS visitan los vecinos en el orden en que se registraron. |
| Dirección de las aristas | La red de laboratorio es **dirigida**: `A → B` no implica `B → A`. La red social es **no dirigida**: la amistad es mutua. |
| Eliminación en el ABB | Hoja: se quita. Un hijo: el hijo ocupa su lugar. Dos hijos: se reemplaza por el **sucesor inorden** (el menor del subárbol derecho). |
| Duplicados | Se rechazan matrículas, equipos, conexiones y amistades repetidas. |
| BFS | Un equipo se marca como visitado al entrar a la cola, para que no se encole dos veces. |
| Empates en recomendaciones | Si dos candidatos tienen los mismos amigos en común, se ordenan alfabéticamente. |

---

## 7. Caso integrador

**Datos de entrada**
- Matrículas, en este orden de inserción: 50, 30, 70, 20, 40, 60, 80.
- Red de laboratorio: PC1 → SW1 → SRV1 → PC2, además PC1 → PC3. PC4 está aislado.
- Red social: Ana es amiga de Beto y Carla; Beto y Carla son amigos de Diego; Carla también es amiga de Elena.

**Resultados obtenidos**

| # | Requisito | Resultado |
|---|---|---|
| 1 | Inorden del ABB | 20, 30, 40, 50, 60, 70, 80 |
| 2 | Eliminar la matrícula 30 | Tiene dos hijos y la reemplaza su sucesor, el **40**. Inorden: 20, 40, 50, 60, 70, 80, así que el ABB conserva su orden. |
| 3 | Tres solicitudes FIFO y dos recuperaciones LIFO | Se atienden S1, S2 y S3 en orden de llegada. Al deshacer dos veces se recuperan **S3 y luego S2**. |
| 4 | BFS desde PC1 | PC1, SW1, PC3, SRV1, PC2. **PC4 no es alcanzable.** |
| 5 | Recomendaciones para Ana | **Diego** (2 amigos en común) y **Elena** (1 amigo en común) |
| 6 | Eliminar una arista | Al quitar SW1 → SRV1, el BFS queda PC1, SW1, PC3 y pasan a ser no alcanzables SRV1, PC2 y PC4. |

Las capturas de cada paso están en `docs/capturas`.

---

## 8. Complejidad temporal

n = estudiantes · h = altura del ABB · V = equipos · E = conexiones · d = número máximo de amigos · R = candidatos recomendados

| Operación | Complejidad | Explicación |
|---|---|---|
| Insertar en el vector | O(1) amortizado | Solo copia los datos cuando duplica la capacidad |
| Búsqueda secuencial | O(n) | Revisa los elementos uno por uno |
| Eliminar del vector | O(n) | Desplaza los elementos siguientes |
| Insertar al final de la lista enlazada | O(1) | Usa una referencia al último nodo |
| Encolar / desencolar | O(1) | Trabaja con referencias al frente y al final |
| Apilar / desapilar | O(1) | Solo usa la cima |
| Insertar, buscar o eliminar en el ABB | O(log n) promedio, O(n) peor caso | Baja un nivel por comparación. Si las claves llegan ordenadas, el árbol se degenera en una lista. |
| Recorridos del ABB | O(n) | Visita cada nodo una vez |
| BFS y DFS | O(V + E) | Procesa cada equipo y cada conexión una vez |
| Equipos no alcanzables | O(V + E) | Un BFS más una revisión de los equipos |
| Eliminar un equipo | O(V + E) | Revisa todas las listas para borrar las aristas entrantes |
| Recomendar contactos | O(d²·(d + R)) + O(R²) | Recorre amigos de amigos y luego ordena por inserción |

---

## 9. Pruebas

Se definieron **55 pruebas automáticas** y todas pasan:

| Tipo | Cantidad | Qué verifican |
|---|---|---|
| Normales | 19 | Todos los pasos del caso integrador, *Deshacer* y la eliminación de aristas entrantes |
| Extremas | 12 | ABB vacío, eliminar la raíz, ABB degenerado de 100 nodos, crecimiento del vector a 1000 elementos, equipo aislado, empates y usuarios sin amigos |
| Erróneas | 24 | Duplicados, datos nulos o vacíos, matrículas y equipos inexistentes, pesos inválidos, estructuras vacías y amistad consigo mismo |

El detalle de cada caso está en [`docs/matriz-pruebas.md`](docs/matriz-pruebas.md).

---

## 10. Conceptos clave

**Eliminar un nodo del ABB con dos hijos.** Se busca el sucesor inorden: un paso a la derecha y luego todo a la izquierda. Su valor se copia en el nodo que se elimina y después se borra el sucesor, que tiene como máximo un hijo. Así se mantiene la regla *izquierda < nodo < derecha*.

**Uso de la cola en BFS.** La cola atiende los equipos en el mismo orden en que se descubren. Por eso el recorrido avanza por niveles: primero los vecinos directos, luego los vecinos de estos, y así sucesivamente.

**Exclusión de amigos directos.** Al recorrer los amigos de los amigos, se descarta al propio usuario y a quienes ya son sus amigos. Solo los demás cuentan como recomendación.

**Eliminación de aristas entrantes.** En una lista de adyacencia cada equipo solo conoce sus conexiones salientes. Para borrar las que llegan a un equipo hay que revisar las listas de todos los demás equipos.

---

## 11. Conclusiones

- Elegir bien la estructura cambia la eficiencia. Con 7 estudiantes, el ABB encuentra una matrícula con menos de la mitad de comparaciones que la búsqueda secuencial, y la diferencia crece con más datos.
- La cola y la pila resuelven problemas opuestos: la cola garantiza un orden justo de atención (FIFO) y la pila permite revertir acciones en orden inverso (LIFO).
- Los árboles sirven para dos cosas distintas: el árbol general representa jerarquías y el ABB acelera las búsquedas, siempre que no se degenere.
- Los grafos dinámicos permiten analizar redes reales: recorrerlas, encontrar partes desconectadas y obtener recomendaciones a partir de relaciones indirectas.
- Integrar todas las estructuras sobre los mismos datos obligó a mantenerlas consistentes y a validar cada operación entre módulos.

---

## 12. Instalación y ejecución

**Requisitos:** JDK 8 o superior. No necesita librerías externas.

```bash
# 1. Clonar el repositorio
git clone https://github.com/luuissilva/Examen_Practico_Integrador_ED.git
cd Examen_Practico_Integrador_ED

# 2. Compilar (Windows PowerShell)
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# 2. Compilar (Linux / macOS / Git Bash)
javac -encoding UTF-8 -d out $(find src -name "*.java")

# 3. Ejecutar el sistema
java -cp out app.Main

# 4. Ejecutar solo las pruebas
java -cp out pruebas.PruebasSIGAC
```

Si en la consola de Windows las tildes o símbolos se ven como `?`, ejecute `chcp 65001` antes del programa.

**Uso del menú:** la opción **7** ejecuta el caso integrador paso a paso, la **6** carga sus datos para probar a mano y la **8** corre todas las pruebas.

---

## 13. Autor

**Luis Silva** – Ingeniería en Software, Universidad Técnica de Ambato.
Trabajo individual. El registro de commits está en [`docs/contribuciones.md`](docs/contribuciones.md).