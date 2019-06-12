package ga;

import java.util.ArrayList;
import java.util.Random;
import rede.Aresta;
import rede.Potencia;
import rede.Rede;
import rede.kruskal.Kruskal;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Cromossomo extends ArrayList<Integer> {

    private Double fitness = Double.POSITIVE_INFINITY;

    public Cromossomo() {
        super();
    }

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
        Rede arvoreRede = extrairArvore(rede);

        // Calculando perda
        Double perda = calcularPerda(arvoreRede);

        fitness = perda;
    }

    public Double getFitness() {
        return fitness;
    }

    private Rede extrairArvore(Rede rede) {
        Kruskal kruskal;

        // Atualizando os pesos das arestas com os genes do cromossomo
        for (int i = 0; i < this.size(); i++) {
            rede.getAresta(i).setPeso(this.get(i));
        }

        // instanciando objeto Kruskal
        kruskal = new Kruskal(rede);

        return kruskal.executar();
    }

    private Double calcularPerda(Rede arvore) {
        Double p = 0D;

        // Calculando a potencia total em cada aresta
        calcularPotencias(arvore);

        // Passa por todas as arestas somando a perda de cada uma
        for (Aresta a : arvore.getArestas()) {
            p += a.getR() * Math.pow(a.potencia.PL + a.potencia.QL, 2D);
        }

        return p;
    }

    private void calcularPotencias(Rede arvore) {
        for (Aresta aresta : arvore.getArestasSaindoDe()[arvore.getNumVertices() - 1]) {
            aresta.potencia = calcPotenciaAresta(arvore, aresta);
        }
    }

    private Potencia calcPotenciaAresta(Rede arvore, Aresta aresta) {
        aresta.potencia = new Potencia();

        // Iniciando com os valores do vertice de destino
        aresta.potencia.PL = aresta.getDestino().getCarga_PL_kw();
        aresta.potencia.QL = aresta.getDestino().getCarga_QL_kvar();

        // Somando com a potencias de todas as arestas que partem do vertice de destino
        for (Aresta aresta_aux : arvore.getArestasSaindoDe()[aresta.getDestino().getId()]) {
            Potencia p_aux = calcPotenciaAresta(arvore, aresta_aux);

            aresta.potencia.PL += p_aux.PL;
            aresta.potencia.QL += p_aux.QL;
        }

        return aresta.potencia;
    }
    
    public void plotarRede(String nomeArquivo, Rede redeOriginal) {
        // extraindo a árvore geradora de custo mínimo
        Rede arvoreRede = extrairArvore(redeOriginal);
        
        arvoreRede.plotarGrafico(nomeArquivo);
    }

    @Override
    public String toString() {
        return super.toString() + " fitness = " + fitness;
    }

}
