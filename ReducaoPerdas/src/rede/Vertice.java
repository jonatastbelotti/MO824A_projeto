package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Vertice {

    private Integer id;
    private Boolean isFonte = Boolean.FALSE;
    private Double carga_PL_kw = 0D;
    private Double carga_QL_kvar = 0D;

    public Vertice(Integer id) {
        this.id = id;
    }

    public void setFonte() {
        this.isFonte = Boolean.TRUE;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getIsFonte() {
        return isFonte;
    }

    public void setPotencias(Double PL_kw, Double QL_kvar) {
        this.carga_PL_kw = PL_kw;
        this.carga_QL_kvar = QL_kvar;
    }

    public Double getCarga_PL_kw() {
        return carga_PL_kw;
    }

    public Double getCarga_QL_kvar() {
        return carga_QL_kvar;
    }

}
