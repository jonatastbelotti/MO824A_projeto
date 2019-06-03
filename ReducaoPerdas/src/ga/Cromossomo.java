package ga;

import java.util.ArrayList;
import java.util.Random;
import rede.Rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Cromossomo extends ArrayList<Integer> {

    private Double fitness = Double.POSITIVE_INFINITY;

    public Cromossomo(Integer numGenes) {
        super();
        Random random = new Random();

        while (size() < numGenes) {
            add(random.nextInt(numGenes));
        }
    }

    Cromossomo(Cromossomo c) {
        super(c);
        this.fitness = c.fitness;
    }

    void calcularFitness(Rede rede) {

    }

    public Double getFitness() {
        return fitness;
    }

}
