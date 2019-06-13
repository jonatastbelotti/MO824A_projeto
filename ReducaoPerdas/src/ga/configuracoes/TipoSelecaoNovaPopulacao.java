package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoSelecaoNovaPopulacao {
    SUBSTITUICAO_1("Substituição com eletismo de 1", 1),
    SUBSTITUICAO_0("Substituição sem eletismo", 0),
    JUNCAO("Junção e seleção dos melhores", 0);

    public String nome;
    public int eletismo;

    private TipoSelecaoNovaPopulacao(String nome, int eletismo) {
        this.nome = nome;
        this.eletismo = eletismo;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}
