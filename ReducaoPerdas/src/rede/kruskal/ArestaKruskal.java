package rede.kruskal;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class ArestaKruskal {

    private Integer id;
    private Integer origem;
    private Integer destino;
    private Integer peso;

    public ArestaKruskal(Integer id, Integer origem, Integer destino, Integer peso) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public Integer getOrigem() {
        return origem;
    }

    public Integer getDestino() {
        return destino;
    }

    public Integer getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "" + id;
    }

}
