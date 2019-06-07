package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoSelecao {
    ROLETA("Roleta"),
    TORNEIO("Torneio");

    public String nome;

    private TipoSelecao(String nome) {
        this.nome = nome;
    }

}
