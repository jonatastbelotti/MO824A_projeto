package main;

import ga.GA;
import java.io.IOException;
import java.util.HashMap;
import rede.Rede;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String nomeConf, nomeArq;
        HashMap<String, Configuracao> configuracoes = new HashMap<>();
        Configuracao conf;

        // Verificando se foram informados todos os parâmetros
        if (args.length != 2) {
            System.out.println("Informe a configuração e o arquivo de entrada!");
            System.exit(0);
        }

        // Pegando parâmetros de execução
        nomeConf = args[0];
        nomeArq = args[1];

        // Verificando se existe essa configuração
        if (configuracoes.containsKey(nomeConf)) {
            System.out.println("A configuração informada não foi definida!");
            System.exit(0);
        }
        
        // Pegando objeto com os parâmetros de configuração
        conf = configuracoes.get(nomeConf);
        
        // Instanciando rede e GA
        Rede rede = new Rede(nomeArq);
        GA ga = new GA(rede, conf.tamPop, conf.tipoSelecao, conf.tipoCruzamento, conf.tipoMutacao, conf.taxMutacao, conf.tipoSelecNovaPop);
        System.out.println(rede + "\n\n" + ga + "\n\nExecução:");
    }

}
