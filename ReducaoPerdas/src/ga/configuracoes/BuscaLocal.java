package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum BuscaLocal {
    SIM("Sim a cada 50 gerações", 100),
    NAO("Não", 0);

    public String nome;
    public Integer qtdGeracoes;

    private BuscaLocal(String nome, Integer qtdGeracoes) {
        this.nome = nome;
        this.qtdGeracoes = qtdGeracoes;
    }

}
