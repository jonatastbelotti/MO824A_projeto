package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoSelecaoNovaPopulacao {
    SUBSTITUICAO("Substituição com eletismo", 1),
    JUNCAO("Junção e seleção", 0);

    public String nome;
    public int eletismo;

    private TipoSelecaoNovaPopulacao(String nome, int eletismo) {
        this.nome = nome;
        this.eletismo = eletismo;
    }

}
