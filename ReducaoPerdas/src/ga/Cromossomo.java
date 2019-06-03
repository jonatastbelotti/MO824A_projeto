package ga;

import java.util.ArrayList;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Cromossomo extends ArrayList<Integer> {

    private Double fitness = Double.POSITIVE_INFINITY;

    Cromossomo(Cromossomo c) {
        super(c);
        this.fitness = c.fitness;
    }

    public Double getFitness() {
        return fitness;
    }

}
