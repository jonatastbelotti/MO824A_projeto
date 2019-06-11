package ga.configuracoes;

/**
 *
 * @author Jônatas Trabuco Belotti [jonatas.t.belotti@hotmail.com]
 */
public enum TipoMutacao {
    ESTATICA("Mutação Estática", 0D),
    ADAPTATIVA("Mutação Adaptativa", 0.5D);

    public String nome;
    public Double valMaximo;

    private TipoMutacao(String nome, Double valMaximo) {
        this.nome = nome;
        this.valMaximo = valMaximo;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}
