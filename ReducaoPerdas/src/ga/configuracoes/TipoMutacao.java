package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoMutacao {
    ESTATICA("Mutação Estática"),
    ADAPTATIVA("Mutação Adaptativa");

    public String nome;

    private TipoMutacao(String nome) {
        this.nome = nome;
    }

}
