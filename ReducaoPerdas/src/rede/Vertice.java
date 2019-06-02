package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Vertice {

    private Integer id;
    private Boolean isFonte = Boolean.FALSE;
    
    public void setFonte() {
        this.isFonte = Boolean.TRUE;
    }

}
