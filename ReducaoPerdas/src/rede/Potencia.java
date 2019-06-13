package rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Potencia {

    public Double PL = 0D;
    public Double QL = 0D;

    @Override
    public String toString() {
        return String.format("PL = %f QL = %f", PL, QL);
    }

}
