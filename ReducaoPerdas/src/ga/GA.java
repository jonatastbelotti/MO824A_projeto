package ga;

import ga.configuracoes.TipoCruzamento;
import ga.configuracoes.TipoMutacao;
import ga.configuracoes.TipoSelecao;
import ga.configuracoes.TipoSelecaoNovaPopulacao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    private ArrayList<Cromossomo> selecionarPais(ArrayList<Cromossomo> pop) {
        switch (tipoSelecao) {
            case ROLETA:
                return selecionarPaisRoleta(pop);
            case TORNEIO:
                return selecionarPaisTorneio(pop);
            default:
                return selecionarPaisRoleta(pop);
        }
    }

    private ArrayList<Cromossomo> realizarCruzamento(ArrayList<Cromossomo> pais) {
        switch (tipoCruzamento) {
            case PONTO_2:
            case PONTO_3:
            case PONTO_4:
                return realizarCruzamentoPonto(pais, tipoCruzamento.pontos);
            case UNIFORME:
            default:
                return realizarCruzamentoUniforme(pais);
        }
    }

    private ArrayList<Cromossomo> realizarMutacao(ArrayList<Cromossomo> filhos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Cromossomo> selecionarNovaPopulacao(ArrayList<Cromossomo> filhos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Cromossomo> selecionarPaisRoleta(ArrayList<Cromossomo> pop) {
        ArrayList<Cromossomo> pais = new ArrayList<>();
        Random random = new Random();
        Double somaTotal = 0D;
        Double sorteio = 0D;
        Double soma = 0D;

        // calculando probabilidades de cada cromossomo ser selecionado
        for (Cromossomo c : pop) {
            somaTotal += 1D / (1D + c.getFitness());
        }

        // embaralhando população
        Collections.shuffle(pop);

        // Selecionando os pais
        while (pais.size() < pop.size()) {
            // sorteando um número
            sorteio = random.nextDouble() * somaTotal;
            soma = 0D;

            for (Cromossomo c : pop) {
                soma += 1D / (1D + c.getFitness());

                if (soma >= sorteio) {
                    pais.add(c);
                    break;
                }
            }
        }

        return pais;
    }

    private ArrayList<Cromossomo> selecionarPaisTorneio(ArrayList<Cromossomo> pop) {
        ArrayList<Cromossomo> pais = new ArrayList<>();
        Random random = new Random();
        Cromossomo c1, c2;

        // Selecionando os pais
        while (pais.size() < pop.size()) {
            c1 = pop.get(random.nextInt(pop.size()));
            c2 = pop.get(random.nextInt(pop.size()));

            // O vencedor é o com menor fitness
            if (c1.getFitness() < c2.getFitness()) {
                pais.add(c1);
            } else {
                pais.add(c2);
            }

        }

        return pais;
    }

    private ArrayList<Cromossomo> realizarCruzamentoPonto(ArrayList<Cromossomo> pais, int pontos) {
        ArrayList<Cromossomo> filhos = new ArrayList<>();
        Cromossomo p1, p2, p_aux, f1, f2;
        int tamPonto, limitePonto;

        // percorrendo todos os pares de pais
        for (int i = 0; i < pais.size(); i += 2) {
            p1 = pais.get(i);
            p2 = pais.get(i + 1);

            f1 = new Cromossomo();
            f2 = new Cromossomo();

            tamPonto = limitePonto = p1.size() / pontos;

            // percorrendo cada gene dos pais
            for (int j = 0; j < p1.size(); j++) {
                // verificando se trocou de ponto
                if (j >= limitePonto) {
                    limitePonto += tamPonto;
                    p_aux = p1;
                    p1 = p2;
                    p2 = p_aux;
                }
                
                f1.add(p1.get(j));
                f2.add(p2.get(j));
            }

            // adicionando os novos filhos
            filhos.add(f1);
            filhos.add(f2);
        }

        return filhos;
    }

    private ArrayList<Cromossomo> realizarCruzamentoUniforme(ArrayList<Cromossomo> pais) {
        Random random = new Random();
        ArrayList<Cromossomo> filhos = new ArrayList<>();
        Cromossomo p1, p2, f1, f2;

        // percorrendo todos os pares de pais
        for (int i = 0; i < pais.size(); i += 2) {
            p1 = pais.get(i);
            p2 = pais.get(i + 1);

            f1 = new Cromossomo();
            f2 = new Cromossomo();

            // percorrendo cada gene dos pais
            for (int j = 0; j < p1.size(); j++) {
                // verificando que filho recebe o gene de que pai
                if (random.nextBoolean()) {
                    f1.add(p1.get(j));
                    f2.add(p2.get(j));
                } else {
                    f2.add(p1.get(j));
                    f1.add(p2.get(j));
                }
            }

            // adicionando os novos filhos
            filhos.add(f1);
            filhos.add(f2);
        }

        return filhos;
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
