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
    private Double PL_kw;
    private Double QL_kvar;
    private Boolean S_NS;
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

    public void setPL_kw(Double PL_kw) {
        this.PL_kw = PL_kw;
    }

    public void setQL_kvar(Double QL_kvar) {
        this.QL_kvar = QL_kvar;
    }

    public void setS_NS(Boolean S_NS) {
        this.S_NS = S_NS;
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

    public Double getPL_kw() {
        return PL_kw;
    }

    public Double getQL_kvar() {
        return QL_kvar;
    }

    public Boolean getS_NS() {
        return S_NS;
    }

    public Boolean getIsChave() {
        return isChave;
    }

    @Override
    public String toString() {
        return "" + id;
    }

}
