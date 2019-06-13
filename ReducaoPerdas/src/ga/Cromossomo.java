package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rede.Aresta;
import rede.Potencia;
import rede.Rede;
import rede.Vertice;
import rede.kruskal.Kruskal;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Cromossomo extends ArrayList<Integer> {

    private List<Boolean> arestaUsada = new ArrayList<>();
    private Double fitness = Double.POSITIVE_INFINITY;
    private Integer pesoMaximo = 0;
    private boolean[] verticeVisitado;

    public Cromossomo() {
        super();
    }

    public Cromossomo(Integer numGenes) {
        super();
        pesoMaximo = numGenes;
        List<Integer> itens = new ArrayList<>();

        for (int i = 1; itens.size() < numGenes; i++) {
            itens.add(i);
        }

        Collections.shuffle(itens);

        while (size() < numGenes) {
            this.add(itens.get(0));
            itens.remove(0);

            this.arestaUsada.add(Boolean.FALSE);
        }
    }

    Cromossomo(Cromossomo c) {
        super(c);
        this.fitness = new Double(c.fitness);
        this.arestaUsada = new ArrayList<>(c.arestaUsada);
    }

    private void copiarCromossomo(Cromossomo c) {
        this.clear();
        this.addAll(c);
        this.fitness = c.fitness;
        this.arestaUsada = new ArrayList<>(c.arestaUsada);
    }

    @Override
    public boolean add(Integer e) {
        this.arestaUsada.add(Boolean.FALSE);
        if (e > pesoMaximo) {
            pesoMaximo = e;
        }

        return super.add(e);
    }

    void calcularFitness(Rede rede) {
        // extraindo a árvore geradora de custo mínimo
        Rede arvoreRede = extrairArvore(rede);

        // marcando as arstas usadas
        marcarArestasUsadas(arvoreRede);

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
        // Marcando todos os vertices como não visitados
        verticeVisitado = new boolean[arvore.getNumVertices()];
        for (int i = 0; i < this.verticeVisitado.length; i++) {
            verticeVisitado[i] = false;
        }

        // marcando a origem como já visitado
        verticeVisitado[arvore.getOrigem().getId()] = true;

        // Calculando as potencias de cada árvore partindo das estações de distribuição
        for (Aresta aresta : arvore.getArestasVertice(arvore.getOrigem())) {
            aresta.potencia = calcPotenciaAresta(arvore, aresta, arvore.getOrigem());
        }
    }

    private Potencia calcPotenciaAresta(Rede arvore, Aresta aresta, Vertice vindoDe) {
        Vertice indoPara;

        // Identificando qual a origem e o detino da aresta
        if (aresta.getV1().equals(vindoDe)) {
            indoPara = aresta.getV2();
        } else {
            indoPara = aresta.getV1();
        }

        // Verificando se esse vertice já foi visitado
        if (verticeVisitado[indoPara.getId()]) {
            return new Potencia();
        } else {
            verticeVisitado[indoPara.getId()] = true;
        }

        // Iniciando com os valores do vertice de destino
        aresta.potencia = new Potencia();
        aresta.potencia.PL = indoPara.getCarga_PL_kw();
        aresta.potencia.QL = indoPara.getCarga_QL_kvar();

        // Somando com a potencias de todas as arestas que partem do vertice de destino e ainda não foram visitadas
        for (Aresta aresta_aux : arvore.getArestasVertice(indoPara)) {
            Potencia p_aux = calcPotenciaAresta(arvore, aresta_aux, indoPara);

            aresta.potencia.PL += p_aux.PL;
            aresta.potencia.QL += p_aux.QL;
        }

        return aresta.potencia;
    }

    private void marcarArestasUsadas(Rede arvore) {
        // marcando todas como não usadas
        for (int i = 0; i < arestaUsada.size(); i++) {
            this.arestaUsada.set(i, Boolean.FALSE);
        }

        // percorrendo todas as arestas usadas e marcando
        for (Aresta aresta : arvore.getArestas()) {
            if (arestaUsada.size() > aresta.getId()) {
                this.arestaUsada.set(aresta.getId(), Boolean.TRUE);
            }
        }
    }

    void mutarGene(int gene) {
        if (arestaUsada.get(gene)) {
            set(gene, pesoMaximo);
        } else {
            set(gene, 0);
        }
    }

    void realizarBucaLocal(Rede rede) {
        Cromossomo melhor = new Cromossomo(this);
        boolean melhorou = false;

        // Percorre todas as arestas em uso e tenta trocar por alguma sem uso
        for (Integer ind_uso : buscarArestasEmUso()) {
            for (Integer ind_sem_uso : buscarArestasSemUso()) {
                Cromossomo cAux = new Cromossomo(this);

                // Trocando as aresta e recalculando o fitness
                cAux.set(ind_uso, this.get(ind_sem_uso));
                cAux.set(ind_sem_uso, this.get(ind_uso));
                cAux.calcularFitness(rede);

                // Verificando se melhorou
                if (cAux.fitness < melhor.fitness) {
                    melhor = new Cromossomo(cAux);
                    melhorou = true;
                }
            }
        }

        // Se a busca local melhorou a solução atualiza o Cromossomo
        if (melhorou) {
            this.copiarCromossomo(melhor);
        }
    }

    public List<Integer> buscarArestasEmUso() {
        List<Integer> resp = new ArrayList<>();

        // Percorre o vetor e salva as posições em uso
        for (int i = 0; i < arestaUsada.size(); i++) {
            if (arestaUsada.get(i)) {
                resp.add(i);
            }
        }

        return resp;
    }

    public List<Integer> buscarArestasSemUso() {
        List<Integer> resp = new ArrayList<>();

        // Percorre o vetor e salva as posições em uso
        for (int i = 0; i < arestaUsada.size(); i++) {
            if (!arestaUsada.get(i)) {
                resp.add(i);
            }
        }

        return resp;
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
