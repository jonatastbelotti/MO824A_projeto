package rede;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Rede {

    private Integer numVertices = 0;
    private Integer numArestas = 0;
    private Integer numChaves = 0;
    private Integer numFontes = 0;
    private Vertice[] vertices;
    private ArrayList<Aresta> arestas;
    private ArrayList<Aresta>[] arestasSaindoDe;
    private ArrayList<Vertice> fontes;
    private Vertice origem;

    public Rede(Integer numVertices) {
        this.iniciarRede(numVertices);
    }

    public Rede(String nomeArquivo) throws IOException {
        carregarArquivo(nomeArquivo);
    }

    private void iniciarRede(Integer numVertices) {
        this.numVertices = numVertices;
        this.vertices = new Vertice[this.numVertices];
        this.arestas = new ArrayList<>();
        this.arestasSaindoDe = new ArrayList[this.numVertices];
        this.fontes = new ArrayList<>();

        // Iniciando vetor de vertices
        for (int i = 0; i < this.vertices.length; i++) {
            this.vertices[i] = new Vertice(i);
        }

        // Iniciando lista de arestas
        while (this.arestas.size() < this.numArestas) {
            this.arestas.add(null);
        }

        // Iniciando lista de adjacencia
        for (int i = 0; i < this.arestasSaindoDe.length; i++) {
            this.arestasSaindoDe[i] = new ArrayList<>();
        }
    }

    private void adicionarAresta(Aresta aresta) {
        while (this.arestas.size() < aresta.getId() + 1) {
            this.arestas.add(null);
        }

        this.arestas.set(aresta.getId(), aresta);
        this.arestasSaindoDe[aresta.getOrigem().getId()].add(aresta);
    }

    public void addAresta(Aresta aresta) {
        this.arestas.add(aresta);
        this.arestasSaindoDe[aresta.getOrigem().getId()].add(aresta);
    }

    private void adicionarFonte(int codVertice) {
        this.vertices[codVertice].setFonte();
        this.fontes.add(this.vertices[codVertice]);

        // Adicionando uma aresta entre o novo vertice
        Aresta aresta = new Aresta(this.arestas.size(), origem, this.vertices[codVertice]);
        adicionarAresta(aresta);
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
                        this.arestas = new ArrayList<>();

                        while (this.arestas.size() < this.numArestas) {
                            this.arestas.add(null);
                        }
                        break;
                    case ".Nodes":
                        this.iniciarRede(Integer.parseInt(partes[1]) + 1);
                        this.origem = this.vertices[this.vertices.length - 1];
                        break;
                    case ".Feeders":
                        this.numFontes = Integer.parseInt(partes[1]);
                        break;
                    case ".Feeder_node_ids":
                    case ".Feder_node_ids":
                        Arrays.stream(partes, 1, partes.length).forEach(p -> {
                            adicionarFonte(Integer.parseInt(p));
                        });
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

                Aresta aresta = new Aresta(id, this.vertices[id_origem], this.vertices[id_destino]);
                aresta.setR(Double.parseDouble(partes[R]));
                aresta.setX(Double.parseDouble(partes[X]));

                if (lendoAresta) {
                    aresta.getDestino().setPotencias(Double.parseDouble(partes[PL_KW]), Double.parseDouble(partes[QL_KVAR]));
                    aresta.setS_NS(partes[S_NS].equals("S"));
                }

                if (lendoChave) {
                    aresta.setChave();
                }

                adicionarAresta(aresta);
            }

            // lendo a próxima linha
            linha = lerArq.readLine();
        }

    }

    public Integer getNumVertices() {
        return numVertices;
    }

    public Vertice[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertice[] vertices) {
        this.vertices = vertices;
    }

    public Integer getNumArestas() {
        return numArestas;
    }

    public Integer getSizeArestas() {
        return this.arestas.size();
    }

    public Integer getNumChaves() {
        return numChaves;
    }

    public Integer getNumFontes() {
        return numFontes;
    }

    public ArrayList<Aresta> getArestas() {
        return arestas;
    }

    public Aresta getAresta(Integer id) {
        return this.arestas.get(id);
    }

    public ArrayList<Aresta>[] getArestasSaindoDe() {
        return arestasSaindoDe;
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
        String arquivo = "instances/bus_83_11.pos";

        Rede rede = new Rede(arquivo);
        System.out.println(rede);
    }

}
