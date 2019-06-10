package ga;

import ga.configuracoes.TipoCruzamento;
import ga.configuracoes.TipoMutacao;
import ga.configuracoes.TipoSelecao;
import ga.configuracoes.TipoSelecaoNovaPopulacao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rede.Rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class GA {

    private Rede rede;
    private Integer tamPopulacao;
    private ArrayList<Cromossomo> populacao;
    private Cromossomo melhorSolucao;
    private Boolean verbose = Boolean.TRUE;
    private TipoSelecao tipoSelecao;
    private TipoCruzamento tipoCruzamento;
    private TipoMutacao tipoMutacao;
    private Double taxaMutacao = 0D;
    private TipoSelecaoNovaPopulacao tipoSelecaoNovaPopulacao;

    public GA(Rede rede, Integer tamPopulacao, TipoSelecao selecao, TipoCruzamento cruzamento, TipoMutacao mutacao, Double taxMutacao, TipoSelecaoNovaPopulacao selecaoNovaPop) {
        this.rede = rede;
        this.tamPopulacao = tamPopulacao;
        this.tipoSelecao = selecao;
        this.tipoCruzamento = cruzamento;
        this.tipoMutacao = mutacao;
        this.taxaMutacao = taxMutacao;
        this.tipoSelecaoNovaPopulacao = selecaoNovaPop;
    }

    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
    }

    public Cromossomo executar(Integer tempoExecucao) {
        Integer geracao;
        Long tempInicial;

        // criando população inicial
        tempInicial = System.currentTimeMillis();
        geracao = 0;
        this.iniciarPopulacao();

        // avaliando população inicial
        this.avaliarPopulacao(this.populacao);

        // selecionando melhor individuo
        this.melhorSolucao = new Cromossomo(this.buscarMelhorCromossomo(this.populacao));
        this.imprimirResultadoAtual(geracao, this.melhorSolucao);

        // executando iterações do algoritmo
        while (((System.currentTimeMillis() - tempInicial) / 1000D) < tempoExecucao) {
            // incrementando geração
            geracao++;

            // selecionando pais para o cruzamento
            ArrayList<Cromossomo> pais = this.selecionarPais(this.populacao);

            // gerando os filhos através do cruzamento
            ArrayList<Cromossomo> filhos = this.realizarCruzamento(pais);

            // realizando mutação
            filhos = this.realizarMutacao(filhos);

            // avalia novos individuos
            this.avaliarPopulacao(filhos);

            // seleciona nova população
            this.populacao = this.selecionarNovaPopulacao(filhos);

            // atualizando melhor solução
            Cromossomo atual = this.buscarMelhorCromossomo(this.populacao);

            if (atual.getFitness() < this.melhorSolucao.getFitness()) {
                this.melhorSolucao = new Cromossomo(atual);
                this.imprimirResultadoAtual(geracao, this.melhorSolucao);
            }
        }

        return this.melhorSolucao;
    }

    private void iniciarPopulacao() {
        this.populacao = new ArrayList<Cromossomo>();

        while (this.populacao.size() < this.tamPopulacao) {
            this.populacao.add(new Cromossomo(this.rede.getNumArestas()));
        }
    }

    private void avaliarPopulacao(List<Cromossomo> pop) {
        for (Cromossomo c : pop) {
            c.calcularFitness(this.rede);
        }
    }

    private Cromossomo buscarMelhorCromossomo(List<Cromossomo> pop) {
        Cromossomo resp = pop.get(0);

        for (Cromossomo c : pop) {
            if (c.getFitness() < resp.getFitness()) {
                resp = c;
            }
        }

        return resp;
    }

    private ArrayList<Cromossomo> selecionarPais(ArrayList<Cromossomo> populacao) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Cromossomo> realizarCruzamento(ArrayList<Cromossomo> pais) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Cromossomo> realizarMutacao(ArrayList<Cromossomo> filhos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Cromossomo> selecionarNovaPopulacao(ArrayList<Cromossomo> filhos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void imprimirResultadoAtual(Integer geracao, Cromossomo sol) {
        if (this.verbose) {
            System.out.printf("[Gera. %d] (Fit. %f)", geracao, sol.getFitness());
        }
    }

    @Override
    public String toString() {
        String resp = "Algoritmo Genético:";

        resp += "\n Tamanho população = " + this.tamPopulacao;
        resp += "\n Tipo seleção pais = " + this.tipoSelecao;
        resp += "\n Tipo cruzamento = " + this.tipoCruzamento;
        resp += "\n Tipo mutação = " + this.tipoMutacao;
        resp += "\n Taxa mutação = " + this.taxaMutacao;
        resp += "\n Seleção nova população = " + this.tipoSelecaoNovaPopulacao;

        return resp;
    }

    public static void main(String[] args) throws IOException {
        String arquivo = "instances/bus_13_3.pos";

        Rede rede = new Rede(arquivo);
        GA ga = new GA(rede, rede.getNumVertices(), TipoSelecao.ROLETA, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.05D, TipoSelecaoNovaPopulacao.JUNCAO);
        System.out.println(ga + "\n\nExecução:");
        Cromossomo resultado = ga.executar(60 * 1);
    }

}
