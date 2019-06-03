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

}
