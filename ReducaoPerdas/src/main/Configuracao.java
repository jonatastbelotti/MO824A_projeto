package main;

import ga.configuracoes.BuscaLocal;
import ga.configuracoes.TipoCruzamento;
import ga.configuracoes.TipoMutacao;
import ga.configuracoes.TipoSelecao;
import ga.configuracoes.TipoSelecaoNovaPopulacao;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public class Configuracao {

    public TipoSelecao tipoSelecao;
    public TipoCruzamento tipoCruzamento;
    public TipoMutacao tipoMutacao;
    public Double taxMutacao;
    public TipoSelecaoNovaPopulacao tipoSelecNovaPop;
    public BuscaLocal buscaLocal;

    public Configuracao(TipoSelecao tipoSelecao, TipoCruzamento tipoCruzamento, TipoMutacao tipoMutacao, Double taxMutacao, TipoSelecaoNovaPopulacao tipoSelecNovaPop, BuscaLocal buscaLocal) {
        this.tipoSelecao = tipoSelecao;
        this.tipoCruzamento = tipoCruzamento;
        this.tipoMutacao = tipoMutacao;
        this.taxMutacao = taxMutacao;
        this.tipoSelecNovaPop = tipoSelecNovaPop;
        this.buscaLocal = buscaLocal;
    }

}
