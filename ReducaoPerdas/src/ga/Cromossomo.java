package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rede.Aresta;
import rede.Rede;
import rede.kruskal.ArestaKruskal;
import rede.kruskal.Kruskal;

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
        // extraindo a árvore geradora de custo mínimo
        extrairArvore(rede);

    }

    public Double getFitness() {
        return fitness;
    }

    private void extrairArvore(Rede rede) {
        Kruskal kruskal;
        List<ArestaKruskal> listArestas = new ArrayList<>();
        
//      // Criando lista de arestas com os pesos do cromossomo
        for (int i = 0; i < this.size(); i++) {
            Aresta a = rede.getAresta(i);
            ArestaKruskal aresta = new ArestaKruskal(a.getId(), a.getOrigem().getId(), a.getDestino().getId(), this.get(i));
            listArestas.add(aresta);
        }
        
        // instanciando objeto Kruskal
        kruskal = new Kruskal(rede.getNumVertices(), rede.getNumArestas(), listArestas);
        
        System.out.println(kruskal.executar());
    }

}
