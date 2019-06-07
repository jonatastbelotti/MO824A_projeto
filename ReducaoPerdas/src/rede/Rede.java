package rede;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Rede {

    private Integer numVertices;
    private Integer numArestas;
    private Integer numChaves;
    private Integer numFontes;
    private Vertice[] vertices;
    private Aresta[] arestas;

    public Rede(String nomeArquivo) throws IOException {
        carregarArquivo(nomeArquivo);
    }

    /**
     *
     * @param nomeArquivo
     * @throws IOException
     */
    private void carregarArquivo(String nomeArquivo) throws IOException {
        FileReader arq = new FileReader(nomeArquivo);
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();
        Boolean lendoAresta = Boolean.FALSE;
        Boolean lendoChave = Boolean.FALSE;
        int ID = 0, SRC_BUS = 1, REC_BUS = 2, R = 3, X = 4, PL_KW = 5, QL_KVAR = 6, S_NS = 7;
        int id, id_origem, id_destino;

        // Percorrendo todas as linhas do arquivo
        while (linha != null) {
            // removendo caracteres de final de linha
            linha = linha.replaceAll("\n", "");
            linha = linha.replaceAll("\r", "");
            linha = linha.replaceAll("\t", " ");

            // separando as partes da linha por espaço
            String[] partes = linha.split("\\s+");

            if (partes[0].startsWith(".")) {
                lendoAresta = Boolean.FALSE;
                lendoChave = Boolean.FALSE;

                switch (partes[0]) {
                    case ".Tie_count":
                        this.numChaves = Integer.parseInt(partes[1]);
                        break;
                    case ".Branch_count":
                        this.numArestas = Integer.parseInt(partes[1]);
                        this.arestas = new Aresta[this.numArestas];
                        break;
                    case ".Nodes":
                        this.numVertices = Integer.parseInt(partes[1]);
                        this.vertices = new Vertice[numVertices];

                        for (int i = 0; i < this.vertices.length; i++) {
                            this.vertices[i] = new Vertice(i);
                        }

                        break;
                    case ".Feeders":
                        this.numFontes = Integer.parseInt(partes[1]);
                        break;
                    case ".Feeder_node_ids":
                    case ".Feder_node_ids":
                        Arrays.stream(partes, 1, partes.length).forEach(p -> vertices[Integer.parseInt(p)].setFonte());
                        break;
                    case ".Branch":
                        lendoAresta = Boolean.TRUE;

                        for (int i = 1; i < partes.length; i++) {
                            switch (partes[i]) {
                                case "Src_bus":
                                    SRC_BUS = i;
                                    break;
                                case "Rec_bus":
                                    REC_BUS = i;
                                    break;
                                case "R":
                                    R = i;
                                    break;
                                case "X":
                                    X = i;
                                    break;
                                case "PL_kw":
                                    PL_KW = i;
                                    break;
                                case "QL_kvar":
                                    QL_KVAR = i;
                                    break;
                                case "S_NS":
                                    S_NS = i;
                                    break;
                            }
                        }

                        break;
                    case ".Tie":
                        lendoChave = Boolean.TRUE;
                        break;
                }
            } else if (lendoAresta || lendoChave) {
                id = Integer.parseInt(partes[ID]);
                id_origem = Integer.parseInt(partes[SRC_BUS]);
                id_destino = Integer.parseInt(partes[REC_BUS]);

                this.arestas[id] = new Aresta(id, this.vertices[id_origem], this.vertices[id_destino]);
                this.arestas[id].setR(Double.parseDouble(partes[R]));
                this.arestas[id].setX(Double.parseDouble(partes[X]));
                this.vertices[id_origem].addAresta(this.arestas[id]);

                if (lendoAresta) {
                    this.arestas[id].getDestino().setPL_kw(Double.parseDouble(partes[PL_KW]));
                    this.arestas[id].getDestino().setQL_kvar(Double.parseDouble(partes[QL_KVAR]));

                    if (partes[S_NS].equals("S")) {
                        this.arestas[id].setChave();
                    }
                }

                if (lendoChave) {
                    this.arestas[id].setChave();
                }
            }

            // lendo a próxima linha
            linha = lerArq.readLine();
        }

    }

    public Integer getNumVertices() {
        return numVertices;
    }

    public Integer getNumArestas() {
        return numArestas;
    }

    public Integer getNumChaves() {
        return numChaves;
    }

    public Integer getNumFontes() {
        return numFontes;
    }

    public Aresta getAresta(Integer id) {
        return this.arestas[id];
    }

    @Override
    public String toString() {
        String resp = "Rede com " + this.numVertices + " nós e " + this.numArestas + " arestas.";

        resp += "\nSendo:";
        resp += "\n " + this.numFontes + " fontes";
        resp += "\n " + this.numChaves + " chaves";

        return resp;
    }

    public static void main(String[] args) throws IOException {
        String arquivo = "instances/bus_10476_84.pos";

        Rede rede = new Rede(arquivo);
        System.out.println(rede);
    }

}
