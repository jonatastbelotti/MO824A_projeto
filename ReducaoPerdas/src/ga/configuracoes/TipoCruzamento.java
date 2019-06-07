package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoCruzamento {
    PONTO_2("2 Pontos", 2),
    PONTO_3("2 Pontos", 3),
    PONTO_4("2 Pontos", 4);

    public String nome;
    public int pontos;

    private TipoCruzamento(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

}
