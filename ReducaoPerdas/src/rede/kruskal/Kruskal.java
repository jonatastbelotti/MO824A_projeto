package rede.kruskal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Kruskal {

    private Integer numVertices;
    private Integer numArestas;
    private List<ArestaKruskal> arestas;

    public Kruskal(Integer numVertices, Integer numArestas, List<ArestaKruskal> arestas) {
        this.numVertices = numVertices;
        this.numArestas = numArestas;
        this.arestas = arestas;

        // ordenando as arestas para a execução do Kruskal
        this.arestas.sort(new Comparator<ArestaKruskal>() {
            @Override
            public int compare(ArestaKruskal o1, ArestaKruskal o2) {
                return o1.getPeso().compareTo(o2.getPeso());
            }
        });
    }

    public List<ArestaKruskal> executar() {
        Integer[] chefes = new Integer[numVertices];
        Integer[] tamComponentes = new Integer[numVertices];
        List<ArestaKruskal> arvore = new ArrayList<>();

        // denifindo cada vértice como seu chefe e o tamanho de cada componente como 1
        for (int i = 0; i < chefes.length; i++) {
            chefes[i] = i;
        }
        Arrays.fill(tamComponentes, 1);

        // percorrendo cada aresta do grafo em ordem crescente
        for (ArestaKruskal aresta : arestas) {
            // buscando quem é o chefe de cada vértica da aresta
            int ch1 = buscaChefe(chefes, aresta.getOrigem());
            int ch2 = buscaChefe(chefes, aresta.getDestino());

            // se os chefes dos dois vértices forem o mesmo significa que essa aresta fecha ciclo
            if (ch1 == ch2) {
                continue;
            }

            // a aresta não fecha ciclo, deve ser inserida na árvores
            arvore.add(aresta);

            // A maior componente passa a ser a chefe da menor
            if (tamComponentes[ch1] >= tamComponentes[ch2]) {
                chefes[ch2] = ch1;
                tamComponentes[ch1] += tamComponentes[ch2];
            } else {
                chefes[ch1] = ch2;
                tamComponentes[ch2] += tamComponentes[ch1];
            }

            // se a árvore tem (numVertices - 1) arestas ela já é completa
            if (arvore.size() == numVertices - 1) {
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
