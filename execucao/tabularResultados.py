ARQUIVOS = {
    "bus_13_3": 113.9088,
    "bus_29_1": 0.013709432,
    "bus_32_1": 55631859.18,
    "bus_83_11": 123854986.7,
    "bus_135_8": 97708466.33,
    "bus_201_3": 184509887.6,
    "bus_873_7": 45803330890,
    "bus_10476_84": 5.3452714182362E+11
}

CONFIGS = [
    "conf1",
    "conf2",
    "conf3",
    "conf4",
    "conf5",
    "conf6",
    "conf7",
    "conf8",
    "conf9",
    "conf10"
]

PASTA_RESULTADOS = "C:\\Users\\jonat\\Desktop\\MO824A_projeto\\execucao\\resultados\\"


def extrairFistess(arq_, conf_):
    f = 0.0
    achou = False
    nome_arq = PASTA_RESULTADOS + conf_ + arq_.replace("bus", "") + ".txt"
    
    file = open(nome_arq, 'r')
    for linha in file:
        if linha.__contains__("Resultado:"):
            achou = True
            continue
        
        if achou:
            partes = linha.split(" ")
            f = eval(partes[2])
            break
    
    return f


# CODIGO PRINCIPAL
for arquivo in ARQUIVOS:
    print("\nArquivo " + arquivo)
    
    for conf in CONFIGS:
        fitness = extrairFistess(arquivo, conf)
        val_original = ARQUIVOS[arquivo]
        melhora = (val_original - fitness) / val_original
        melhora *= 100

        #print("%f \t %f \t %f%%" % (val_original, fitness, melhora))
        print(str(val_original).replace(".", ",") + " \t " + str(fitness).replace(".", ",") + " \t " + str(melhora).replace(".", ",") + "%")
    
    print("\n")
