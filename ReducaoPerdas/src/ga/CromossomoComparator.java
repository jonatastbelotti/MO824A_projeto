package ga;

import java.util.Comparator;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class CromossomoComparator implements Comparator<Cromossomo> {

    @Override
    public int compare(Cromossomo c1, Cromossomo c2) {
        return c1.getFitness().compareTo(c2.getFitness());
    }
    
}
