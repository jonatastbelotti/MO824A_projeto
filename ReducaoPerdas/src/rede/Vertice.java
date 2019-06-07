package rede;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Vertice {

    private Integer id;
    private Boolean isFonte = Boolean.FALSE;
    private List<Aresta> arestasDestino;
    private Double PL_kw;
    private Double QL_kvar;

    public Vertice(Integer id) {
        this.id = id;
        this.arestasDestino = new ArrayList<>();
    }

    public void setFonte() {
        this.isFonte = Boolean.TRUE;
    }

    public void addAresta(Aresta aresta) {
        this.arestasDestino.add(aresta);
    }

    public Integer getId() {
        return id;
    }

    public Boolean getIsFonte() {
        return isFonte;
    }

    public List<Aresta> getArestasDestino() {
        return arestasDestino;
    }

    public Double getPL_kw() {
        return PL_kw;
    }

    public void setPL_kw(Double PL_kw) {
        this.PL_kw = PL_kw;
    }

    public Double getQL_kvar() {
        return QL_kvar;
    }

    public void setQL_kvar(Double QL_kvar) {
        this.QL_kvar = QL_kvar;
    }

}
