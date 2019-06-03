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

}
