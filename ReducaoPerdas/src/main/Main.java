package main;

import ga.Cromossomo;
import ga.GA;
import ga.configuracoes.BuscaLocal;
import ga.configuracoes.TipoCruzamento;
import ga.configuracoes.TipoMutacao;
import ga.configuracoes.TipoSelecao;
import ga.configuracoes.TipoSelecaoNovaPopulacao;
import java.io.IOException;
import java.util.HashMap;
import rede.Rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String nomeConf, nomeArq, nomeGrafico;
        HashMap<String, Configuracao> configuracoes = new HashMap<>();
        Configuracao conf;
        int tPop = 30;
        int maxGeracoesSemMelhora = 1000000;
        
        // Adicionando configurações
        configuracoes.put("conf1", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        configuracoes.put("conf2", new Configuracao(TipoSelecao.ROLETA, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        configuracoes.put("conf3", new Configuracao(TipoSelecao.AMOSTRAGEM, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        
        configuracoes.put("conf4", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.UNIFORME, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        
        configuracoes.put("conf5", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.2D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        configuracoes.put("conf6", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ADAPTATIVA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_0, BuscaLocal.NAO));
        
        configuracoes.put("conf7", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.SUBSTITUICAO_1, BuscaLocal.NAO));
        configuracoes.put("conf8", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.JUNCAO, BuscaLocal.NAO));
        
        configuracoes.put("conf9", new Configuracao(TipoSelecao.TORNEIO, TipoCruzamento.PONTO_2, TipoMutacao.ESTATICA, 0.1D, TipoSelecaoNovaPopulacao.JUNCAO, BuscaLocal.SIM));
        
        configuracoes.put("conf10", new Configuracao(TipoSelecao.AMOSTRAGEM, TipoCruzamento.UNIFORME, TipoMutacao.ADAPTATIVA, 0.1D, TipoSelecaoNovaPopulacao.JUNCAO, BuscaLocal.NAO));

        // Verificando se foram informados todos os parâmetros
        if (args.length != 3) {
            System.out.println("Informe a configuração, o arquivo de entrada e o grafico!");
            System.exit(0);
        }

        // Pegando parâmetros de execução
        nomeConf = args[0];
        nomeArq = args[1];
        nomeGrafico = args[2];

        // Verificando se existe essa configuração
        if (!configuracoes.containsKey(nomeConf)) {
            System.out.println("A configuração informada não foi definida!");
            System.exit(0);
        }
        
        // Pegando objeto com os parâmetros de configuração
        conf = configuracoes.get(nomeConf);
        
        // Verificando tamanho da população
        if (nomeArq.contains("bus_13_3") || nomeArq.contains("bus_29_1") || nomeArq.contains("bus_32_1") || nomeArq.contains("bus_83_11") || nomeArq.contains("bus_135_8") || nomeArq.contains("bus_201_3")) {
            tPop = 30;
        } else if (nomeArq.contains("bus_873_7")) {
            tPop = 50;
        } else if (nomeArq.contains("bus_10476_84")) {
            tPop = 100;
        }
        
        // Instanciando rede e GA
        Rede rede = new Rede(nomeArq);
        GA ga = new GA(rede, tPop, conf.tipoSelecao, conf.tipoCruzamento, conf.tipoMutacao, conf.taxMutacao, conf.tipoSelecNovaPop, conf.buscaLocal);
        System.out.println(rede + "\n\n" + ga + "\n\nExecução:");
        
        Cromossomo resultado = ga.executar((int) (60 * 30), maxGeracoesSemMelhora);
        
        System.out.println("\nResultado:");
        System.out.printf(" Fit. %f Arestas: %s\n", resultado.getFitness(), resultado.buscarArestasEmUso());

        resultado.plotarRede("resultado GA", rede);
    }

}
