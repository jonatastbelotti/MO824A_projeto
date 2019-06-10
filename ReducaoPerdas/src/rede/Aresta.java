package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Aresta {

    private Integer id;
    private Vertice origem;
    private Vertice destino;
    private Double R = 0D;
    private Double X = 0D;
    private Boolean S_NS;
    private Boolean isChave = Boolean.FALSE;
    private Integer peso = -1;
    public Potencia potencia;

    public Aresta(Integer id, Vertice origem, Vertice destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
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
        return "" + id + " -> ori: " + origem.getId() + " dest: " + destino.getId() + " peso: " + peso;
    }

}
