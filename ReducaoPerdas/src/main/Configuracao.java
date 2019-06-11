package main;

import ga.configuracoes.TipoCruzamento;
import ga.configuracoes.TipoMutacao;
import ga.configuracoes.TipoSelecao;
import ga.configuracoes.TipoSelecaoNovaPopulacao;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Configuracao {

    public int tamPop;
    public TipoSelecao tipoSelecao;
    public TipoCruzamento tipoCruzamento;
    public TipoMutacao tipoMutacao;
    public Double taxMutacao;
    public TipoSelecaoNovaPopulacao tipoSelecNovaPop;

    public Configuracao(int tamPop, TipoSelecao tipoSelecao, TipoCruzamento tipoCruzamento, TipoMutacao tipoMutacao, Double taxMutacao, TipoSelecaoNovaPopulacao tipoSelecNovaPop) {
        this.tamPop = tamPop;
        this.tipoSelecao = tipoSelecao;
        this.tipoCruzamento = tipoCruzamento;
        this.tipoMutacao = tipoMutacao;
        this.taxMutacao = taxMutacao;
        this.tipoSelecNovaPop = tipoSelecNovaPop;
    }

}
