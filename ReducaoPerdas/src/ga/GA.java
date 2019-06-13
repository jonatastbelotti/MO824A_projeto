package ga;

import ga.configuracoes.BuscaLocal;
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
    private List<Cromossomo> populacao;
    private Cromossomo melhorSolucao;
    private Boolean verbose = Boolean.TRUE;
    private TipoSelecao tipoSelecao;
    private TipoCruzamento tipoCruzamento;
    private TipoMutacao tipoMutacao;
    private Double taxaMutacao = 0D;
    private TipoSelecaoNovaPopulacao tipoSelecaoNovaPopulacao;
    private BuscaLocal buscaLocal;

    public GA(Rede rede, Integer tamPopulacao, TipoSelecao selecao, TipoCruzamento cruzamento, TipoMutacao mutacao, Double taxMutacao, TipoSelecaoNovaPopulacao selecaoNovaPop, BuscaLocal buscaLocal) {
        this.rede = rede;
        this.tamPopulacao = tamPopulacao;
        this.tipoSelecao = selecao;
        this.tipoCruzamento = cruzamento;
        this.tipoMutacao = mutacao;
        this.taxaMutacao = taxMutacao;
        this.tipoSelecaoNovaPopulacao = selecaoNovaPop;
        this.buscaLocal = buscaLocal;

        if (this.tamPopulacao % 2 == 1) {
            this.tamPopulacao++;
        }
    }

    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
    }
    
    public Cromossomo executar(Integer tempoExecucao) {
        return executar(tempoExecucao, null);
    }

    public Cromossomo executar(Integer tempoExecucao, Integer maxGeraSemMelhora) {
        Integer geracao, gerSemMelhora;
        Long tempInicial;

        // criando população inicial
        tempInicial = System.currentTimeMillis();
        geracao = 0;
        gerSemMelhora = 0;
        this.iniciarPopulacao();

        // avaliando população inicial
        this.avaliarPopulacao(this.populacao);

        // selecionando melhor individuo
        this.melhorSolucao = new Cromossomo(this.buscarMelhorCromossomo(this.populacao));
        this.imprimirResultadoAtual(geracao, this.melhorSolucao);

        // executando iterações do algoritmo
        while (((System.currentTimeMillis() - tempInicial) / 1000D) < tempoExecucao && (maxGeraSemMelhora == null || (maxGeraSemMelhora != null && gerSemMelhora < maxGeraSemMelhora))) {
            // incrementando geração
            geracao++;
            gerSemMelhora++;

            // selecionando pais para o cruzamento
            List<Cromossomo> pais = this.selecionarPais(this.populacao);

            // gerando os filhos através do cruzamento
            List<Cromossomo> filhos = this.realizarCruzamento(pais);

            // realizando mutação
            calcularTaxaMutacao(populacao);
            filhos = this.realizarMutacao(filhos);

            // avalia novos individuos
            this.avaliarPopulacao(filhos);

            // aplica busca local nos filhos
            this.executarBuscaLocal(filhos, geracao);

            // seleciona nova população
            this.populacao = this.selecionarNovaPopulacao(this.populacao, filhos);

            // atualizando melhor solução
            Cromossomo atual = this.buscarMelhorCromossomo(this.populacao);

            if (atual.getFitness() < this.melhorSolucao.getFitness()) {
                this.melhorSolucao = new Cromossomo(atual);
                this.imprimirResultadoAtual(geracao, this.melhorSolucao);
                gerSemMelhora = 0;
            }
        }

        return this.melhorSolucao;
    }

    private void iniciarPopulacao() {
        this.populacao = new ArrayList<>();

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

    private List<Cromossomo> selecionarPais(List<Cromossomo> pop) {
        switch (tipoSelecao) {
            case ROLETA:
                return selecionarPaisRoleta(pop);
            case TORNEIO:
                return selecionarPaisTorneio(pop);
            case AMOSTRAGEM:
                return selecionarPaisAmostragem(pop);
            default:
                return selecionarPaisRoleta(pop);
        }
    }

    private List<Cromossomo> realizarCruzamento(List<Cromossomo> pais) {
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

    private List<Cromossomo> realizarMutacao(List<Cromossomo> pop) {
        Random random = new Random();
        Cromossomo c;

        // Verifica quantos genes sofrerão mutação
        int quantMutacao = (int) (pop.size() * taxaMutacao);

        for (int n = 0; n < quantMutacao; n++) {
            // seleciona o cromossomo
            c = pop.get(random.nextInt(pop.size()));

            // seleciona o gene do cromossomo e realiza a mutação
            c.mutarGene(random.nextInt(rede.getNumArestas()));
        }

        return pop;
    }

    private void executarBuscaLocal(List<Cromossomo> pop, Integer geracoes) {
        if (buscaLocal == BuscaLocal.SIM && geracoes % buscaLocal.qtdGeracoes == 0) {
            // faz a busca local em cada cromossomo
            for (Cromossomo c : pop) {
                c.realizarBucaLocal(rede);
            }
        }
    }

    private List<Cromossomo> selecionarNovaPopulacao(List<Cromossomo> pop, List<Cromossomo> filhos) {
        switch (tipoSelecaoNovaPopulacao) {
            case JUNCAO:
                return juntarPopulacoes(pop, filhos);
            case SUBSTITUICAO_0:
            case SUBSTITUICAO_1:
            default:
                return substituirPopulacao(pop, filhos, tipoSelecaoNovaPopulacao.eletismo);
        }
    }

    private List<Cromossomo> selecionarPaisRoleta(List<Cromossomo> pop) {
        List<Cromossomo> pais = new ArrayList<>();
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

    private List<Cromossomo> selecionarPaisTorneio(List<Cromossomo> pop) {
        List<Cromossomo> pais = new ArrayList<>();
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

    private List<Cromossomo> selecionarPaisAmostragem(List<Cromossomo> pop) {
        List<Cromossomo> pais = new ArrayList<>();
        Double somaTotal = 0D;
        List<Double> setas = new ArrayList<>();
        Double espaco, soma, iniCromossomo, fimCromossomo;

        // calculando probabilidade total
        for (Cromossomo c : pop) {
            somaTotal += 1D / (1D + c.getFitness());
        }

        // Embarralhando a população
        Collections.shuffle(pop);

        // Posicionando todas as setas na roleta universal
        espaco = somaTotal / pop.size();
        soma = 0D;
        while (setas.size() < pop.size()) {
            setas.add(soma);
            soma += espaco;
        }

        // Selecionando todos os pais
        for (Double valSeta : setas) {
            iniCromossomo = 0D;

            for (Cromossomo c : pop) {
                fimCromossomo = iniCromossomo + (1D / (1D + c.getFitness()));

                if (valSeta >= iniCromossomo && valSeta <= fimCromossomo) {
                    pais.add(c);
                    break;
                }

                iniCromossomo = fimCromossomo;
            }
        }

        return pais;
    }

    private List<Cromossomo> realizarCruzamentoPonto(List<Cromossomo> pais, int pontos) {
        List<Cromossomo> filhos = new ArrayList<>();
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

    private List<Cromossomo> realizarCruzamentoUniforme(List<Cromossomo> pais) {
        Random random = new Random();
        List<Cromossomo> filhos = new ArrayList<>();
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

    private void calcularTaxaMutacao(List<Cromossomo> pop) {
        // Se for mutação adaptativa tem que recalcular a taxa de mutação antes
        if (tipoMutacao == TipoMutacao.ADAPTATIVA) {
            double media = 0D;
            double maior = 0D;

            // calculando media e buscando maior
            for (Cromossomo c : pop) {
                media += c.getFitness();

                if (c.getFitness() > maior) {
                    maior = c.getFitness();
                }
            }
            media /= pop.size();

            // calculando desvio padrão
            double desvio = 0D;
            for (Cromossomo c : pop) {
                desvio += Math.pow(c.getFitness() - media, 2D);
            }
            desvio = Math.sqrt(desvio / pop.size()) / maior;

            // Recalculando taxa de mutação
            taxaMutacao = tipoMutacao.valMaximo * (1D - desvio);
        }
    }

    private List<Cromossomo> juntarPopulacoes(List<Cromossomo> pop, List<Cromossomo> filhos) {
        List<Cromossomo> novaPopulacao = new ArrayList<>();

        // Juntando a população atual com os filhos
        novaPopulacao.addAll(pop);
        novaPopulacao.addAll(filhos);

        // Ordenando a nova população pelo valor do fitnes
        novaPopulacao.sort(new CromossomoComparator());

        // retornando apenas os melhores individuos
        return novaPopulacao.subList(0, this.tamPopulacao);
    }

    private List<Cromossomo> substituirPopulacao(List<Cromossomo> pop, List<Cromossomo> filhos, int eletismo) {
        List<Cromossomo> melhores = new ArrayList<>();

        // Ordenando população atual
        pop.sort(new CromossomoComparator());

        // Selecionando os melhores da população atual
        melhores = pop.subList(0, eletismo);

        return juntarPopulacoes(melhores, filhos);
    }

    private void imprimirResultadoAtual(Integer geracao, Cromossomo sol) {
        if (this.verbose) {
            System.out.printf("[Gera. %d] (Fit. %f)\n", geracao, sol.getFitness());
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
        resp += "\n Busca local = " + this.buscaLocal;

        return resp;
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

        Double t = 50D;
        GA ga = new GA(rede, t.intValue(), TipoSelecao.AMOSTRAGEM, TipoCruzamento.UNIFORME, TipoMutacao.ADAPTATIVA, 0.1D, TipoSelecaoNovaPopulacao.JUNCAO, BuscaLocal.SIM);

        System.out.println(rede + "\n\n" + ga + "\n\nExecução:");

        Cromossomo resultado = ga.executar((int) (60 * 0.1));

        System.out.println("\nResultado:");
        System.out.printf(" Fit. %f Arestas: %s\n", resultado.getFitness(), resultado.buscarArestasEmUso());

        resultado.plotarRede("resultado GA", rede);
    }

}
