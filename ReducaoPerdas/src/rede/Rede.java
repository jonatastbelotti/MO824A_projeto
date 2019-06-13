package rede;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

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
    private ArrayList<Aresta>[] listaAdjacencia;
    private ArrayList<Vertice> fontes;
    private Vertice origem = null;
    private Integer largura = 10;
    private Integer altura = 10;

    public Rede(Integer numVertices) {
        this.iniciarRede(numVertices);
    }

    public Rede(String nomeArquivo) throws IOException {
        carregarArquivo(nomeArquivo);
    }

    public Rede(Rede rede) {
        this.numVertices = rede.numVertices;
        this.numArestas = rede.numArestas;
        this.numChaves = rede.numChaves;
        this.numFontes = rede.numFontes;
        this.vertices = rede.vertices;
        this.arestas = new ArrayList<>(rede.arestas);
        this.fontes = new ArrayList<>(rede.fontes);
        this.origem = rede.origem;
        this.largura = rede.largura;
        this.altura = rede.altura;

        this.listaAdjacencia = new ArrayList[rede.listaAdjacencia.length];
        for (int i = 0; i < rede.listaAdjacencia.length; i++) {
            this.listaAdjacencia[i] = new ArrayList<>(rede.listaAdjacencia[i]);
        }
    }

    private void iniciarRede(Integer numVertices) {
        this.numVertices = numVertices;
        this.vertices = new Vertice[this.numVertices];
        this.arestas = new ArrayList<>();
        this.listaAdjacencia = new ArrayList[this.numVertices];
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
        for (int i = 0; i < this.listaAdjacencia.length; i++) {
            this.listaAdjacencia[i] = new ArrayList<>();
        }
    }

    private void adicionarAresta(Aresta aresta) {
        Vertice v1, v2;

        while (this.arestas.size() < aresta.getId() + 1) {
            this.arestas.add(null);
        }

        this.arestas.set(aresta.getId(), aresta);

        v1 = aresta.getV1();
        v2 = aresta.getV2();
        this.listaAdjacencia[v1.getId()].add(aresta);
        this.listaAdjacencia[v2.getId()].add(aresta);
    }

    public void addAresta(Aresta aresta) {
        Vertice v1, v2;

        this.arestas.add(aresta);

        v1 = aresta.getV1();
        v2 = aresta.getV2();

        this.listaAdjacencia[v1.getId()].add(aresta);
        this.listaAdjacencia[v2.getId()].add(aresta);
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
        Boolean lendoCoordenadas = Boolean.FALSE;
        int ID = 0, SRC_BUS = 1, REC_BUS = 2, R = 3, X = 4, PL_KW = 5, QL_KVAR = 6, S_NS = 7, IND_X = 1, IND_Y = 2;
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
                lendoCoordenadas = Boolean.FALSE;

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
                    case ".Node":
                        lendoCoordenadas = Boolean.TRUE;
                        for (int i = 1; i < partes.length; i++) {
                            if (partes[i].equals("x_coord")) {
                                IND_X = i;
                            } else {
                                IND_Y = i;
                            }
                        }
                        break;
                    case ".Grid_size":
                        lendoCoordenadas = Boolean.TRUE;
                        this.largura = Integer.parseInt(partes[IND_X]);
                        this.altura = Integer.parseInt(partes[IND_Y]);

                        if (origem != null) {
                            origem.setcoordenadas(largura / 2, altura);
                        }
                        break;
                    default:
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
                    aresta.getV2().setPotencias(Double.parseDouble(partes[PL_KW]), Double.parseDouble(partes[QL_KVAR]));
                    aresta.setS_NS(partes[S_NS].equals("S"));
                }

                if (lendoChave) {
                    aresta.setChave();
                }

                adicionarAresta(aresta);
            } else if (lendoCoordenadas) {
                vertices[Integer.parseInt(partes[0])].setcoordenadas(Integer.parseInt(partes[IND_X]), Integer.parseInt(partes[IND_Y]));
            }

            // lendo a próxima linha
            linha = lerArq.readLine();
        }

    }

    public Iterable<Aresta> getArestasVertice(Vertice vertice) {
        return this.getArestasVertice(vertice.getId());
    }

    public Iterable<Aresta> getArestasVertice(Integer idVertice) {
        return this.listaAdjacencia[idVertice];
    }

