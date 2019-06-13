package rede.kruskal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import rede.Aresta;
import rede.Rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Kruskal {

    private Rede grafo;
    private ArrayList<Aresta> arestasOrdenadas;

    public Kruskal(Rede grafoComPesos) {
        this.grafo = grafoComPesos;
        this.arestasOrdenadas = new ArrayList<>(this.grafo.getArestas());

        // ordenando as arestas para a execução do Kruskal
        Collections.sort(arestasOrdenadas, new Comparator<Aresta>() {
            @Override
            public int compare(Aresta o1, Aresta o2) {
                return o1.getPeso().compareTo(o2.getPeso());
            }
        });
    }

    public Rede executar() {
        Integer[] chefes = new Integer[grafo.getNumVertices()];
        Integer[] tamComponentes = new Integer[grafo.getNumVertices()];

        // Inciando árvore com todos os vértices
        Rede arvore = new Rede(grafo.getNumVertices());
        arvore.setVertices(grafo.getVertices(), grafo.getOrigem());

        // denifindo cada vértice como seu chefe e o tamanho de cada componente como 1
        for (int i = 0; i < chefes.length; i++) {
            chefes[i] = i;
        }
        Arrays.fill(tamComponentes, 1);

        // percorrendo cada aresta do grafo em ordem crescente
        for (Aresta aresta : arestasOrdenadas) {
            // buscando quem é o chefe de cada vértica da aresta
            int ch1 = buscaChefe(chefes, aresta.getV1().getId());
            int ch2 = buscaChefe(chefes, aresta.getV2().getId());

            // se os chefes dos dois vértices forem o mesmo significa que essa aresta fecha ciclo
            if (ch1 == ch2) {
                continue;
            }

            // a aresta não fecha ciclo, deve ser inserida na árvores
            arvore.addAresta(aresta);

            // A maior componente passa a ser a chefe da menor
            if (tamComponentes[ch1] >= tamComponentes[ch2]) {
                chefes[ch2] = ch1;
                tamComponentes[ch1] += tamComponentes[ch2];
            } else {
                chefes[ch1] = ch2;
                tamComponentes[ch2] += tamComponentes[ch1];
            }

            // se a árvore tem (numVertices - 1) arestas ela já é completa
            if (arvore.getSizeArestas() == arvore.getNumVertices() - 1) {
                break;
            }
        }

        return arvore;
    }

    private Integer buscaChefe(Integer[] chefes, Integer v) {
        while (!Objects.equals(chefes[v], v)) {
            v = chefes[v];
        }

        return v;
    }

}
