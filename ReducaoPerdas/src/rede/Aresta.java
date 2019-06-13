package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Aresta {

    private Integer id;
    private Vertice v1;
    private Vertice v2;
    private Double R = 0D;
    private Double X = 0D;
    private Boolean S_NS;
    private Boolean isChave = Boolean.FALSE;
    private Integer peso = -1;
    public Potencia potencia;

    public Aresta(Integer id, Vertice v1, Vertice v2) {
        this.id = id;
        this.v1 = v1;
        this.v2 = v2;
        potencia = new Potencia();
    }

    public void setR(Double R) {
        this.R = R;
    }

    public void setX(Double X) {
        this.X = X;
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

    public Vertice getV1() {
        return v1;
    }

    public Vertice getV2() {
        return v2;
    }

    public Double getR() {
        return R;
    }

    public Double getX() {
        return X;
    }

    public Boolean getS_NS() {
        return S_NS;
    }

    public Boolean getIsChave() {
        return isChave;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return String.format("%d: %d - %d -> peso: %d", id, v1.getId(), v2.getId(), peso);
    }

}
