package estructuras;

/** Arista dirigida y ponderada: origen -> destino con un peso (latencia/costo). */
public class Arista {
    private final Vertice destino;
    private final int peso;

    public Arista(Vertice destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice getDestino() { return destino; }
    public int getPeso() { return peso; }

    @Override
    public String toString() { return destino.getNombre() + "(" + peso + ")"; }
}
