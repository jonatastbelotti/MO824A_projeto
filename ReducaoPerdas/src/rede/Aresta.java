package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Aresta {

    private Integer id;
    private Vertice origem;
    private Vertice destino;
    private Double R;
    private Double X;
    private Boolean isChave = Boolean.FALSE;

    public Aresta(Integer id, Vertice origem, Vertice destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
    }

    public void setR(Double R) {
        this.R = R;
    }

    public void setX(Double X) {
        this.X = X;
    }

    public void setChave() {
        this.isChave = Boolean.TRUE;
    }

    public Integer getId() {
        return id;
    }

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public Double getR() {
        return R;
    }

    public Double getX() {
        return X;
    }

    public Boolean getIsChave() {
        return isChave;
    }

    @Override
    public String toString() {
        return "" + id;
    }

}