//    public Double calcularPerda() {
//        Double p = 0D;
//        
//        Rede arvore = new Rede(this);
//        
//
//        // Calculando a potencia total em cada aresta
//        calcularPotencias(arvore);
//
//        // Passa por todas as arestas somando a perda de cada uma
//        for (Aresta a : arvore.getArestas()) {
//            if (a != null) {
//                p += a.getR() * Math.pow(a.potencia.PL + a.potencia.QL, 2D);
//            }
//        }
//
//        return p;
//    }
//
//    private void calcularPotencias(Rede arvore) {
//        for (Aresta aresta : arvore.getArestasSaindoDe()[arvore.getOrigem().getId()]) {
//            aresta.potencia = calcPotenciaAresta(arvore, aresta);
//        }
//    }
//    private Potencia calcPotenciaAresta(Rede arvore, Aresta aresta) {
//        aresta.potencia = new Potencia();
//
//        // Iniciando com os valores do vertice de destino
//        aresta.potencia.PL = aresta.getDestino().getCarga_PL_kw();
//        aresta.potencia.QL = aresta.getDestino().getCarga_QL_kvar();
//
//        // Somando com a potencias de todas as arestas que partem do vertice de destino
//        for (Aresta aresta_aux : arvore.getArestasSaindoDe()[aresta.getDestino().getId()]) {
//            Potencia p_aux = calcPotenciaAresta(arvore, aresta_aux);
//
//            aresta.potencia.PL += p_aux.PL;
//            aresta.potencia.QL += p_aux.QL;
//        }
//
//        return aresta.potencia;
//    }
    public Integer getNumVertices() {
        return numVertices;
    }

    public Vertice[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertice[] vertices, Vertice origem) {
        this.vertices = vertices;
        this.origem = origem;
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

    public Vertice getOrigem() {
        return this.origem;
    }

    @Override
    public String toString() {
        String resp = "Rede com " + this.numVertices + " nós e " + this.numArestas + " arestas.";

        resp += "\nSendo:";
        resp += "\n " + this.numFontes + " fontes";
        resp += "\n " + this.numChaves + " chaves";
        resp += "\n " + this.vertices.length + " vertices totais";
        resp += "\n " + this.arestas.size() + " arestas totais";

        return resp;
    }

    public void plotarGrafico(String nomeArquivo) {
        plotarGrafico(nomeArquivo, false);
    }

    /**
     * Gráficos criados com a biblioteca https://github.com/knowm/XChart
     * Documentação em:
     * https://knowm.org/open-source/xchart/xchart-example-code/
     *
     * @param nomeArquivo
     */
    public void plotarGrafico(String nomeArquivo, boolean showGrafico) {
        double[] x_, y_;
        XYChart chart = new XYChartBuilder().width(600).height(400).title(nomeArquivo).xAxisTitle("X").yAxisTitle("Y").build();
        chart.getStyler().setLegendVisible(false);

        // Plotando arestas
        for (Aresta aresta : this.arestas) {
            if (aresta != null && !aresta.getV1().equals(origem) && !aresta.getV2().equals(origem)) {
                x_ = new double[]{aresta.getV1().getX(), aresta.getV2().getX()};
                y_ = new double[]{aresta.getV1().getY(), aresta.getV2().getY()};

                XYSeries series = chart.addSeries("serie aresta " + aresta.getId(), x_, y_);
                series.setLineColor(Color.BLACK);
                series.setLineStyle(SeriesLines.SOLID);
                series.setMarker(SeriesMarkers.NONE);
            }
        }

        // Plotando vertices
        for (Vertice vertice : vertices) {
            if (!vertice.equals(origem)) {
                x_ = new double[]{vertice.getX()};
                y_ = new double[]{vertice.getY()};

                XYSeries series = chart.addSeries("serie vertice " + vertice.getId(), x_, y_);
                series.setMarker(SeriesMarkers.CIRCLE);

                if (vertice.getIsFonte()) {
                    series.setMarkerColor(Color.RED);
                } else {
                    series.setMarkerColor(Color.BLACK);
                }
            }
        }

        if (showGrafico) {
            new SwingWrapper<>(chart).displayChart();
        }

        try {
            // Save it
            BitmapEncoder.saveBitmap(chart, nomeArquivo, BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ex) {
            Logger.getLogger(Rede.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        String arquivo;
        arquivo = "instances/bus_13_3.pos";
//        arquivo = "instances/bus_29_1.pos";
//        arquivo = "instances/bus_32_1.pos";
//        arquivo = "instances/bus_83_11.pos";
//        arquivo = "instances/bus_135_8.pos";
//        arquivo = "instances/bus_201_3.pos";
//        arquivo = "instances/bus_873_7.pos";
//        arquivo = "instances/bus_10476_84.pos";

        Rede rede = new Rede(arquivo);
        System.out.println(rede);
//        System.out.printf("Perda: %f\n", rede.calcularPerda());

//        rede.plotarGrafico(arquivo.replace(".pos", ""));
    }

}
